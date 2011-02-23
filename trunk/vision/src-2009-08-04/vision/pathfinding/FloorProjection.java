/*
 * CG.java
 * 
 */
package vision.pathfinding;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import javax.vecmath.Matrix3f;
import javax.vecmath.Point2f;
import javax.vecmath.Vector3f;
import vision.Setup;

/**
 *
 * this is used to transform image from camera to its floor projection
 * 
 * @author Tomas Kotula
 */
public class FloorProjection {

    /** Creates a new instance */
    public FloorProjection() {


        // constant        
        float tau = 0.9f;

        // vectors in camera space
        cx = new Vector3f(tau, 0, 0);
        cy = new Vector3f(0, 0, -0.75f * tau);
        cz = new Vector3f(0, 1, 0);

        height = Setup.getInt(Setup.CAM_HEIGHT, 500) / 1000f;


        transform = new Matrix3f();
        transform.rotX((float) Math.toRadians(-Setup.getInt(Setup.CAM_ANGLE, 0)));
        //        transform.transform(new Vector3f(0, 0, -height)); !!! vectors dont move origin
//        transform.rotZ(wang);

        transform.transform(cx);
        transform.transform(cy);
        transform.transform(cz);
    }
    private float height;
    private Vector3f cz,  cy,  cx;
    private Matrix3f transform;

    /**
     *  @param floorx, from center to right
     *  @param floory, forward
     *  @returns the pos of pixel laying at floor
     */
    Point2f getPoint2f(float floorx, float floory) {

        //   cx cy cz 
        float i,
                j,
                k;
        float xd,
                yd,
                zd,
                wd;

        Vector3f cw = new Vector3f(floorx, floory - 0.55f, height);
        cw.y = Math.max(0, cw.y);

        wd = new Matrix3f(cx.x, cy.x, cz.x,
                cx.y, cy.y, cz.y,
                cx.z, cy.z, cz.z).determinant();

        xd = new Matrix3f(cy.x, cz.x, cw.x,
                cy.y, cz.y, cw.y,
                cy.z, cz.z, cw.z).determinant();

        yd = new Matrix3f(cx.x, cz.x, cw.x,
                cx.y, cz.y, cw.y,
                cx.z, cz.z, cw.z).determinant();

        zd = new Matrix3f(cx.x, cy.x, cw.x,
                cx.y, cy.y, cw.y,
                cx.z, cy.z, cw.z).determinant();

        i = xd / wd;
        j = yd / wd;
        k = zd / wd;

        return new Point2f(i / k, j / k);
    }

    //    Dimension dim = new Dimension(320, 240);
    public Point getPoint(float floorx, float floory, int iw, int ih) {
        Point2f p2f = getPoint2f(floorx, floory);
        return new Point((int) ((p2f.x + 0.5) * iw), (int) ((p2f.y + 0.5) * ih));
    }
    /**
     * Pixel size
     * meter in pixel 0.01 == 1px == 1cm
     */
    private float mpx = 0.02f;

    public BufferedImage filter(BufferedImage cam, BufferedImage floor) {

        int iw = cam.getWidth();
        int ih = cam.getHeight();


        final int floorw = 400;
        final int floorh = 400;



        if (floor == null) {
            floor = new BufferedImage(floorw, floorh, BufferedImage.TYPE_INT_RGB);
        }
        floor.createGraphics();


        for (int y = 0; y < floorh; y++) {
            for (int x = 0; x < floorw; x++) {
                try {
                    Point2f p = getPoint2f((x - floorw / 2) * mpx, y * mpx);
                    floor.setRGB(x, y, cam.getRGB((int) ((0.5 + p.x) * iw),
                            (int) ((0.5 + p.y) * ih)));
                } catch (ArrayIndexOutOfBoundsException ex) {
                }
            }
        }

        Graphics2D g2 = (Graphics2D) floor.getGraphics();
        g2.setColor(Color.CYAN);

        for (int i = 0; i < 20; i++) {
            g2.drawLine(0, (int) (i / mpx), floorw, (int) (i / mpx));
            g2.drawString((int) (i) + "m", 0, (int) (i / mpx));
        }

        return floor;

    }

    public static BufferedImage putDown(BufferedImage image) {
        return new FloorProjection().filter(image, null);
    }
}
