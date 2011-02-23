package vision;

import java.awt.Dimension;
import java.awt.Point;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 *
 * Image coordinates are represented as Point instances.
 * [0, 0] upper left corner x right, y down
 *
 * Camera space has [0, 0, 0] in the focus of the image.
 * Coordinates x and y are in the center of the image, which is w pixels from the focus.
 * X is rogth, Y is up and Z is toward the viewer
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
        this.dimension = new Dimension(320, 240);
//        this.dimension = new Dimension(640, 480);
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
//        if (-point.z < 0.01) {
//            System.err.println("negative z!");
//            point.z*=-1;
//        }
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
