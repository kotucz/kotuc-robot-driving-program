package robotour.pathing.simple;

import robotour.localization.odometry.OdometryBase;

/**
 *
 * @author Kotuc
 */
public class DiffOdometry extends OdometryBase {


    final DiffWheelParameters params = new DiffWheelParameters();

    public void addEncoderDiff(int lticks, int rticks) {
        odometryRough(lticks*params.mpticks, rticks*params.mpticks);
    }

    /**
     * Rough approximation of arc motion.
     * Works fine for small amount of rotation or distance.
     * @param ldist
     * @param rdist
     */
    void odometryRough(double ldist, double rdist) {

        double rotanglecw = ((ldist - rdist) / (params.fullRevolution));

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
        double rotanglecw = ((ldist - rdist) / (params.fullRevolution));

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


//    void shift(double fwx, double fxy, double radsccw) {
//        // TODO implement
//        // needed??
//    }
}
