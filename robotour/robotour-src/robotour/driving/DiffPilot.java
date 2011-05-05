package robotour.driving;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.iface.DiffWheels;
import robotour.navi.basic.Angle;
import robotour.pathing.simple.DiffPlanner;
import robotour.pathing.simple.DiffWheelParameters;

/**
 *
 * @author Tomas
 */
public class DiffPilot {

    private double defspeed = 0.3;
    private double defrotspeed = 0.3;
    private final AtomicBoolean moving = new AtomicBoolean(false);
    private final DiffWheelParameters params = new DiffWheelParameters();
    private final DiffPlanner planner = new DiffPlanner();
    private final DiffWheels wheels;

    public DiffPilot(DiffWheels wheels) {
        this.wheels = wheels;
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
    public void forward(double distance, boolean immediateReturn) {
//        final long duration = Math.abs(Math.round(1000.0 * distance / powerToSpeed(power)));
        final long durationms = (long) Math.abs(1000.0 * distance / defspeed);
        System.out.println("Travel " + distance + " m = " + durationms + " ms");
        startMove(defspeed * Math.signum(distance), defspeed * Math.signum(distance), durationms, immediateReturn);
    }

    /**
     * Makes a rotation of angle clockwise. Counterclockwise if negative.
     *
     * @param angle
     * @param immediateReturn if method is nonblocking
     */
    public void rotate(Angle angle, boolean immediateReturn) {
        double simpleWheelDist = planner.oneWheelOnPlaceRotateDist(angle);

        final long durationms = Math.round(1000.0 * Math.abs(simpleWheelDist) / defrotspeed);
//        final long duration = Math.abs(Math.round(1000.0 * angle.radians() / powerToAngularSpeed(rotspeed)));
//        final long duration = Math.abs(Math.round(1000.0 * angle.radians()));
        //final long duration = 1000;
        System.out.println("Rotate " + angle + " = " + durationms + " ms");
        startMove(defrotspeed * angle.sign(), -defrotspeed * angle.sign(), durationms, immediateReturn);
    }

    /**
     * Rotates by shorter side to selected azimuth. May use compass.
     * 
     * @param azimuth
     * @param immediateReturn if method is nonblocking
     */
//    public void rotateTo(Azimuth azimuth, boolean immediateReturn) {
//        try {
//            this.rotate(angle, immediateReturn);
//        } catch (MeasureException ex) {
//            Logger.getLogger(DiffPilot.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    //private static final Object monitor = new Object();
    private synchronized void startMove(double leftSpeed, double rightSpeed, long durationms, boolean immediateReturn) {
        stopPrevious();
        moving.set(true);
        wheels.setSpeedsLR(leftSpeed, rightSpeed);
        stopAfter(durationms);
        if (!immediateReturn) {
            waitForStop();
        }
        return;

    }

    public void stop() {
        stopPrevious();
    }
    Timer timer = new Timer();

    private void stopAfter(long durationms) {
//        System.out.println("Dur: "+duration);
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                stopPrevious();
            }
        }, durationms);
    }

    private synchronized void stopPrevious() {
        timer.cancel();
        wheels.stop();
        moving.set(false);
        notifyAll();
    }

    synchronized void waitForStop() {
        while (isMoving()) {
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(DiffPilot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(DiffPilot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
