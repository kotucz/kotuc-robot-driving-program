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
    private final double pixw;
    private final Dimension dimension;
    private static final double xratio = 4;
    private static final double yratio = 3;
    private static final double fratio = 1.2*xratio;

    private CameraProportions() {
//        double iw = 320;
//        double ih = 240;
        double coef = 1.2; // see Tests.camCalibration
        this.dimension = new Dimension(320, 240);
//        this.dimension = new Dimension(640, 480);
        this.pixw = (dimension.width * coef);
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
        return new Point((int) (imgCenter.x - pixw * point.x / point.z), (int) (imgCenter.y + pixw * point.y / point.z));
    }

    /**
     *
     * note: vector is only direcion not real position!
     *
     * @param imgpixelx x coordinate of image pixel - may be more precise
     * @param imgpixely y coordinate of image pixel - may be more precise
     * @return
     */
    public Vector3d getProjectionVectorPixels(double imgpixelx, double imgpixely) {
        return new Vector3d(imgpixelx - imgCenter.x, -imgpixely + imgCenter.y, -pixw);
    }


    /**
     *
     * note: vector is only direcion not real position!
     *
     * @param imgpixelx x coordinate of image 0 left 1 right
     * @param imgpixely y coordinate of image 0 top 1 bottom
     * @return
     */
    public Vector3d getProjectionVectorImageCoord(double imgx, double imgy) {
        return new Vector3d(xratio*(imgx-0.5), -yratio*(imgy-0.5) , -fratio);
    }
}
