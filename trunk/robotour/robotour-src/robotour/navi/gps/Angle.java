package robotour.navi.gps;

import java.io.Serializable;

/**
 * Measure of angle in degrees. For precise gps computing
 * @author Tomas
 */
public class Angle implements Serializable {

    private static final long serialVersionUID = 15L;
    private final double radians;

    protected Angle(double radians) {
        this.radians = radians;
    }

    /**
     * Using this constructor for retyping its subclasses
     * @param angle
     */
    protected Angle(Angle angle) {
        this.radians = angle.radians;
    }

    /**
     * Format: 49o55'30.23"N,18o2'30.05"E
     * @param lat
     * @return
     */
    public static Angle parseDMS(String lat) {

        String[] nums = lat.split("\\D");

        double degrees = Double.parseDouble(nums[0]) +
                Double.parseDouble(nums[1]) / 60.0 +
                Double.parseDouble(nums[2] + "." + nums[3]) / 3600.0;

        return new Angle(Math.toRadians(degrees));

    }

    public double degrees() {
        return Math.toDegrees(radians);
    }

    public double radians() {
        return radians;
    }

    public double sin() {
        return Math.sin(radians);
    }

    public double cos() {
        return Math.cos(radians);
    }

    public Angle add(Angle angle) {
        return new Angle(this.radians + angle.radians);
    }

    public Angle inverse() {
        return new Angle(-radians);
    }

    public Angle sub(Angle angle) {
        return new Angle(this.radians - angle.radians);
    }

    public static Angle valueOfDegrees(double degrees) {
        return new Angle(Math.toRadians(degrees));
    }

//    static Angle valueOf(String string) {
//        return null;
//    }
    public String toDegreesString() {
        double l = Math.abs(Math.toDegrees(this.radians));
        int deg = (int) l;
        l -= deg;
        int min = (int) (l *= 60);
        l -= min;
        int sec = (int) (l *= 60);
        l -= sec;
        int rem = (int) (l *= 1000);
        return deg + "°" + min + "'" + sec + "." + rem + "\"";
    }

    @Override
    public String toString() {
        return "Angle(" + toDegreesString() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Angle other = (Angle) obj;
        if (this.radians != other.radians) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + (int) (Double.doubleToLongBits(this.radians) ^ (Double.doubleToLongBits(this.radians) >>> 32));
        return hash;
    }

    /**
     *
     * @return angle in [-180, 180]
     */
    public Angle shorter() {
        double rads = radians;
        while (rads > Math.PI) {
            rads -= 2 * Math.PI;
        }
        while (rads < -Math.PI) {
            rads += 2 * Math.PI;
        }
        return new Angle(rads);
    }
}
