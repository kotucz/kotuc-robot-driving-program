package robotour.navi.gps;

/**
 *
 * @author Kotuc
 */
public class TrackPoint {

    private GPSPoint point;
    private double altitude;
    private long time;
    String name = "";

    public TrackPoint() {
    }

    public TrackPoint(GPSPoint point) {
        this.point = point;
    }

    public String getName() {
        return name;
    }

    public long getTime() {
        return time;
    }

    public GPSPoint getPoint() {
        return point;
    }

    public double getAltitude() {
        return altitude;
    }

    @Override
    public String toString() {
        return name + " " + super.toString();
    }
}