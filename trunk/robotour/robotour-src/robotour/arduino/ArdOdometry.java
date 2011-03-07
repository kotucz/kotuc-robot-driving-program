package robotour.arduino;

import robotour.gui.map.LocalPath;
import robotour.gui.map.LocalPoint;
import robotour.navi.basic.Azimuth;
import robotour.navi.basic.RobotPose;

/**
 * Uses compass (Azimuzh) and Encoder to evaluate position
 * @author Kotuc
 */
public class ArdOdometry extends RobotPose {

    public ArdOdometry() {
        super(new LocalPoint(0, 0), Azimuth.NORTH);
    }


//    RobotPose pose = new RobotPose(new LocalPoint(0, 0), Azimuth.NORTH);
    //private Azimuth azimuth = Azimuth.NORTH;
    //private LocalPoint point = new LocalPoint(0, 0);

    private double metresPerTick = 1.0/92; // cca 1 cm
    LocalPath track = new LocalPath();

    public LocalPath getTrack() {
        return track;
    }    

    public void setTo(LocalPoint point) {
        this.point = point;
    }

//    public LocalPoint getPoint() {
//        return this.point;
//    }

    public void updatedAzimuth(Azimuth newAzimuth) {
        this.azimuth = newAzimuth;
    }

    public void updatedOdometer(int change) {
        if (change != 0) {

//            double dx = speed * azimuth.sin() * ms / 1000.0;
//            double dy = speed * azimuth.cos() * ms / 1000.0;
//            point = new LocalPoint(point.getX() + dx, point.getY() + dy);

            double dist = metresPerTick * change;

            point = point.move(azimuth, dist);

            track.append(point);
        }
    }
}
