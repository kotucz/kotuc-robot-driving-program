package robotour.navi.local.beacons;

import robotour.navi.basic.RobotPose;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;
import robotour.gui.map.LocalPoint;
import robotour.navi.basic.Azimuth;

/**
 *
 * @author Kotuc
 */
public class PoseSolver implements Runnable {

    List<LocalPoint> beacons = new ArrayList<LocalPoint>();
    BeaconLocalization localization;
    RobotPose sol;

    public PoseSolver(BeaconLocalization localization, RobotPose sol) {
        this.localization = localization;
        this.sol = sol;
    }

    void iterate1(double[] angles) {
        final double d = 0.1;
        final double lam = 0.5;
        {
            RobotPose testx = new RobotPose(sol.getPoint().move(Azimuth.EAST, d), sol.getAzimuth());

            double dx = (evalFunction(angles, testx) - evalFunction(angles, sol)) / d;

            sol.setPoint(sol.getPoint().move(Azimuth.EAST, -lam * dx));
        }
        {
            RobotPose testy = new RobotPose(sol.getPoint().move(Azimuth.NORTH, d), sol.getAzimuth());

            double dy = (evalFunction(angles, testy) - evalFunction(angles, sol)) / d;

            sol.setPoint(sol.getPoint().move(Azimuth.NORTH, -lam * dy));
        }

        {
            RobotPose testa = new RobotPose(sol.getPoint(), Azimuth.valueOfRadians(sol.getAzimuth().radians() + d));

            double da = (evalFunction(angles, testa) - evalFunction(angles, sol)) / d;

            sol.setAzimuth(Azimuth.valueOfRadians(sol.getAzimuth().radians() - lam * da));
        }


    }

    void iterate2(double[] angles) {
        double d = 0.1;
        final double lam = 0.1;
        {
            final RobotPose testxp = new RobotPose(sol.getPoint().move(Azimuth.EAST, d), sol.getAzimuth());
            final RobotPose testxn = new RobotPose(sol.getPoint().move(Azimuth.EAST, -d), sol.getAzimuth());

            final double dx = 0.5 * (evalFunction(angles, testxp) - evalFunction(angles, testxn)) / d;

            sol.setPoint(sol.getPoint().move(Azimuth.EAST, -lam * dx));
        }
        {
            final RobotPose testyp = new RobotPose(sol.getPoint().move(Azimuth.NORTH, d), sol.getAzimuth());
            final RobotPose testyn = new RobotPose(sol.getPoint().move(Azimuth.NORTH, -d), sol.getAzimuth());

            final double dy = 0.5 * (evalFunction(angles, testyp) - evalFunction(angles, testyn)) / d;

            sol.setPoint(sol.getPoint().move(Azimuth.NORTH, -lam * dy));
        }

        {

            d = 0.01;

            final RobotPose testap = new RobotPose(sol.getPoint(), Azimuth.valueOfRadians(sol.getAzimuth().radians() + d));
            final RobotPose testan = new RobotPose(sol.getPoint(), Azimuth.valueOfRadians(sol.getAzimuth().radians() - d));

            final double da = 0.5 * (evalFunction(angles, testap) - evalFunction(angles, testan)) / d;

            sol.setAzimuth(Azimuth.valueOfRadians(sol.getAzimuth().radians() - lam * da));
        }


    }

    void iterate3(double[] angles) {
        double d = 0.01;
        final double lam = 0.01;

        final RobotPose testxp = new RobotPose(sol.getPoint().move(Azimuth.EAST, d), sol.getAzimuth());
        final RobotPose testxn = new RobotPose(sol.getPoint().move(Azimuth.EAST, -d), sol.getAzimuth());

        final double dx = 0.5 * (evalFunction(angles, testxp) - evalFunction(angles, testxn)) / d;

        final RobotPose testyp = new RobotPose(sol.getPoint().move(Azimuth.NORTH, d), sol.getAzimuth());
        final RobotPose testyn = new RobotPose(sol.getPoint().move(Azimuth.NORTH, -d), sol.getAzimuth());

        final double dy = 0.5 * (evalFunction(angles, testyp) - evalFunction(angles, testyn)) / d;

        final RobotPose testap = new RobotPose(sol.getPoint(), Azimuth.valueOfRadians(sol.getAzimuth().radians() + d));
        final RobotPose testan = new RobotPose(sol.getPoint(), Azimuth.valueOfRadians(sol.getAzimuth().radians() - d));

        final double da = 0.5 * (evalFunction(angles, testap) - evalFunction(angles, testan)) / d;

        sol.setPoint(new LocalPoint(sol.getPoint().getX() - lam * dx, sol.getPoint().getY() - lam * dy));
        sol.setAzimuth(Azimuth.valueOfRadians(sol.getAzimuth().radians() - lam * da));

    }

