package robotour.localization.odometry;

import robotour.navi.basic.Point;
import robotour.navi.basic.Azimuth;
import robotour.navi.basic.Pose;

/**
 *
 * @author Kotuc
 */
public class OdometryBase {

    final protected Pose pose = new Pose(new Point(1, 1), Azimuth.NORTH);

    public OdometryBase() {
    }
    
    public Pose getPose() {
        return pose;
    }    

    public Azimuth getAzimuth() {
        return pose.getAzimuth();
    }

    public Point getPoint() {
        return pose.getPoint();
    }

    public void setPoint(Point point) {
        pose.setPoint(point);
    }

}
