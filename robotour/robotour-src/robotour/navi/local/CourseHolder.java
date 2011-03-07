package robotour.navi.local;

import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.iface.Compass;
import robotour.iface.MeasureException;
import robotour.iface.Wheels;
import robotour.navi.basic.Azimuth;
import robotour.util.RobotSystems;

/**
 *
 * @author Tomas
 */
public class CourseHolder implements Runnable {

    private final double proportional = 0.8;
    private final double integral = 0.3;
    private double integralSum = 0;
    private final double derivative = 0.5;
    private double previous = 0;
    private final Compass compass;
    private final Wheels wheels;

    public CourseHolder(Compass compass, Wheels wheels) {
        this.compass = compass;
        this.wheels = wheels;
    }
    volatile boolean enabled;
    private Azimuth hold;

    public void hold(Azimuth azimuth) {
        this.hold = azimuth;
        run();
    }

    public void run() {
        enabled = true;
        while (enabled) {
            try {
                
                double error = hold.sub(compass.getAzimuth()).shorter().radians();
                System.out.println("Course error: "+error);

                integralSum = 0.9 * (integralSum + error);

                wheels.setSteer(error * proportional +
                        integralSum * integral +
                        (error - previous) * derivative);

                previous = error;

            } catch (MeasureException ex) {
                Logger.getLogger(CourseHolder.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(CourseHolder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void main(String[] args) {
        RobotSystems systems = RobotSystems.getDefault();
        new CourseHolder(systems.getCompass(), systems.getWheels()).hold(Azimuth.valueOfDegrees(360*Math.random()));
    }
}
