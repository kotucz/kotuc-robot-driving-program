/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package robotour.util;

import java.util.Timer;
import java.util.TimerTask;
import robotour.iface.Wheels;

/**
 *
 * @author Tomas
 */
public class TimeoutWheels implements Wheels {

    private Timer timer = new Timer();
    private TimerTask timeoutTask;
    private final int timeout = 1000;
    private final Wheels wheels;

    public TimeoutWheels(Wheels wheels) {
        this.wheels = wheels;
    }

    /**
     * starts timeout to stop wheels when no signal comes
     */
    private synchronized void resetTimeout() {
        if (timeoutTask != null) {
            timeoutTask.cancel();
        }
        timeoutTask = new TimerTask() {

            @Override
            public void run() {
                System.err.println("Wheels timeout: No command came from client for "+timeout+" ms. STOP");
                stop();

            }
        };
        timer.schedule(timeoutTask, timeout);
    }

    public void setSpeed(double speed) {
        wheels.setSpeed(speed);
        resetTimeout();
    }

    public void setSteer(double steer) {
        wheels.setSteer(steer);
        resetTimeout();
    }

    public void stop() {
        wheels.stop();
        // resetTimeout(); pointless
    }
}
