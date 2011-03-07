package robotour.navi.local.odometry;

/**
 *
 * @author Kotuc
 */
public class DiffOdometry extends OdometryBase {

    // distance between left and right wheel
    private static final double WHEEL_GAUGE = 0.3;

    private static final double tickspm = 10000;
    private static final double mpticks = 1.0/tickspm;

    public void addEncoderDiff(int lticks, int rticks) {
        odometry1(lticks*mpticks, rticks*mpticks);
    }

    void odometry1(double ldist, double rdist) {

        double rotanglecw = ((ldist - rdist) / (WHEEL_GAUGE * Math.PI));

        double dist = (ldist + rdist) / 2.0;

        pose.doMoveFwRight(dist, rotanglecw);

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
        double rotanglecw = ((ldist - rdist) / (WHEEL_GAUGE * Math.PI));

        double dist1 = (ldist + rdist) / 2.0f;
        double dist = 0;

        
        //            if (rotangleccw < Math.PI / 2.0) {
        //                dist = dist1 * Math.cos(rotangleccw / 2.0); // around 0
        //            }
        // exceotuibs at low rotation
        dist = 2.0 * dist1 * Math.sin(rotanglecw / 2.0) / rotanglecw; // 0 <<

        pose.doMoveFwRight(dist, rotanglecw);

//        double fwx = Math.cos(0.5 * rotangleccw + 0.5 * Math.PI) * dist;
//        double fwy = Math.sin(0.5 * rotangleccw + 0.5 * Math.PI) * dist;

//        fw.normalize();
//        moveRelFw(getWorldVector(fw.mul((float) dist)));
//        rotateCCW(rotangleccw);
//        shift(fwx, fwy, rotangleccw);
    }

    void shift(double fwx, double fxy, double radsccw) {
        // TODO implement
    }
}
