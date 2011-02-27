package eurobot.kuba;

import robotour.gui.map.LocalPoint;
import robotour.navi.gps.Azimuth;
import robotour.navi.local.beacons.RobotPose;

/**
 *
 * @author Kotuc
 */
public class DiffOdometry {

    // distance between left and right wheel
    private static final double WHEEL_GAUGE = 0.3;
    final RobotPose pose = new RobotPose(new LocalPoint(1, 1), Azimuth.NORTH);

    private static final double tickspm = 10000;
    private static final double mpticks = 1.0/tickspm;

    void encoderDiff(int lticks, int rticks) {
        odometry1(lticks*mpticks, rticks*mpticks);
    }

    void odometry1(double ldist, double rdist) {

        double rotangleccw = (float) ((rdist - ldist) / (WHEEL_GAUGE * Math.PI));

        double dist = (ldist + rdist) / 2.0;

        double fwx = Math.cos(0.5 * rotangleccw + 0.5 * Math.PI) * dist;
        double fwy = Math.sin(0.5 * rotangleccw + 0.5 * Math.PI) * dist;
//            Vec2 fw = new Vec2((float) Math.cos(0.5 * rotangleccw + Math.PI / 2), (float) Math.sin(rotangleccw + Math.PI / 2));
//        fw.normalize();

//        moveRelFw(getWorldVector(fw.mul((float) )));
//        rotateCCW(rotangleccw);

        shift(fwx, fwy, rotangleccw);

//            XForm movexf = new XForm();
//
//            movexf.R.setAngle(rotangleccw);
//	    movexf.position.set(getWorldVector(fw.mul((float)(ldist+rdist)/2.0f)));
//
////            xform.position.set(xform.position.add(movexf.position));
////            xform.position.set(XForm.mul(xform, fw));
//            xform.position = xform.position.add(movexf.position);
//            xform.R.set(xform.R.mul(movexf.R));
////            xform.R.set(movexf.R.mul(xform.R));

    }

    void odometry2(double ldist, double rdist) {
        double rotangleccw = ((rdist - ldist) / (WHEEL_GAUGE * Math.PI));

        double dist1 = (ldist + rdist) / 2.0f;
        double dist = 0;
        //            if (rotangleccw < Math.PI / 2.0) {
        //                dist = dist1 * Math.cos(rotangleccw / 2.0); // around 0
        //            }
        dist = 2.0 * dist1 * Math.sin(rotangleccw / 2.0) / rotangleccw; // 0 <<

        double fwx = Math.cos(0.5 * rotangleccw + 0.5 * Math.PI) * dist;
        double fwy = Math.sin(0.5 * rotangleccw + 0.5 * Math.PI) * dist;

//        fw.normalize();
//        moveRelFw(getWorldVector(fw.mul((float) dist)));
//        rotateCCW(rotangleccw);
        shift(fwx, fwy, rotangleccw);
    }

    /**
     * Moves relative forward
     */
//    void moveRelFw(double fwx, double fwy) {
//        xform.position = xform.position.add(fw);
//    }
//
//    void rotateCCW(float angle) {
//        Mat22 R = new Mat22(angle);
//        xform.R.set(xform.R.mul(R));
//    }
//
//    Vec2 getWorldVector(Vec2 localVector) {
//        return Mat22.mul(xform.R, localVector);
//    }
    void shift(double fwx, double fxy, double radsccw) {
        // TODO implement
    }
}
