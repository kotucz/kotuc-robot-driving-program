/*
 * FloorCanvas.java
 */
package vision.pathfinding;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import javax.vecmath.Point2f;


/**
 *
 * @author Tomas Kotula
 * @deprecated use PlaneCanvas
 */
public class FloorCanvas {

    private final int gw,  gh;
    private final Graphics2D g2;
    private final FloorProjection fp = new FloorProjection();

    /** Creates a new instance of FloorCanvas
     * @param canvas 
     */
    public FloorCanvas(BufferedImage canvas) {

        this.g2 = (Graphics2D) canvas.getGraphics();
        this.gw = canvas.getWidth();
        this.gh = canvas.getHeight();

    }

    public void drawLine(Point2f fp1, Point2f fp2) {
        this.drawLine(fp1.x, fp1.y, fp2.x, fp2.y);
    }

    public void drawLine(float fp1x, float fp1y, float fp2x, float fp2y) {
        Point p1 = fp.getPoint(fp1x, fp1y, gw, gh);
        Point p2 = fp.getPoint(fp2x, fp2y, gw, gh);

        g2.drawLine(p1.x, p1.y, p2.x, p2.y);
    }

    public float getDistance(float fp1x, float fp1y, float fp2x, float fp2y) {
        Point p1 = fp.getPoint(fp1x, fp1y, gw, gh);
        Point p2 = fp.getPoint(fp2x, fp2y, gw, gh);

        int x = p1.x - p2.x,
                y = p1.y - p2.y;

        return (float) Math.sqrt(x * x + y * y);
    }

    public void drawGrid() {

        g2.setColor(Color.WHITE);

        float fx = 0;
        float fy = 0;

        Point p = fp.getPoint(0, 3, gw, gh);

        g2.drawString("3m", p.x, p.y);

        p = fp.getPoint(0, 5, gw, gh);

        g2.drawString("5m", p.x, p.y);

        for (fx = -5; fx < 5.1; fx += 0.5) {
            for (fy = 0; fy < 15; fy += 0.5) {

                //            Point p1 = fp.getPoint(fx, fy, iw, ih);
//            Point p2 = fp.getPoint(fx+0.5f, fy, iw, ih);
//            Point p3 = fp.getPoint(fx, fy+0.5f, iw, ih);
//            
//            g2.drawLine(p1.x, p1.y, p2.x, p2.y);
//            g2.drawLine(p1.x, p1.y, p3.x, p3.y);

                drawLine(fx, fy, fx + 0.5f, fy);
                drawLine(fx, fy, fx, fy + 0.5f);

            }
        }


    }

    public void setColor(Color c) {
        g2.setColor(c);
    }
}
