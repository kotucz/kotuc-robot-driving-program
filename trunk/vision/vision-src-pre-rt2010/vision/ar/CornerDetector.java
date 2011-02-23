package vision.ar;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import vision.BasicOps;
import vision.CameraProportions;
import vision.colors.Colors;

/**
 *
 * @author Kotuc
 */
public class CornerDetector {

    private static final int step = 2; // distance between pixels
    private static final double tresh = 0.10;
    // harris constant cca in 0.04 to 0.15
    private static final double k = 0.04;
//               final double k = 0.15;


//    private TreeMap<Double, Point> sigPoints = new TreeMap<Double, Point>();
    /**
     * Find corner depending on getCornerIntensityFunction
     *
     * @param image
     * @return
     */
    public static BufferedImage findCorners(BufferedImage image) {
        int iw = image.getWidth();
        int ih = image.getHeight();
        BufferedImage output = BasicOps.copy(image);
        Graphics g = output.createGraphics();

        for (int y = step; y < ih - step; y++) {
            for (int x = step; x < iw - step; x++) {

                double mc = getCornerIntensity(image, x, y);

//                sigPoints.put(mc, new Point(x, y));

//                mc *= 10;

//                double valx = 0.5 + (w-e)/(2*(e-2*c+w));
//                double valy = 0.5 + (n-s)/(2*(s-2*c+n));
//                double valx = Colors.dist(w,e);
//                double valy = Colors.dist(s,n);
                output.setRGB(x, y, (int) (Math.max(0, Math.min(mc, 1)) * 10 * 255));
                if (mc > tresh) {
                    output.setRGB(x, y, 0xFF0000);
                }

            }
        }
        return output;
    }

    /**
     * Harris-Laplace corner finder
     * @see http://en.wikipedia.org/wiki/Corner_detection
     */
    private static double getCornerIntensity(BufferedImage image, int x, int y) {
        if (x - step < 0 || y - step < 0 ||
                x + step >= image.getWidth() || y + step >= image.getHeight()) {
            return 0;
        }
        int n = image.getRGB(x, y - step);
        int s = image.getRGB(x, y + step);
        int e = image.getRGB(x + step, y);
        int w = image.getRGB(x - step, y);
        int ne = image.getRGB(x + step, y - step);
        int sw = image.getRGB(x - step, y + step);
        int se = image.getRGB(x + step, y + step);
        int nw = image.getRGB(x - step, y - step);
        int c = image.getRGB(x, y);
        // 2nd (xx) intensity derivation
        double derIxx = (Colors.sub(e, c) + Colors.sub(w, c));
        double derIyy = (Colors.sub(s, c) + Colors.sub(n, c));
        double derIxy = (Colors.sub(nw, ne) - Colors.sub(se, sw)) / 4;
//                double valx = 0.5 + (Colors.sub(w, e) / (2 * derIxx));
//                double valy = 0.5 + (Colors.sub(n, s) / (2 * derIyy));

        double mc = (derIxx * derIyy - derIxy * derIxy) -
                k * Math.pow(derIxx + derIyy, 2);

        return mc;
    }

    public static Point getLocalCornerMaximum(BufferedImage image, int x, int y, int w, int h) {
        Point max = new Point();
        int x0 = x;
        int y0 = y;
        int x1 = x + w;
        int y1 = y + h;
        double maxVal = 0;
        for (x = x0; x < x1; x++) {
            for (y = y0; y < y1; y++) {
                double intens = getCornerIntensity(image, x, y);
                if (intens > maxVal) {
                    max.x = x;
                    max.y = y;
                    maxVal = intens;
                }
            }
        }
        return max;
    }
}
