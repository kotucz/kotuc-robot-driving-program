package robotour.tests;

import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.iface.Compass;
import robotour.iface.MeasureException;
import robotour.util.RobotSystems;
import robotour.iface.Wheels;
import robotour.navi.gps.Azimuth;

/**
 *
 * @author Tomas
 */
public class CompassTest {

    public static void main(String[] args) throws MeasureException {
        RobotSystems systems = RobotSystems.getDefault();
        Compass cmps = systems.getCompass();
        Wheels wheels = systems.getWheels();

        

        for (long start = System.currentTimeMillis(); true;) {
            wheels.setSteer(0.3);
            long time = System.currentTimeMillis() - start;
            if (time > 60000) {
                break;
            }
            Azimuth az = cmps.getAzimuth();
            System.out.println(time + "\t" + az.degrees());
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(CompassTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
