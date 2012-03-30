package robotour.arduino;

import robotour.gui.map.LocalPath;
import robotour.navi.basic.Point;
import robotour.navi.basic.Azimuth;
import robotour.navi.basic.Pose;

/**
 * Uses compass (Azimuzh) and Encoder to evaluate position
 * @author Kotuc
 */
public class ArdOdometry extends Pose {

    public ArdOdometry() {
        super(new Point(0, 0), Azimuth.NORTH);
    }


//    Pose pose = new Pose(new Point(0, 0), Azimuth.NORTH);
    //private Azimuth azimuth = Azimuth.NORTH;
    //private Point point = new Point(0, 0);

    private double metresPerTick = 1.0/92; // cca 1 cm
    LocalPath track = new LocalPath();

    public LocalPath getTrack() {
        return track;
    }    

    public void setTo(Point point) {
        this.point = point;
    }

//    public Point getPoint() {
//        return this.point;
//    }

    public void updatedAzimuth(Azimuth newAzimuth) {
        this.azimuth = newAzimuth;
    }

    public void updatedOdometer(int change) {
        if (change != 0) {

//            double dx = speed * azimuth.sin() * ms / 1000.0;
//            double dy = speed * azimuth.cos() * ms / 1000.0;
//            point = new Point(point.getX() + dx, point.getY() + dy);

            double dist = metresPerTick * change;

            point = point.move(azimuth, dist);

            track.append(point);
        }
    }
}
