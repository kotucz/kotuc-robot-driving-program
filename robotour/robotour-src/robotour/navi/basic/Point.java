package robotour.navi.basic;

import java.awt.geom.Point2D;
import javax.vecmath.Point2d;

/**
 * @author Tomas
 */
public class Point {

    /**
     * metres to east
     */
    private final double x;
    /**
     * metres to north
     */
    private final double y;

    public Point(double x, double y) {
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
        final Point other = (Point) obj;
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

    //    public static Point valueOf(GPSPoint gps) {
//        return new Point(gps.getLongMetres(), gps.getLatMetres());
//    }

    /**
     *
     * @param azimuth
     * @param distance
     * @return
     */
    public Point move(Azimuth azimuth, double distance) {
        double dx = distance * azimuth.sin();
        double dy = distance * azimuth.cos();
        return new Point(this.x + dx, this.y + dy);
    }

    /**
     * TODO use carefully
     * @param point
     * @return
     * @deprecated
     */
    public Azimuth getAzimuthTo(Point point) {
        return Azimuth.valueOfRadians(Math.PI / 2 - Math.atan2(point.y - this.y, point.x - this.x));
//        return Azimuth.valueOfRadians(Math.atan2(-(point.x-this.x), point.y-this.y));
    }

    public Heading getHeadingTo(Point point) {
        return Heading.fromRadians(Math.atan2(point.y - this.y, point.x - this.x));
    }



    public double getDistanceTo(Point point) {
        return Math.hypot(point.x - this.x, point.y - this.y);
    }

    public Point2d toPoint2d() {
        return new Point2d(this.x, this.y);
    }

    public Point2D toAwtPoint() {
        return new Point2D.Double(this.x, this.y);
    }

    public Point add(Point that) {
        return new Point(this.x + that.x, this.y + that.y);
    }

    public Point add(Vector vec) {
        return new Point(this.x + vec.x, this.y + vec.y);
    }

    public static Point fromPoint2D(Point2D p) {
        return new Point(p.getX(), p.getY());
    }

    public Vector vectorTo(Point other) {
        return new Vector(other.x - this.x, other.y - this.y);
    }



}
