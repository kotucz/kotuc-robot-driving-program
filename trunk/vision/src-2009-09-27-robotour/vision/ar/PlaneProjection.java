package vision.ar;

import java.awt.Point;
import javax.media.j3d.Transform3D;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import vision.CameraProportions;

/**
 * Can convert between camera coordinates and coordinates on plane given a transform
 * between them.
 *
 * Camera and plane transform are mutually inverted.
 *
 * @author Kotuc
 */
public class PlaneProjection {

    private CameraProportions proportions = CameraProportions.getCameraProportions();
    private Transform3D planeTransform = new Transform3D();
    private Transform3D cameraTransform = new Transform3D();

    public void setProportions(CameraProportions proportions) {
        this.proportions = proportions;
    }

    public CameraProportions getProportions() {
        return proportions;
    }

    /**
     *
     * Plane stays on coordinates (0,0,0) with (0,0,1) normal vector, (0, 1, 0) up.
     * Camera is initialy on (0, 0, 0) with (0, 0, -1) normal and (0, 1, 0) up.
     * Camera position ad desired vector derived from proportions and image point
     * are transformed via transform. The pixel vector intersection is calculated
     * and returned.
     *
     * @param cameraPoint
     * @return Position of projection cameraPoint pixel onto plane with given transform.
     */
    public Point3d getPositionOnPlane(Point imagePoint) {

        Point3d camPoint = new Point3d();
        Vector3d projVector = proportions.getProjectionVector(imagePoint.x, imagePoint.y);

        cameraTransform.transform(camPoint);
        cameraTransform.transform(projVector);

        // vector intersection parameter parameter
        // ray = point + t*vector
        double t = -camPoint.z / projVector.z;

//        System.out.println("t: " + t);

        // ray.z==0 -> projectionPoint
        projVector.scale(t);
        camPoint.add(projVector);

        return camPoint;
    }

    /**
     *
     * @param planePoint
     * @return imageCoodrinates of point in plane space with respect to transform.
     */
    public Point getPositionOnImage(Point3d planePoint) {

        // get Vector

        planeTransform.transform(planePoint);

        if (planePoint.z < 0) {
            return proportions.getProjectionImagePoint(planePoint);
        } else {
            // behind
            return new Point(0, 0);//;null;
        }

    }

    void toCameraSpace(Point3d point) {
        planeTransform.transform(point);
    }

    public void setPlaneTransform(Transform3D planeTransform) {
        this.planeTransform.set(planeTransform);
        this.cameraTransform.invert(planeTransform);
    }

    public void setCameraTransform(Transform3D cameraTransform) {
        this.cameraTransform.set(cameraTransform);
        this.planeTransform.invert(cameraTransform);
    }

    /**
     * 
     * @param cameraPos position from plane origin
     * @param pan horisontal angle up
     * @param tilt vertical angle up
     */
    public void setCamera(Point3d cameraPos, double pan, double tilt) {
        Transform3D camTrans = new Transform3D();
        camTrans.setTranslation(new Vector3d(cameraPos));

        Transform3D camRot = new Transform3D();
        camRot.rotZ(-pan); // rads "right"
        camTrans.mul(camRot);

        camRot = new Transform3D();
        camRot.rotX(tilt); // rads "up"
        camTrans.mul(camRot);

//        System.out.println("Cam trans " + camTrans);

//        this.planeTransform.invert(camTrans);
        setCameraTransform(camTrans);
    }

    public static void main(String[] args) {
        PlaneProjection pp = new PlaneProjection();
        pp.setCamera(new Point3d(0, 0, 1), 0, 1);
        System.out.println("image [0, 0] on plane: " + pp.getPositionOnPlane(new Point(320, 240)));
        System.out.println("back on image: " + pp.getPositionOnImage(new Point3d(0.0, 1.5574077246549018, 0.0)));
    }
}
