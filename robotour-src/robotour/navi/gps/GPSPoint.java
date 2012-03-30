/*
 * GPSPoint.java
 *
 */
package robotour.navi.gps;

/**
 *
 * @author Tomas Kotula
 */
public final class GPSPoint {

    /**
     *  World coordinates angle to North
     */
    private final Latitude latitude;
    /**
     *  World coordinates angle to East
     */
    private final Longitude longitude;

    /**
     * Returns latitude Angle
     * @return latitude Angle
     */
    public Latitude latitude() {
        return latitude;
    }

    /**
     * Returns longitude Angle
     * @return longitude Angle
     */
    public Longitude longitude() {
        return longitude;
    }

    /**
     * Creates new GPSPoint at [latitude, longitude]
     * @param latitude
     * @param longitude
     */
    private GPSPoint(Latitude latitude, Longitude longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static GPSPoint at(Latitude latitude, Longitude longitude) {
        return new GPSPoint(latitude, longitude);
    }

//    public String getLatString() {
//        return latitude.toDegreesString() + ((latitude.radians() < 0) ? "S" : "N");
//    }
//
//    public String getLongString() {
//        return longitude.toDegreesString() + ((longitude.radians() < 0) ? "W" : "E");
//    }
//    /**
//     *
//     * @return length of this latitude angle to North in metres.
//     */
//    public double getLatMetres() {
//        return latitude.radians() * EARTH_RADIUS;
//    }
//
//    /**
//     *
//     * @return length of this longitude angle to East in metres.
//     */
//    public double getLongMetres() {
//        return longitude.radians() * LONG_EARTH_R;
////        return longitude.degrees() * 72602.289332233937399765867967468;
////        return longitude.radians() * EARTH_R;
//    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }


        final GPSPoint other = (GPSPoint) obj;
        if (this.latitude != other.latitude) {
            return false;
        }
        if (this.longitude != other.longitude) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.latitude != null ? this.latitude.hashCode() : 0);
        hash = 97 * hash + (this.longitude != null ? this.longitude.hashCode() : 0);
        return hash;
    }

//    double getDistanceMetresSquared(GPSPoint that) {
//        return (Math.pow(((that.latitude.radians() - this.latitude.radians())), 2) +
//                Math.pow(((that.longitude.radians() - this.longitude.radians())), 2)) *
//                Math.pow(EARTH_RADIUS, 2);
//    }
//
//    double getDistanceMetres(GPSPoint that) {
//        return EARTH_RADIUS * Math.hypot(
//                that.latitude.radians() - this.latitude.radians(),
//                that.longitude.radians() - this.longitude.radians());
//    }
//
//    GPSPoint addMetres(double latm, double longm) {
//        return new GPSPoint(
//                Latitude.valueOfRadians(this.latitude.radians() + (latm / EARTH_RADIUS)),
//                Longitude.valueOfRadians(this.longitude.radians() + (longm / LONG_EARTH_R)));
//    }

    /**
     *
     * @param that another point
     * @return azimuth to North (0 rad) according to world coordinates in radians.
     * 
     */
    public double getAzimuthTo(GPSPoint that) {

        double yup = (that.latitude.radians() - this.latitude.radians());
        double xri = (that.longitude.radians() - this.longitude.radians());

        double alpha = Math.atan2(yup, xri);

        return alpha;
    }

    public static GPSPoint fromDegrees(double latitude, double longitude) {
        return new GPSPoint(
                Latitude.valueOfDegrees(latitude),
                Longitude.valueOfDegrees(longitude));
    }

    @Override
    public String toString() {
        return "GPSPoint[" + latitude + "," + longitude + "]";
    }
}
