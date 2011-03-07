package robotour.navi.basic;

/**
 *
 * @author Tomas
 */
public class Azimuth extends Angle {

    public static final Azimuth NORTH = new Azimuth(0);
    public static final Azimuth EAST = new Azimuth(Math.PI/2);
    public static final Azimuth SOUTH = new Azimuth(Math.PI);
    public static final Azimuth WEST = new Azimuth(3*Math.PI/4);

    private static final long serialVersionUID = 21L;

    private Azimuth(double radians) {
        super(radians);
    }

    private Azimuth(Angle angle) {
        super(angle);
    }

    public static Azimuth valueOf(Angle angle) {
        return new Azimuth(angle);
    }

    public static Azimuth valueOfRadians(double radians) {
        return new Azimuth(radians);
    }

    public static Azimuth valueOfDegrees(double degrees) {
        return new Azimuth(Angle.valueOfDegrees(degrees));
    }

    @Override
    public String toString() {
        return Math.round(degrees()) + "°";
    }
}
