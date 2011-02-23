package vision.ar;

import java.awt.Graphics;
import java.awt.Point;
import javax.vecmath.Point3d;

/**
 *
 * @author Kotuc
 */
public class PlaneCanvas {

    private PlaneProjection proj = new PlaneProjection();
    private Graphics g;

    public PlaneProjection getPlaneProjection() {
        return proj;
    }

    public void setGraphics(Graphics g) {
        this.g = g;
    }
    private double nearz = -0.1;

    public void drawLine(Point3d point1, Point3d point2) {
//        System.out.println("line " + point1 + " " + point2);
        proj.toCameraSpace(point1);
        if (point1.z > nearz) {
            return;
        }
        proj.toCameraSpace(point2);
        if (point2.z > nearz) {
            return;
        }
//        Point p1 = proj.getPositionOnImage(point1);
        Point p1 = proj.getCameraProportions().getProjectionImagePoint(point1);
//        Point p2 = proj.getPositionOnImage(point2);
        Point p2 = proj.getCameraProportions().getProjectionImagePoint(point2);
        g.drawLine(p1.x, p1.y, p2.x, p2.y);
    }

    public void drawGrid(int x0, int y0) {

        double size = 20;
        double step = 0.25;

//        for (double w = -size; w <= size; w += 0.25) {
//
//            Point3d p1 = new Point3d(x0 + size , y0 + w, 0);
//            Point3d p2 = new Point3d(x0 - size , y0 + w, 0);
//
//            drawLine(p1, p2);
//
//
//            Point3d p3 = new Point3d(x0 + w, y0 + size, 0);
//            Point3d p4 = new Point3d(x0 + w, y0 - size, 0);
//
//            drawLine(p3, p4);
//
//        }

        double cross = step / 4.0;

        for (double x = -size; x < size; x += step) {
            for (double y = -size; y < size; y += step) {

                Point3d p1 = new Point3d(x0 + x + cross, y0 + y, 0);
                Point3d p2 = new Point3d(x0 + x - cross, y0 + y, 0);

                drawLine(p1, p2);


                Point3d p3 = new Point3d(x0 + x, y0 + y + cross, 0);
                Point3d p4 = new Point3d(x0 + x, y0 + y - cross, 0);

                drawLine(p3, p4);

            }
        }


    }
}
