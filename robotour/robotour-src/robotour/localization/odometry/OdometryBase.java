package robotour.localization.odometry;

import robotour.gui.map.LocalPoint;
import robotour.navi.basic.Azimuth;
import robotour.navi.basic.RobotPose;

/**
 *
 * @author Kotuc
 */
public class OdometryBase {

    final protected RobotPose pose = new RobotPose(new LocalPoint(1, 1), Azimuth.NORTH);

    public OdometryBase() {
    }
    
    public RobotPose getPose() {
        return pose;
    }    

    public Azimuth getAzimuth() {
        return pose.getAzimuth();
    }

    public LocalPoint getPoint() {
        return pose.getPoint();
    }

    public void setPoint(LocalPoint point) {
        pose.setPoint(point);
    }

}
