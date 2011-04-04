package eurobot.kuba;

import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.iface.DiffWheels;
import robotour.navi.basic.Angle;
import robotour.navi.basic.Azimuth;
import robotour.navi.local.odometry.RTOdometry;

/**
 *
 * @author Kotuc
 */
public class Driver {

    final DiffWheels diffWheels;
    volatile Thread move = null;

    class SpeedProfile {

        private static final double maxAcceleration = 0.1;
        private static final double maxSpeed = 0.1;
    }

    class WheelCmd {
        final double leftspeed;
        final double rightspeed;
        final double leftacc;
        final double rightacc;
        final long duration;

        public WheelCmd(double leftspeed, double rightspeed, double leftacc, double rightacc, long duration) {
            this.leftspeed = leftspeed;
            this.rightspeed = rightspeed;
            this.leftacc = leftacc;
            this.rightacc = rightacc;
            this.duration = duration;
        }
    }

    public Driver(DiffWheels diffWheels) {
        this.diffWheels = diffWheels;
    }

    synchronized boolean isMoving() {
        return move != null;
    }

    /**
     * Travels distance meters forward. Backwards if distance is negative.
     *
     * @param distance
     * @param immediateReturn if method is nonblocking
     */
    public void travel(double distance, boolean immediateReturn) {
        final long duration = Math.abs(Math.round(1000.0 * distance / RTOdometry.powerToSpeed(power)));
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
        //final long duration = 1000;
        System.out.println("Rotate " + angle + " = " + duration + " ms");
        go(null, steerpower * Math.signum(angle.radians()), duration, immediateReturn);
    }
    private double maxAngSpeed = RTOdometry.powerToAngularSpeed(1);

    private double powerToAngularSpeed(double steerpower) {
        return maxAngSpeed * steerpower;
    }

    /**
     * Rotates by shorter side to selected azimuth. May use compass.
     *
     * @param azimuth
     * @param immediateReturn if method is nonblocking
     */
    public void rotateTo(Azimuth azimuth, boolean immediateReturn) {
        Angle angle = azimuth.sub(cmps.getAzimuth()).shorter();
        this.rotate(angle, immediateReturn);
    }
    //private static final Object monitor = new Object();
    double leftspeed;
    double rightspeed;
    double leftdist;
    double rightdist;

    private synchronized void go(double left, double right, long durationms, boolean immediateReturn) {
        diffWheels.setSpeedsLR(left, right);
        this.move = new StopIn(durationms);
        move.start();
        if (!immediateReturn) {
            try {
                move.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Driver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void stop() {
        diffWheels.stop();
    }

    class StopIn extends Thread {

        //    long startt;
        //    long endt;
        long millis;

        public StopIn(long millis) {
            this.millis = millis;
        }

        public void run() {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException ex) {
                Logger.getLogger(Driver.class.getName()).log(Level.SEVERE, null, ex);
            }
            diffWheels.stop();

            //        startt = System.currentTimeMillis();
//        endt = startt + 1000;
//
//        while (System.currentTimeMillis() < endt) {
//        }

        }
    }
}
