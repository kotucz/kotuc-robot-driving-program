package robotour.navi.gps;

/**
 *
 * @author Tomas
 */
public class Longitude extends Angle {

    private static final long serialVersionUID = 17L;

    private Longitude(double radians) {
        super(radians);
    }

    private Longitude(Angle angle) {
        super(angle);
    }

    public static Longitude valueOf(Angle angle) {
        return new Longitude(angle);
    }

    public static Longitude valueOfRadians(double radians) {
        return new Longitude(radians);
    }

    public static Longitude valueOfDegrees(double degrees) {
        return new Longitude(Angle.valueOfDegrees(degrees));
    }

    @Override
    public String toString() {
        return toDegreesString() + ((radians() < 0) ? "W" : "E");
    }
}
