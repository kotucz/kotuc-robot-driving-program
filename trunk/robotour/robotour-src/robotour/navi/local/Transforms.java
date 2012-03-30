package robotour.navi.local;

import robotour.navi.basic.Point;
import robotour.navi.basic.Pose;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

/**
 * Created by IntelliJ IDEA.
 * User: Kotuc
 * Date: 27.3.12
 * Time: 19:10
 * To change this template use File | Settings | File Templates.
 */
public class Transforms {

    public static AffineTransform getLocalToGlobalAffineTransform(Pose pose) {
        AffineTransform transform = new AffineTransform();
        transform.translate(pose.getPoint().getX(), pose.getPoint().getY());
        transform.rotate(pose.getHeading().radians());
        return transform;
    }

    public static Point globalToLocal(final Pose pose, final Point globalPoint) {
        AffineTransform transform = getLocalToGlobalAffineTransform(pose);
        try {
            transform.invert();
        } catch (NoninvertibleTransformException e) {
            // should always be inbertible
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        Point2D transformed = transform.transform(globalPoint.toAwtPoint(), null);
        return new Point(transformed.getX(), transformed.getY());
    }

}
