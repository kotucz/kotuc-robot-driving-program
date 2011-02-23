package vision.pathfinding.birdseye;

import java.awt.image.BufferedImage;
import vision.pathfinding.knowledge.DoubleMatrix;

public class RealFloor extends Floor {

//    DoubleMatrix values = new DoubleMatrix(width, height);
//    public final DoubleMatrix values = new DoubleMatrix(width, height, Double.NaN);
    public final DoubleMatrix matrix = new DoubleMatrix(width, height, 0.5);

    public void set(double xm, double ym, int val) {
        throw new IllegalArgumentException("do not inssert rgb");
    }

    public void set(double xm, double ym, double val) {
        if (contains(xm, ym)) {
            matrix.set(getX(xm), getY(ym), val);
        }
    }

    public double get(double xm, double ym) {
        if (contains(xm, ym)) {
            return matrix.get(getX(xm), getY(ym));
        } else {
            return Double.NaN;
        }
    }

    public void fillRect(int x, int y, int w, int h, double val) {
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                matrix.set(x+i, y+j, val);
            }
        }
    }

    public BufferedImage getImage() {
        return matrix.getImage();
    }
}
