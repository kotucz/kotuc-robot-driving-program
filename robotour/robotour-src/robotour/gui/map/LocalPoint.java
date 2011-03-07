package robotour.gui.map;

import java.awt.geom.Point2D;
import javax.vecmath.Point2d;
import robotour.navi.basic.Azimuth;

/**
 *
 * @author Tomas
 */
public class LocalPoint {

    /**
     * metres to east
     */
    private final double x;
    /**
     * metres to north
     */
    private final double y;

    public LocalPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LocalPoint other = (LocalPoint) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 53 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        return hash;
    }

    @Override
    public String toString() {
        return "LP(" + x + "," + y + ")";
    }

//    public static LocalPoint valueOf(GPSPoint gps) {
//        return new LocalPoint(gps.getLongMetres(), gps.getLatMetres());
//    }

    public LocalPoint move(Azimuth azimuth, double distance) {
        double dx = distance * azimuth.sin();
        double dy = distance * azimuth.cos();
        return new LocalPoint(this.x + dx, this.y + dy);
    }

    public Azimuth getAzimuthTo(LocalPoint point) {
        return Azimuth.valueOfRadians(Math.PI/2-Math.atan2(point.y-this.y, point.x-this.x));

//        return Azimuth.valueOfRadians(Math.atan2(-(point.x-this.x), point.y-this.y));
    }

    public double getDistanceTo(LocalPoint point) {
        return Math.hypot(point.x-this.x, point.y-this.y);
    }

    public Point2d toPoint2d() {
        return new Point2d(this.x, this.y);
    }

    public Point2D toAwtPoint() {
        return new Point2D.Double(this.x, this.y);
    }

}