    double evalFunction(double[] angles, RobotPose test) {
        return squareSumStrict(angles, test);
//        return squareSumAng(angles, test);
    }

        /**
     * Sum of squares of beacon's distances to lines of test pose.
     * @param angles
     * @param test
     * @return
     */
    double squareSumStrict(double[] angles, RobotPose test) {

//        double[] simangs = localization.detectBeaconsSimulate(test);

        Point2d pos = test.getPoint().toPoint2d();

        double ss = 0;

        int i = 0;
        for (LocalPoint localPoint : localization.beacons) {
            Point2d beaconPoint = localPoint.toPoint2d();
            // vector from test to beacon
            Vector2d toBeacon = new Vector2d();
            toBeacon.sub(beaconPoint, pos);

            Azimuth realWorldAngle = Azimuth.valueOfRadians(test.getAzimuth().radians() - angles[i]);
            // normalized vector in direction of measured angle from robot
            Vector2d testAngle = new Vector2d(new LocalPoint(0, 0).move(realWorldAngle, 1.0).toPoint2d());

            // absolute toBeacon projection in direction of testAngle
            testAngle.scale(Math.abs(toBeacon.dot(testAngle)));

            // get peprendicular distance to line. add false direction penalty
            toBeacon.sub(testAngle);

            double distance = toBeacon.length();

            localization.solvdists[i] = distance;

            ss += Math.pow(distance, 2);

            i++;
        }

        return ss;
    }

    /**
     * Sum of squares of beacon's distances to lines of test pose.
     * @param angles
     * @param test
     * @return
     */
    double squareSum(double[] angles, RobotPose test) {

//        double[] simangs = localization.detectBeaconsSimulate(test);

        Point2d pos = test.getPoint().toPoint2d();

        double ss = 0;

        int i = 0;
        for (LocalPoint localPoint : localization.beacons) {
            Point2d beaconPoint = localPoint.toPoint2d();
            // vector from test to beacon
            Vector2d toBeacon = new Vector2d();
            toBeacon.sub(beaconPoint, pos);

            Azimuth realWorldAngle = Azimuth.valueOfRadians(test.getAzimuth().radians() - angles[i]);
            // normalized vector in direction of measured angle from robot
            Vector2d testAngle = new Vector2d(new LocalPoint(0, 0).move(realWorldAngle, 1.0).toPoint2d());

            double distance = Math.sqrt(toBeacon.lengthSquared() - Math.pow(toBeacon.dot(testAngle), 2));

            localization.solvdists[i] = distance;

            ss += Math.pow(distance, 2);

            i++;
        }

        return ss;
    }

    /**
     * Sum of squares of anles of tested pose and measured angles.
     * @param angles
     * @param test
     * @return
     */
    double squareSumAng(double[] angles, RobotPose test) {

        double[] simangs = localization.detectBeaconsSimulate(test);

        double ss = 0;
        for (int i = 0; i < angles.length; i++) {
            ss += Math.pow(angles[i] - simangs[i], 2);
        }

        return ss;
    }

    public void run() {
        while (true) {
            iterate3(localization.detectBeacons());
//            iterate2(localization.detectBeacons());
//            iterate1(localization.detectBeacons());
            if (localization.pose.getPoint().getDistanceTo(sol.getPoint())<0.005) {
                localization.pose.setPoint(new LocalPoint(Math.random()*3-1.5, Math.random()*2.1));
                localization.pose.setAzimuth( Azimuth.valueOfRadians(Math.random()*2*Math.PI));
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(PoseSolver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
