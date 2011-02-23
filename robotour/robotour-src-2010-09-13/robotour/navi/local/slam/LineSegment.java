package robotour.navi.local.slam;

import java.util.Collection;
import javax.vecmath.Vector2d;
import robotour.gui.map.LocalPoint;
import robotour.navi.gps.Azimuth;

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
    double distanceTo(LocalPoint p) {
        Vector2d normal = getNormal();
        Vector2d pvec = new Vector2d(p.toPoint2d());

        // shortest distance in phi direction - projection on normal
        double d = normal.dot(pvec);

        return Math.abs(d - rho);
    }

    private Vector2d getNormal() {
        Vector2d normal = new Vector2d(new LocalPoint(0, 0).move(phi, 1).toPoint2d());
        return normal;
    }

    private Vector2d getParallel() {
        Vector2d parallel = new Vector2d(new LocalPoint(0, 0).move(Azimuth.valueOfRadians(phi.radians() + Math.PI / 2), 1).toPoint2d());
        return parallel;
    }

    double totalLeastSquaresSum(Collection<LocalPoint> points) {
        double sum = 0;
        for (LocalPoint localPoint : points) {
            sum += Math.pow(this.distanceTo(localPoint), 2);
        }
        return sum;
    }

    void totalLeastSquaresOptimize(Collection<LocalPoint> points) {

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

    public static LineSegment createSegment(LocalPoint pointa, LocalPoint pointb) {
        Vector2d pavec = new Vector2d(pointa.toPoint2d());
        Vector2d pbvec = new Vector2d(pointb.toPoint2d());

        Vector2d parallel = new Vector2d(pbvec);
        parallel.sub(pavec);
        parallel.normalize();

        Vector2d normal = new Vector2d(-parallel.y, parallel.x);
        normal.normalize();

        // shortest distance in phi direction - projection on normal
        double d = normal.dot(pavec);

        Azimuth phi = new LocalPoint(0, 0).getAzimuthTo(new LocalPoint(normal.x, normal.y));

        double start = parallel.dot(pavec);
        double end = parallel.dot(pbvec);

        if (d < 0) {
            return new LineSegment(Azimuth.valueOfRadians(phi.radians() + Math.PI), -d, -start, -end);
        } else {
            return new LineSegment(phi, d, start, end);
        }

    }

    LocalPoint getPoint(double t) {
        Vector2d normal = getNormal();

        Vector2d parallel = getParallel();

        normal.scale(rho);
        parallel.scale(t);

        Vector2d point = new Vector2d();
        point.add(normal, parallel);

        return new LocalPoint(point.x, point.y);
    }

    LocalPoint getEndPoint() {
        return getPoint(end);
    }

    LocalPoint getStartPoint() {
        return getPoint(start);
    }
}
