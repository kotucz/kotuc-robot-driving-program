package vision.pathfinding.birdseye;

import java.awt.image.BufferedImage;
import javax.vecmath.Point3d;
import vision.ar.PlaneProjection;
import vision.pathfinding.knowledge.DoubleMatrix;

/**
 *
 * @author Kotuc
 */
public class BirdsEye {

    RealFloor floor = new RealFloor();
    // cam properties
    final double camHeightm = 0.7;
    final double camTiltRads = Math.toRadians(70);
//    final double camTiltRads = Math.toRadians(90);
    PlaneProjection projection = new PlaneProjection();
    double cutoffm;

    public RealFloor getFloor() {
        return floor;
    }

    public void setCameraPosition(double x, double y, double azimrads) {
        projection.setCamera(new Point3d(x, y, camHeightm), azimrads, camTiltRads);
//        floor.set(x, y, 0xFFFF0000);
    }

    public void paintProjectedCameraImage(BufferedImage image) {

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Point3d planepoint = projection.getPositionOnPlanePercents(((double) x) / image.getWidth(), ((double) y) / image.getHeight());
//                System.out.println(""+planepoint);
                if (planepoint != null) {
//                    floor.setRGB(planepoint.x, planepoint.y, image.getRGB(x, y));
                }
            }
        }

//        floor.set(planepoint.x, planepoint.y, image.getRGB(x, y));

    }

    public BufferedImage getFloorImage() {
        return floor.getImage();
    }

    public void paintProjectedCameraMatrix(DoubleMatrix matrix) {

        for (int y = 10; y < matrix.getHeight(); y++) {
            for (int x = 0; x < matrix.getWidth(); x++) {
                Point3d planepoint = projection.getPositionOnPlanePercents(((double) x) / matrix.getWidth(), ((double) y) / matrix.getHeight());
//                System.out.println(""+planepoint);
                if (planepoint != null) {
                    floor.set(planepoint.x, planepoint.y, matrix.get(x, y));
                }
            }
        }

//        floor.set(planepoint.x, planepoint.y, image.getRGB(x, y));

    }

    
}
