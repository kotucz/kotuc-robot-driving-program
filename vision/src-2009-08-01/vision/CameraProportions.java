package vision;

import java.awt.Dimension;
import java.awt.Point;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 *
 * @author Kotuc
 */
public class CameraProportions {

    private final Point imgCenter;
    private final double w;
    private final Dimension dimension;

    private CameraProportions() {
//        double iw = 320;
//        double ih = 240;
        double coef = 1.2; // see Tests.camCalibration
//        this.dimension = new Dimension(320, 240);
        this.dimension = new Dimension(640, 480);
        this.w = (dimension.width * coef);
        this.imgCenter = new Point(dimension.width / 2, dimension.height / 2);
    }
    private static CameraProportions INSTANCE = new CameraProportions();

    public static CameraProportions getCameraProportions() {
        return INSTANCE;
    }

    /**
     *
     * @param point coodrinates in camera space
     * @return image coordinates of point
     */
    public Point getProjectionImagePoint(Point3d point) {
        return new Point((int) (imgCenter.x - w * point.x / point.z), (int) (imgCenter.y + w * point.y / point.z));
    }

    /**
     *
     * note: vector is only direcion not real position!
     *
     * @param imgx x coordinate of image pixel - may be more precise
     * @param imgy y coordinate of image pixel - may be more precise
     * @return
     */
    public Vector3d getProjectionVector(double imgx, double imgy) {
        return new Vector3d(imgx - imgCenter.x, -imgy + imgCenter.y, -w);
    }
}
