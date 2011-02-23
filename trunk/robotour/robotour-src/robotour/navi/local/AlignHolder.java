package robotour.navi.local;

import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.iface.MeasureException;
import robotour.iface.Sonar;
import robotour.iface.Wheels;
import robotour.navi.gps.Azimuth;
import robotour.util.RobotSystems;

/**
 *
 * @author Tomas
 */
public class AlignHolder implements Runnable {

    private final double proportional = 1;
    private final double integral = 0.3;
    private double integralSum = 0;
    private final double derivative = 0.;
    private double previous = 0;
    private final Sonar leftSonar;
    private final Sonar rigtSonar;
    private final Wheels wheels;

    public AlignHolder(Sonar leftSonar, Sonar rigtSonar, Wheels wheels) {
        this.leftSonar = leftSonar;
        this.rigtSonar = rigtSonar;
        this.wheels = wheels;
    }
    volatile boolean enabled;
    private double hold;

    public void hold(double ratio) {
        this.hold = ratio;
        run();
    }

    public void run() {
        enabled = true;
        while (enabled) {
            try {

                double leftDist = leftSonar.getDistance();
                double rightDist = rigtSonar.getDistance();

                double error = leftDist - rightDist;
                System.out.println("Align error: " + error);

                integralSum = 0.6 * (integralSum + error);

                wheels.setSteer(error * proportional +
                        integralSum * integral +
                        (error - previous) * derivative);

                previous = error;

            } catch (MeasureException ex) {
                wheels.stop();
                Logger.getLogger(AlignHolder.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(AlignHolder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void main(String[] args) {
        RobotSystems systems = RobotSystems.getDefault();
        new AlignHolder(systems.getLeftSonar(), systems.getRightSonar(), systems.getWheels()).hold(0.5);
    }
}
