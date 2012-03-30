package experimental.slam;

import java.util.Collection;
import javax.vecmath.Vector2d;
import robotour.navi.basic.Point;
import robotour.navi.basic.Azimuth;

/**
 *
 * @author Kotuc
 */
public class LineSegment {

    /**
     *  //line shortest distance angle counterclockwise from x+ to y+
     *  line shortest distance azimuth
     */
    Azimuth phi;
    /**
     * shortest distance to line from origin (0, 0). always non negative
     */
    double rho;
    double start;
    double end;

    public LineSegment(Azimuth phi, double rho, double start, double end) {
        this.phi = phi;
        this.rho = rho;
        this.start = start;
        this.end = end;
    }

    /**
     * 
     * @param p
     * @return
     * @see http://mathworld.wolfram.com/Point-LineDistance2-Dimensional.html
     */
    double distanceTo(Point p) {
        Vector2d normal = getNormal();
        Vector2d pvec = new Vector2d(p.toPoint2d());

        // shortest distance in phi direction - projection on normal
        double d = normal.dot(pvec);

        return Math.abs(d - rho);
    }

    private Vector2d getNormal() {
        Vector2d normal = new Vector2d(new Point(0, 0).move(phi, 1).toPoint2d());
        return normal;
    }

    private Vector2d getParallel() {
        Vector2d parallel = new Vector2d(new Point(0, 0).move(Azimuth.valueOfRadians(phi.radians() + Math.PI / 2), 1).toPoint2d());
        return parallel;
    }

    double totalLeastSquaresSum(Collection<Point> points) {
        double sum = 0;
        for (Point point : points) {
            sum += Math.pow(this.distanceTo(point), 2);
        }
        return sum;
    }

    void totalLeastSquaresOptimize(Collection<Point> points) {

        for (int i = 0; i < 20; i++) {

            final double dp = 0.00001;
            final double dr = 0.00001;

            Azimuth a0 = phi;

            phi = Azimuth.valueOfRadians(a0.radians() + dp);
            double fp1 = this.totalLeastSquaresSum(points);

            phi = Azimuth.valueOfRadians(a0.radians() - dp);
            double fp2 = this.totalLeastSquaresSum(points);

//            double dfdp = (fp1 - fp2) / (2 * dp);
            double dfdp = (fp1 - fp2);

            phi = a0;

            double r0 = rho;

            rho = r0 + dr;
            double fr1 = this.totalLeastSquaresSum(points);

            rho = r0 - dr;
            double fr2 = this.totalLeastSquaresSum(points);

//            double dfdr = (fr1 - fr2) / (2 * dr);
            double dfdr = (fr1 - fr2);

            phi = Azimuth.valueOfRadians(a0.radians() - 0.0001 * dfdp);
            rho += -0.0001 * dfdr;
        }
    }

    public static LineSegment createSegment(Point pointa, Point pointb) {
        Vector2d pavec = new Vector2d(pointa.toPoint2d());
        Vector2d pbvec = new Vector2d(pointb.toPoint2d());

        Vector2d parallel = new Vector2d(pbvec);
        parallel.sub(pavec);
        parallel.normalize();

        Vector2d normal = new Vector2d(-parallel.y, parallel.x);
        normal.normalize();

        // shortest distance in phi direction - projection on normal
        double d = normal.dot(pavec);

        Azimuth phi = new Point(0, 0).getAzimuthTo(new Point(normal.x, normal.y));

        double start = parallel.dot(pavec);
        double end = parallel.dot(pbvec);

        if (d < 0) {
            return new LineSegment(Azimuth.valueOfRadians(phi.radians() + Math.PI), -d, -start, -end);
        } else {
            return new LineSegment(phi, d, start, end);
        }

    }

    Point getPoint(double t) {
        Vector2d normal = getNormal();

        Vector2d parallel = getParallel();

        normal.scale(rho);
        parallel.scale(t);

        Vector2d point = new Vector2d();
        point.add(normal, parallel);

        return new Point(point.x, point.y);
    }

    Point getEndPoint() {
        return getPoint(end);
    }

    Point getStartPoint() {
        return getPoint(start);
    }
}
