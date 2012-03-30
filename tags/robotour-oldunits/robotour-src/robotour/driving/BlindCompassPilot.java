package robotour.driving;

import robotour.iface.MeasureException;
import robotour.iface.Wheels;
import robotour.iface.Compass;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.navi.basic.Angle;
import robotour.navi.basic.Azimuth;

/**
 *
 * @author Tomas
 */
public class BlindCompassPilot {

    private double power = 0.3;
    private double steerpower = 0.3;
    private final Wheels wheels;
    private final Compass cmps;
    private final AtomicBoolean moving = new AtomicBoolean(false);
    private volatile Double speed;
    private volatile Double steer;

    public BlindCompassPilot(Wheels wheels, Compass cmps) {
        this.wheels = wheels;
        this.cmps = cmps;
        new Thread(new Runnable() {

            public void run() {
                while (true) {
                    push();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(BlindCompassPilot.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }).start();
    }

    public void setPower(double power) {
        this.power = power;
    }

    boolean isMoving() {
        return moving.get();
    }

    /**
     * Travels distance meters forward. Backwards if distance is negative.
     *
     * @param distance
     * @param immediateReturn if method is nonblocking
     */
    public void travel(double distance, boolean immediateReturn) {
//        final long duration = Math.abs(Math.round(1000.0 * distance / powerToSpeed(power)));
        final long duration = (long)Math.abs(1000.0 * distance);
        System.out.println("Travel " + distance + " m = " + duration + " ms");
        go(power * Math.signum(distance), null, duration, immediateReturn);
    }

    /**
     * Makes a rotation of angle clockwise. Counterclockwise if negative.
     *
     * @param angle
     * @param immediateReturn if method is nonblocking
     */
    public void rotate(Angle angle, boolean immediateReturn) {
        final long duration = Math.abs(Math.round(1000.0 * angle.radians() / powerToAngularSpeed(steerpower)));
//        final long duration = Math.abs(Math.round(1000.0 * angle.radians()));
        //final long duration = 1000;
        System.out.println("Rotate " + angle + " = " + duration + " ms");
        go(null, steerpower * Math.signum(angle.radians()), duration, immediateReturn);
    }
//    private double maxAngSpeed = RTOdometry.powerToAngularSpeed(1);
    private double maxAngSpeed = 1.0; // mps

    private double powerToAngularSpeed(double steerpower) {
        return maxAngSpeed * steerpower;
    }

    /**
     * Rotates by shorter side to selected azimuth. May use compass.
     *
     * @param azimuth
     * @param immediateReturn if method is nonblocking
     */
    public void rotateToAndCalib(Azimuth azimuth) {
        try {
            while (true) {
                Angle angle = azimuth.sub(cmps.getAzimuth()).shorter();
                this.rotate(angle, false);
                Angle error = azimuth.sub(cmps.getAzimuth()).shorter();
                if (Math.abs(error.degrees()) < 4) {
                    return;
                } else {
                    
                    System.out.println("Rotate error: " + error + " - " + (error.radians() / angle.radians()));
                    maxAngSpeed *= 1 - (error.radians() / angle.radians());
                // correct error
                }
            }
        } catch (MeasureException ex) {
            Logger.getLogger(BlindCompassPilot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Rotates by shorter side to selected azimuth. May use compass.
     * 
     * @param azimuth
     * @param immediateReturn if method is nonblocking
     */
    public void rotateTo(Azimuth azimuth, boolean immediateReturn) {
        try {
            Angle angle = azimuth.sub(cmps.getAzimuth());
            while (angle.radians() > Math.PI) {
                angle = angle.sub(Angle.valueOfDegrees(360));
            }
            while (angle.radians() < -Math.PI) {
                angle = angle.add(Angle.valueOfDegrees(360));
            }
            this.rotate(angle, immediateReturn);
        } catch (MeasureException ex) {
            Logger.getLogger(BlindCompassPilot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //private static final Object monitor = new Object();

    private synchronized void go(Double speed, Double steer, long duration, boolean immediateReturn) {
        stopPrevious();
        moving.set(true);
        this.speed = speed;
        this.steer = steer;
        push();
        stopAfter(duration);
        if (!immediateReturn) {
            while (isMoving()) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(BlindCompassPilot.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(BlindCompassPilot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return;

    }

    public void stop() {
        stopPrevious();
    }

    private synchronized void push() {
        System.out.println("push");
        if (speed != null) {
            wheels.setSpeed(speed);
        }
        if (steer != null) {
            wheels.setSteer(steer);
        }
    }
    Timer timer = new Timer();

    private void stopAfter(long duration) {
//        System.out.println("Dur: "+duration);
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                stopPrevious();
            }
        }, duration);
    }

    private synchronized void stopPrevious() {
        timer.cancel();
        this.speed = null;
        this.steer = null;
        wheels.stop();
        moving.set(false);
        notifyAll();
    }
}
