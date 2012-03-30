package robotour.driving;

import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.iface.DiffWheels;

/**
 *
 * @author Kotuc
 */
public class DiffDriver {

    final DiffWheels diffWheels;
    volatile Thread move = null;

    public DiffDriver(DiffWheels diffWheels) {
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
//    public void travel(double distance, boolean immediateReturn) {
//        final long duration = Math.abs(Math.round(1000.0 * distance / RTOdometry.powerToSpeed(power)));
//        System.out.println("Travel " + distance + " m = " + duration + " ms");
//        go(power * Math.signum(distance), null, duration, immediateReturn);
//    }

    /**
     * Makes a rotation of angle clockwise. Counterclockwise if negative.
     *
     * @param angle
     * @param immediateReturn if method is nonblocking
     */
//    public void rotate(Angle angle, boolean immediateReturn) {
//        final long duration = Math.abs(Math.round(1000.0 * angle.radians() / powerToAngularSpeed(steerpower)));
//        //final long duration = 1000;
//        System.out.println("Rotate " + angle + " = " + duration + " ms");
//        go(null, steerpower * Math.signum(angle.radians()), duration, immediateReturn);
//    }
//    private double maxAngSpeed = RTOdometry.powerToAngularSpeed(1);

//    private double powerToAngularSpeed(double steerpower) {
//        return maxAngSpeed * steerpower;
//    }

    /**
     * Rotates by shorter side to selected azimuth. May use compass.
     *
     * @param azimuth
     * @param immediateReturn if method is nonblocking
     */
//    public void rotateTo(Azimuth azimuth, boolean immediateReturn) {
//        Angle angle = azimuth.sub(cmps.getAzimuth()).shorter();
//        this.rotate(angle, immediateReturn);
//    }
    //private static final Object monitor = new Object();    

    private synchronized void go(double left, double right, long durationms, boolean immediateReturn) {
        diffWheels.setSpeedsLR(left, right);
        this.move = new StopIn(durationms);
        move.start();
        if (!immediateReturn) {
            try {
                move.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(DiffDriver.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(DiffDriver.class.getName()).log(Level.SEVERE, null, ex);
            }
            diffWheels.stop();

            //        startt = System.currentTimeMillis();
//        endt = startt + 1000;
//
//        while (System.currentTimeMillis() < endt) {
//        }

        }
    }

    void waitForStop() {
        
    }

}
