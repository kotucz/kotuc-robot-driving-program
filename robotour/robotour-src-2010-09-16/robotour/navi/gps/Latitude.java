package robotour.navi.gps;

/**
 *
 * @author Tomas
 */
public class Latitude extends Angle {

    private static final long serialVersionUID = 19L;

    private Latitude(double radians) {
        super(radians);
    }

    private Latitude(Angle angle) {
        super(angle);
    }

    public static Latitude valueOf(Angle angle) {
        return new Latitude(angle);
    }

    public static Latitude valueOfRadians(double radians) {
        return new Latitude(radians);
    }

    public static Latitude valueOfDegrees(double degrees) {
        return new Latitude(Angle.valueOfDegrees(degrees));
    }

    @Override
    public String toString() {
        return toDegreesString() + ((radians() < 0) ? "S" : "N");
    }
}
