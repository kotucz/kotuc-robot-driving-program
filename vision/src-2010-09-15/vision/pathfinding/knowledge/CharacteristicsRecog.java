package vision.pathfinding.knowledge;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * Searches for left and right road corner. Computes best way though the center of the road.
 * @author Kotuc
 */
public class CharacteristicsRecog {

    private int iw, ih;
    final int wn = 32;
    final int hn = 30;
    private static final double sampleScaleFactor = 5;

//    final int wn = 64;
//    final int hn = 60;
    Info[][] infos = new Info[wn][hn];
    DoubleMatrix values = new DoubleMatrix(wn, hn);
//    private int average = -1;
//
//    public int getAverage() {
//        if (average == -1) {
//            throw new IllegalStateException("nothing processed");
//        }
//        return average;
//    }
    /**
     *
     * @return
     */
//    public double getGoodSteer() {
////        return ((2.0 * getAverage()) / (double) iw) - 1.0;
//        return 0;
//    }
    private Info sample;
    double max = 0;
    double min = 1;

    public void process(BufferedImage image) {

        iw = image.getWidth();
        ih = image.getHeight();

//        BufferedImage conv = BasicOps.convolve(image, 3);

        int w = iw / wn;
        int h = ih / hn;

        sample = Info.createFrom(image, new Rectangle(iw / 2 - iw / 10, ih - ih / 5, iw / 5, ih / 5));
        sample.hist.scale(sampleScaleFactor);

        max = 0;
        min = 10000;

        for (int x = 0; x < wn; x++) {
            for (int y = 0; y < hn; y++) {
                Info info = Info.createFrom(image, new Rectangle(w * x, h * y, w, h));
                double diff = info.diff(sample);
//                System.out.println("diff " + diff);
                max = Math.max(diff, max);
                min = Math.min(diff, min);
                values.set(x, y, diff);
                infos[x][y] = info;
            }
        }
    }

    public DoubleMatrix getValues() {
        return values;
    }

    public BufferedImage getDebugImage() {

        return values.getImage();

//        BufferedImage result = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_RGB);
//
//        Graphics2D g = (Graphics2D) result.getGraphics();
//        g.setStroke(new BasicStroke(3));
//
////        this.image = image;
//
//        for (int i = 0; i < wn; i++) {
//            for (int j = 0; j < hn; j++) {
//                Info info = infos[i][j];
//                double diff = info.diff(sample);
//                info.color = 0xFF000000 | ((int) Math.min(255.0 * (diff - min) / (max - min), 255) << 16);
//
//                g.setColor(new Color(info.color));
//
//                g.fillRect(info.rect.x, info.rect.y, info.rect.width, info.rect.height);
//            }
//        }
//        return result;
    }
//    private double k, q;
//    /**
//     * @see http://www.cs.princeton.edu/introcs/97data/LinearRegression.java.html
//     */
//    public void linearRegression(List<Point> points) {
//        int n = points.size();
//
//        // first pass: read in data, compute xbar and ybar
//        double sumx = 0.0, sumy = 0.0, sumx2 = 0.0;
////        while (!StdIn.isEmpty()) {
////            x[n] = StdIn.readDouble();
////            y[n] = StdIn.readDouble();
////            sumx += x[n];
////            sumx2 += x[n] * x[n];
////            sumy += y[n];
////            n++;
////        }
//        for (Point point : points) {
//
//            sumx += point.x;
//            sumx2 += point.x * point.x;
//            sumy += point.y;
//
//        }
//
//        double xbar = sumx / n;
//        double ybar = sumy / n;
//
//        // second pass: compute summary statistics
//        double xxbar = 0.0, yybar = 0.0, xybar = 0.0;
////        for (int i = 0; i < n; i++) {
////            xxbar += (x[i] - xbar) * (x[i] - xbar);
////            yybar += (y[i] - ybar) * (y[i] - ybar);
////            xybar += (x[i] - xbar) * (y[i] - ybar);
////        }
//        for (Point point : points) {
//            xxbar += (point.x - xbar) * (point.x - xbar);
//            yybar += (point.y - ybar) * (point.y - ybar);
//            xybar += (point.x - xbar) * (point.y - ybar);
//        }
//        double beta1 = xybar / xxbar;
//        double beta0 = ybar - beta1 * xbar;
//
//        // print results
////        System.out.println("y   = " + beta1 + " * x + " + beta0);
//
//        q = beta1;
//        k = beta0;
//
////        // analyze results
////        int df = n - 2;
////        double rss = 0.0;      // residual sum of squares
////        double ssr = 0.0;      // regression sum of squares
////        for (int i = 0; i < n; i++) {
////            double fit = beta1 * x[i] + beta0;
////            rss += (fit - y[i]) * (fit - y[i]);
////            ssr += (fit - ybar) * (fit - ybar);
////        }
////        double R2 = ssr / yybar;
////        double svar = rss / df;
////        double svar1 = svar / xxbar;
////        double svar0 = svar / n + xbar * xbar * svar1;
////        System.out.println("R^2                 = " + R2);
////        System.out.println("std error of beta_1 = " + Math.sqrt(svar1));
////        System.out.println("std error of beta_0 = " + Math.sqrt(svar0));
////        svar0 = svar * sumx2 / (n * xxbar);
////        System.out.println("std error of beta_0 = " + Math.sqrt(svar0));
////
////        System.out.println("SSTO = " + yybar);
////        System.out.println("SSE  = " + rss);
////        System.out.println("SSR  = " + ssr);
//
//    }
//
}
