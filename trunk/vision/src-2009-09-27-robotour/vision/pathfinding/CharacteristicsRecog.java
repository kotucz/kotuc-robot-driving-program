package vision.pathfinding;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import vision.BasicOps;
import vision.colors.Colors;

/**
 * Searches for left and right road corner. Computes best way though the center of the road.
 * @author Kotuc
 */
public class CharacteristicsRecog {

    private int iw, ih;
    private BufferedImage image;

    public BufferedImage getResult() {
        return image;
    }
    private int average = -1;

    public int getAverage() {
        if (average == -1) {
            throw new IllegalStateException("nothing processed");
        }
        return average;
    }

    /**
     *
     * @return
     */
    public double getGoodSteer() {
//        return ((2.0 * getAverage()) / (double) iw) - 1.0;
        return 0;
    }
    private Info sample;

    public void process(BufferedImage image) {

        iw = image.getWidth();
        ih = image.getHeight();

        BufferedImage conv = BasicOps.convolve(image, 3);

        BufferedImage black = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = (Graphics2D) black.getGraphics();
        g.setStroke(new BasicStroke(3));

//        this.image = image;
        this.image = black;

        int wn = 10;
        int hn = 10;

        int w = iw / wn;
        int h = ih / hn;

        sample = characteristics(image, conv, new Rectangle(iw / 2 - iw / 10, ih - ih / 5, iw / 5, ih / 5));
        int max = 0;
        Info[][] infos = new Info[wn][hn];
        for (int i = 0; i < wn; i++) {
            for (int j = 0; j < hn; j++) {
                Info info = characteristics(image, conv, new Rectangle(w * i + 1, h * j + 1, w - 3, h - 3));
                max = Math.max((int) info.diff(sample), max);
                infos[i][j] = info;
            }
        }
        for (int i = 0; i < wn; i++) {
            for (int j = 0; j < hn; j++) {
                Info info = infos[i][j];
                info.color = 0xFF000000 | ((int) Math.min(255 * info.diff(sample) / max, 255) << 16);

                g.setColor(new Color(info.color));

                g.fillRect(info.rect.x, info.rect.y, info.rect.width, info.rect.height);

            }
        }

        

    }

    public Info characteristics(BufferedImage image, BufferedImage conv, Rectangle rect) {
        Info info = new Info();
        info.rect = rect;

        for (int i = rect.x; i < rect.x + rect.width; i++) {
            for (int j = rect.y; j < rect.y + rect.height; j++) {
                int rgb = image.getRGB(i, j);
                info.red.add(Colors.getRed(rgb));
                info.green.add(Colors.getGreen(rgb));
                info.blue.add(Colors.getBlue(rgb));

                info.edge.add(Colors.getGray(conv.getRGB(i, j)));
            }
        }

        return info;
    }

    class Info {

        Rectangle rect;
        Stats red = new Stats();
        Stats green = new Stats();
        Stats blue = new Stats();
        Stats edge = new Stats();
        int color;

        public double diff(Info info) {
            return 3.0*Math.abs(info.edge.avg() - this.edge.avg()) +
                   Math.abs(info.red.avg() - this.red.avg()) +
                   Math.abs(info.green.avg() - this.green.avg()) +
                   Math.abs(info.blue.avg() - this.blue.avg());
        }
    }

    class Stats {

        int count = 0;
        int sum = 0;

        void add(int num) {
            sum += num;
            count++;
        }

        int avg() {
            return sum / count;
        }
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
