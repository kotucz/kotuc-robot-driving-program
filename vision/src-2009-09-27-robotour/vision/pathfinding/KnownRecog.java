package vision.pathfinding;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import vision.BasicOps;
import vision.colors.Colors;

/**
 *  * @author Kotuc
 */
public class KnownRecog {

    private BufferedImage image;

    public BufferedImage getResult() {
        return image;
    }
    private List<Info> knowledge;

    public void createKnowledge(File dir) {
        knowledge = new LinkedList<Info>();
        if (!dir.isDirectory()) {
            dir = dir.getParentFile();
        }
        final File[] listFiles = dir.listFiles();
        for (File file1 : listFiles) {
            try {
                BufferedImage img = ImageIO.read(file1);
                Info info = characteristics(img, new Rectangle(0, 0, img.getWidth(), img.getHeight()));
                info.name = file1.getName();
                info.image = BasicOps.getResizedImage(img, 64, 48);
                System.out.println("Knowlege " + info.name);
                knowledge.add(info);
            } catch (IOException ex) {
                Logger.getLogger(KnownRecog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Loaded knowledge: " + knowledge.size());

    }
    private Info sample;
    public Info[] infos;

    public void process(BufferedImage image) {

        if (knowledge.isEmpty()) {
            throw new IllegalStateException("knowledge is empty!");
        }

        final int iw = image.getWidth();
        final int ih = image.getHeight();

//        BufferedImage conv = BasicOps.convolve(image, 3);

        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setStroke(new BasicStroke(3));

        this.image = image;

        final int snapy = iw / 4; // random?
        final int snapn = 10;
        final int snapw = iw / snapn;
        final int snaph = ih / 10;

        sample = characteristics(image, new Rectangle(iw / 2 - iw / 10, ih - ih / 5, iw / 5, ih / 5));
        sample.name = "GOOD";
        sample.image = image;

        int max = 0;
        this.infos = new Info[snapn];
        for (int i = 0; i < snapn; i++) {
            Info info = characteristics(image, new Rectangle(i * snapw, snapy, snapw, snaph));
//            max = Math.max((int) info.diff(sample), max);
            double bestVal = info.diff(sample);
            info.match = sample;
            for (Info known1 : knowledge) {
                double diff = known1.diff(info);
                if (diff < bestVal) {
                    info.match = known1;
                    bestVal = diff;
                }
            }

            infos[i] = info;
        }
        for (int i = 0; i < snapn; i++) {
            Info info = infos[i];
            info.color = 0xFF000000 | ((int) Math.min(255 * info.diff(sample) / max, 255) << 16);

            g.setColor(new Color(info.color));

            g.fillRect(info.rect.x, 0, info.rect.width, info.rect.height);

//            g.setColor(Color.getHSBColor((float)info.hue.avg(), 1, 1));
            g.setColor(Color.getHSBColor(0, 0, (float) info.sat.avg()));
            g.drawRect(info.rect.x, info.rect.y, info.rect.width, info.rect.height);
            if (info.match != null) {
                g.drawImage(info.match.image, info.rect.x, info.rect.y - info.rect.height, info.rect.width, info.rect.height, null);
                info.good = !info.match.name.startsWith("B");
                if (info.isGood()) {
                    g.setColor(Color.GREEN);
                } else {
                    g.setColor(Color.ORANGE);
                }
            } else {
                g.setColor(Color.getHSBColor(0, 1, 1));
            }
            g.drawRect(info.rect.x, info.rect.y - info.rect.height, info.rect.width, info.rect.height);
        }



    }

    public Info characteristics(BufferedImage image, Rectangle rect) {
        Info info = new Info();
        info.rect = rect;
        Stats hue = new Stats();

        Stats red = new Stats();
        Stats green = new Stats();
        Stats blue = new Stats();

        for (int i = rect.x; i < rect.x + rect.width; i++) {
            for (int j = rect.y; j < rect.y + rect.height; j++) {
                int rgb = image.getRGB(i, j);

                red.add(Colors.getRed(rgb));
                green.add(Colors.getGreen(rgb));
                blue.add(Colors.getBlue(rgb));

                float[] hsb = null;
                hsb = Color.RGBtoHSB(Colors.getRed(rgb), Colors.getGreen(rgb), Colors.getBlue(rgb), hsb);
                hue.add(hsb[0]);
                info.sat.add(hsb[1]);

//                info.edge.add(Colors.getGray(conv.getRGB(i, j)));
            }
        }

        info.vector = new double[]{
                    red.avg(),
                    green.avg(),
                    blue.avg(),
                    hue.avg(),
                    info.sat.avg()
                };
        return info;
    }
//    static final double[] weights = new double[]{};

    public class Info {

        Stats sat = new Stats();
        String name;
        public Rectangle rect;
        double[] vector;
        Image image;
        Info match;
//        public Stats edge = new Stats();
        int color;
        boolean good;

        public double diff(Info info) {
            double diff = 0;
            for (int i = 0; i < vector.length; i++) {
                diff += Math.abs(this.vector[i] - info.vector[i]);
            }
            return diff;
        }

        public boolean isGood() {
            return good;
        }
    }

    public class Stats {

        int count = 0;
        double sum = 0;

        void add(double num) {
            sum += num;
            count++;
        }

        double avg() {
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
