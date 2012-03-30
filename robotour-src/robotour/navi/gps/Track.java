package robotour.navi.gps;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author PC
 */
public class Track  {

    private final List<TrackPoint> points = new LinkedList<TrackPoint>();
    private String name = "trackName";
        
    public List<TrackPoint> track() {
        return points;
    }

    /** Creates a new instance of Track */
    public Track() {
    }

    public Track(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void append(TrackPoint point) {
//        track.add(point);
        points.add(point);

    // TODO resize bounds

//        topLeft.set(Math.max(topLeft.latitude, point.latitude),
//                    Math.min(bottomRight.latitude, point.latitude));
//        
//        topLeft.set(Math.min(topLeft.longitude, point.longitude),
//                    Math.max(bottomRight.longitude, point.longitude));
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName() + " " + points.size() + " pts";
    }

//    TrackPoint getFirst() {
//        return track.getFirst();
//    }
    /**
     *  returns the nearest trackpoint to wanted position e.g. gps or mouse click..
     * @param to
     * @return
     */
//    public TrackPoint getNearest(GPSPoint to) {
//        TrackPoint near = null;
//        double dist = Double.MAX_VALUE;
//        for (TrackPoint trkpt : points) {
//            double d = to.getDistanceMetresSquared(trkpt.getPoint());
//            if (d < dist) {
//                near = trkpt;
//                dist = d;
//            }
//        }
//        return near;
//    }
}
