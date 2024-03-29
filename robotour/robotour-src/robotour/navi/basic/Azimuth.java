package robotour.navi.basic;

/**
 *
 * @author Tomas
 * NOTE Azimuth is defined with respect to geographic coordinates. Local XY coorinates does not have to be aligned,
 * although it was originally intendet to do so.
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

    Heading toHeading() {
        return Heading.fromRadians(Math.PI/2.-radians());
    }

    static Azimuth fromHeading(Heading heading) {
        return Azimuth.valueOfRadians(Math.PI/2.-heading.radians());
    }

}
