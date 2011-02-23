package robotour.navi.local.beacons;

import robotour.gui.map.LocalPoint;
import robotour.navi.gps.Azimuth;

/**
 *
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

    public void setTo(RobotPose pose) {
        this.setPoint(pose.getPoint());
        this.setAzimuth(pose.getAzimuth());
    }
    

}
