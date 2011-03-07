package robotour.navi.basic;

import robotour.gui.map.LocalPoint;
import robotour.navi.basic.Azimuth;

/**
 * Is mutable.
 * @author Kotuc
 */
public class RobotPose {

    protected LocalPoint point;
    protected Azimuth azimuth;

    public RobotPose(RobotPose pose) {
        this.setTo(pose);
    }

    public RobotPose(LocalPoint point, Azimuth azimuth) {
        this.point = point;
        this.azimuth = azimuth;
    }

    public void setAzimuth(Azimuth azimuth) {
        this.azimuth = azimuth;
    }

    public void setPoint(LocalPoint point) {
        this.point = point;
    }

    public Azimuth getAzimuth() {
        return azimuth;
    }

    public LocalPoint getPoint() {
        return point;
    }

    public final void setTo(RobotPose pose) {
        this.setPoint(pose.getPoint());
        this.setAzimuth(pose.getAzimuth());
    }

    /**
     * Moves along arc (part of circle) specified by the angle and cutline (tetiva) distance.
     * @param dist
     * @param radscw
     */
    public void doMoveFwRight(double dist, double radscw) {
        this.point = point.move(Azimuth.valueOfRadians(azimuth.radians() + 0.5 * radscw), dist);
        this.azimuth = Azimuth.valueOfRadians(azimuth.radians() + radscw);
        //            Vec2 fw = new Vec2((float) Math.cos(0.5 * rotangleccw + Math.PI / 2), (float) Math.sin(rotangleccw + Math.PI / 2));
        //        fw.normalize();
        //        moveRelFw(getWorldVector(fw.mul((float) )));
        //        rotateCCW(rotangleccw);
        //        shift(fwx, fwy, rotangleccw);
    }

    public void move(Azimuth azimuth, double dist) {
        point.move(azimuth, dist);
    }       

}
