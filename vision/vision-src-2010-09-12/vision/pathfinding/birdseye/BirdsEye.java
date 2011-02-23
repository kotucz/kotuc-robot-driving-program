package vision.pathfinding.birdseye;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.vecmath.Point3d;
import vision.ar.PlaneProjection;

/**
 *
 * @author Kotuc
 */
public class BirdsEye {

    class Floor {
        // floor image properties

        double sizexm = 50.0;
        double sizeym = 50.0;
        double offsetxm = -sizexm / 2.0;
        double offsetym = -sizeym / 2.0;
        Rectangle2D.Double rect = new Rectangle2D.Double(offsetxm, offsetym, sizexm, sizeym);
        double pixelsperm = 10.0;
        int width = (int) (sizexm * pixelsperm);
        int height = (int) (sizeym * pixelsperm);
        int[] cells = new int[width * height];
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        Floor() {
            g.setColor(Color.GREEN);
            g.drawRect(0, 0, image.getWidth() - 1, image.getHeight() - 1);
        }

        int indexi(int x, int y) {
            return x + y * width;
        }

        // possibly out of bounds
        int getX(double xm) {
            xm -= offsetxm;
            return (int) (xm * pixelsperm);
        }

        int getY(double ym) {
            ym -= offsetym;
            return (int) (ym * pixelsperm);
        }

        int index(double xm, double ym) {
            return indexi(getX(xm), getY(ym));
        }

        private void set(double xm, double ym, int val) {
            if (rect.contains(xm, ym)) {
                cells[index(xm, ym)] = val;
                image.setRGB(getX(xm), image.getWidth() - getY(ym), val);
            }
        }
    }
    Floor floor = new Floor();
    // cam properties
    final double camHeightm = 0.7;
//    final double camTiltRads = Math.toRadians(75);
    final double camTiltRads = Math.toRadians(90);
    PlaneProjection projection = new PlaneProjection();
    double cutoffm;

    public void setCameraPosition(double x, double y, double azimrads) {
        projection.setCamera(new Point3d(x, y, camHeightm), azimrads, camTiltRads);
    }

    public void paintProjectedCameraImage(BufferedImage image) {

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Point3d planepoint = projection.getPositionOnPlanePercents(((double) x) / image.getWidth(), ((double) y) / image.getHeight());
//                System.out.println(""+planepoint);
                if (planepoint != null) {
                    floor.set(planepoint.x, planepoint.y, image.getRGB(x, y));
                }
            }
        }
    }

    public BufferedImage getFloorImage() {
        return floor.image;
    }
}
