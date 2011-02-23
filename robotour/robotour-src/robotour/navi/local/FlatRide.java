package robotour.navi.local;

import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.iface.Compass;
import robotour.iface.MeasureException;
import robotour.navi.gps.Azimuth;
import robotour.util.RobotSystems;

/**
 *
 * @author Tomas
 */
public class FlatRide {

    public static void main(String[] args) throws InterruptedException, MeasureException {
        RobotSystems systems = RobotSystems.getDefault();
        Compass compass = systems.getCompass();
        Pilot pilot = new Pilot(systems.getWheels(), compass);
        DistanceHolder disth = new DistanceHolder(systems.getCenterSonar(), systems.getWheels());

        System.out.println(compass.getAzimuth());

        final int offset = 22;

        //pilot.rotateTo(Azimuth.valueOfDegrees(-90), false);
        pilot.rotateToAndCalib(Azimuth.valueOfDegrees(270 + offset));

        bump(disth);

        pilot.rotateToAndCalib(Azimuth.valueOfDegrees(174));
        pilot.travel(1.5, false);
        pilot.rotateToAndCalib(Azimuth.valueOfDegrees(270 + offset));
        //pilot.rotateTo(Azimuth.valueOfDegrees(-90), false);

        bump(disth);

        pilot.rotateToAndCalib(Azimuth.valueOfDegrees(0 + offset));

        bump(disth);

        pilot.rotateToAndCalib(Azimuth.valueOfDegrees(79));

        bump(disth);

        pilot.rotateToAndCalib(Azimuth.valueOfDegrees(270 + offset));

        pilot.travel(1.2, false);

//        pilot.rotateToAndCalib(Azimuth.valueOfDegrees(174));
//
//        bump(disth);
//
//        pilot.rotateToAndCalib(Azimuth.valueOfDegrees(270 + offset));
//
//        pilot.travel(1.2, false);
//
//        pilot.rotateToAndCalib(Azimuth.valueOfDegrees(90 + offset));

        //
//        pilot.rotateTo(Azimuth.valueOfDegrees(90), false);
//        pilot.travel(2.0, false);
//        pilot.rotateTo(Azimuth.valueOfDegrees(0), false);
//        pilot.travel(1.0, false);
        System.exit(0);
    }

    private static void bump(DistanceHolder disth) throws InterruptedException {
        disth.hold(0.15, true);
        Thread.sleep(5000);
        disth.stop();
    }
}
