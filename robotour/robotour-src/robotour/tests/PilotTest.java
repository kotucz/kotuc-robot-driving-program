package robotour.tests;

import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.iface.MeasureException;
import robotour.navi.local.BlindPilot;
import robotour.util.RobotSystems;
import robotour.iface.Compass;
import robotour.navi.basic.Angle;
import robotour.navi.basic.Azimuth;

/**
 *
 * @author Tomas
 */
public class PilotTest {

    public static void main(String[] args) throws InterruptedException, MeasureException {
        RobotSystems system = RobotSystems.getDefault();
        BlindPilot pilot = new BlindPilot(system.getWheels(), system.getCompass());
        Compass compas = system.getCompass();

        System.out.println(compas.getAzimuth());

        metreNback(pilot);


        //fwRtNback(pilot);
        //rightSquare(pilot);
        //compassSquare(pilot, compas);

        //eight(pilot);

        //pilot.travel(1.0, false);
        //pilot.travel(-1.0, false);

        System.exit(0);
    }

    static void metreNback(BlindPilot pilot) {
        // forward
        pilot.travel(1.0, false);
        // right
        pilot.rotateToAndCalib(Azimuth.valueOfDegrees(0));
        pilot.travel(1.0, false);
        // turn around
        pilot.rotateToAndCalib(Azimuth.valueOfDegrees(180));

    }

    /**            
     *  >-------v
     *  |v------<
     *  ||
     *  ||
     *  ^|
     *  S^
     */
    static void fwRtNback(BlindPilot pilot) {
        // forward
        pilot.travel(1.0, false);
        // right
        pilot.rotate(Angle.valueOfDegrees(90), false);
        pilot.travel(1.0, false);
        // turn around
        pilot.rotate(Angle.valueOfDegrees(180), false);

        // left
        pilot.travel(1.0, false);
        pilot.rotate(Angle.valueOfDegrees(-90), false);
        // back
        pilot.travel(1.0, false);
        // turn around to original position
        pilot.rotate(Angle.valueOfDegrees(-180), false);
    }

    static void rightSquare(BlindPilot pilot) {
        double side = 1.0;

        for (int i = 0; i < 4; i++) {
            // forward
            pilot.travel(side, false);
            // right
            pilot.rotate(Angle.valueOfDegrees(90), false);
        }

        pilot.rotate(Angle.valueOfDegrees(90), false);

        for (int i = 0; i < 4; i++) {
            // forward
            pilot.travel(side, false);
            // left
            pilot.rotate(Angle.valueOfDegrees(-90), false);
        }

        pilot.rotate(Angle.valueOfDegrees(-90), false);


    }

    private static void compassSquare(BlindPilot pilot, Compass compass) {
        try {
            final double side = 1.0;
            Azimuth azimuth0 = compass.getAzimuth();

            for (int i = 0; i < 12; i++) {

                // forward
                pilot.travel(side, false);
                // right
                Azimuth azimuth = Azimuth.valueOf(azimuth0.add(Angle.valueOfDegrees((i + 1) * 90)));
                pilot.rotateTo(azimuth, false);
            }

//            for (int i = 1; i > -3; i--) {
//                Azimuth azimuth = Azimuth.valueOf(azimuth0.add(Angle.valueOfDegrees(i*90)));
//                pilot.rotate(azimuth.sub(compass.getAzimuth()), false);
//                // forward
//                pilot.travel(side, false);
//                // right
//            }

//            pilot.rotate(Angle.valueOfDegrees(90), false);
//            for (int i = 0; i < 4; i++) {
//                // forward
//                pilot.travel(side, false);
//                // left
//                pilot.rotate(Angle.valueOfDegrees(-90), false);
//            }
//            pilot.rotate(Angle.valueOfDegrees(-90), false);
        } catch (MeasureException ex) {
            Logger.getLogger(PilotTest.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            pilot.stop();
        }


    }

    /**
     *  5     4
     *
     * 2,6   3,7
     *
     * 1,9    8
     *
     * @param pilot
     */
    static void eight(BlindPilot pilot) {
        final double side = 0.5;
        pilot.rotateTo(Azimuth.valueOfDegrees(180), false);

        pilot.travel(side, false);
        pilot.rotateTo(Azimuth.valueOfDegrees(270), false);
        pilot.travel(side, false);
        pilot.rotateTo(Azimuth.valueOfDegrees(180), false);
        pilot.travel(side, false);
        pilot.rotateTo(Azimuth.valueOfDegrees(90), false);
        pilot.travel(side, false);
        pilot.rotateTo(Azimuth.valueOfDegrees(0), false);
        pilot.travel(side, false);
        pilot.rotateTo(Azimuth.valueOfDegrees(270), false);
        pilot.travel(side, false);
        pilot.rotateTo(Azimuth.valueOfDegrees(0), false);
        pilot.travel(side, false);
        pilot.rotateTo(Azimuth.valueOfDegrees(90), false);
        pilot.travel(side, false);
        pilot.rotateTo(Azimuth.valueOfDegrees(180), false);
    }
}
