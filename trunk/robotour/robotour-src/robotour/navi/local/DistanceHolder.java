package robotour.navi.local;

import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.iface.MeasureException;
import robotour.iface.Sonar;
import robotour.iface.Wheels;
import robotour.navi.basic.Azimuth;
import robotour.util.RobotSystems;

/**
 *
 * @author Tomas
 */
public class DistanceHolder implements Runnable {

    private final double proportional = 0.5;
    private final double integral = 0.5;
    private final double derivative = 0.0;
    private double integralSum = 0;
    private double previous = 0;
    private final Sonar sonar;
    private final Wheels wheels;

    public DistanceHolder(Sonar sonar, Wheels wheels) {
        this.sonar = sonar;
        this.wheels = wheels;
    }
    volatile boolean enabled;
    private double hold;

    public void hold(double distance, boolean immediateReturn) {
        this.hold = distance;
        if (immediateReturn) {
            new Thread(this).start();
        } else {
            run();
        }
    }

    public void stop() {
        enabled = false;
    }

    public void run() {
        enabled = true;
        while (enabled) {
            try {

                double error = sonar.getDistance() - hold;
                System.out.println("Distance error: " + error);

                integralSum = 0.6 * (integralSum + error);

                wheels.setSpeed(error * proportional +
                        integralSum * integral +
                        (error - previous) * derivative);

                previous = error;

            } catch (MeasureException ex) {
                wheels.setSpeed(0);
                Logger.getLogger(DistanceHolder.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(DistanceHolder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void main(String[] args) {
        RobotSystems systems = RobotSystems.getDefault();
        new DistanceHolder(systems.getCenterSonar(), systems.getWheels()).hold(0.1/* + Math.random() / 2.0*/, false);
    }
}
