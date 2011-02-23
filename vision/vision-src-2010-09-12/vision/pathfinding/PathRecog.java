package vision.pathfinding;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import vision.colors.HeatMap;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import vision.CommonParameters;

/**
 * Searches for left and right road edge. Computes best way though the center of the road.
 * Uses linear regression for left and right edges.
 * @author Kotuc
 */
public class PathRecog {

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
        return -2 * q;
    }

    public void process(BufferedImage src) {

        final double saturationTresh = CommonParameters.saturationTreshold.getValue();

        iw = src.getWidth();
        ih = src.getHeight();

//        HeatMap hm = HeatMap.getHeatMapCM(src);
//        HeatMap hm = HeatMap.getHeatMap2_2(src);
        HeatMap hm = HeatMap.createHeatMap(src, HeatMap.SATURATION_HEAT_FUNCTION);

        hm.setTreshold(saturationTresh);
//        HeatMap hm = HeatMap.createHeatMap(src, HeatMap.HUE_HEAT_FUNCTION);

//        image = hm.createRoadBnWImage();
        image = hm.createHeatImage(HeatMap.RED_GREEN_TESH(saturationTresh));
//        image = hm.createHeatImage(HeatMap.THERMAL);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setStroke(new BasicStroke(3));

        int leftBorder, rightBorder, center = iw / 2;

        int centerSum = 0;

        List<Point> lpoints = new ArrayList<Point>();
        List<Point> cpoints = new ArrayList<Point>();
        List<Point> rpoints = new ArrayList<Point>();

        int cykoef = 2;

        for (int y = ih - 1; y >= 0; y--) {
            rightBorder = center;
            while (rightBorder < iw - 1 && hm.isRoad(rightBorder, y)) {
                rightBorder++;
            }
            leftBorder = center;
            while (leftBorder > 0 && hm.isRoad(leftBorder, y)) {
                leftBorder--;
            }
            center = (leftBorder + rightBorder) / 2;
            centerSum += center;

            if (rightBorder - leftBorder > 10) {

//                lpoints.add(new Point(leftBorder, y));
                lpoints.add(new Point(y, leftBorder));
//                cpoints.add(new Point(center, cykoef*y));
                cpoints.add(new Point(y, center));
//                rpoints.add(new Point(rightBorder, y));
                rpoints.add(new Point(y, rightBorder));

                g.setColor(Color.WHITE);
                g.fillOval(leftBorder - 1, y - 1, 3, 3);
                g.fillOval(center - 1, y - 1, 3, 3);
                g.fillOval(rightBorder - 1, y - 1, 3, 3);
            }
        }
        this.average = centerSum / ih;
        g.drawLine(average, 0, average, ih);
//        System.out.println("Avg: " + getAverage() +
//                "\tSteer: " + getGoodSteer());

        g.setColor(Color.RED);
        linearRegression(lpoints);
//        g.drawLine(0, (int) k, iw, (int) (iw * q + k));
        g.drawLine((int) k, 0, (int) (ih * q + k), ih);


        g.setColor(Color.BLUE);
        linearRegression(rpoints);
//        g.drawLine(0, (int) k, iw, (int) (iw * q + k));
        g.drawLine((int) k, 0, (int) (ih * q + k), ih);

        g.setColor(Color.GREEN);
        linearRegression(cpoints);
//        g.drawLine(0, (int) k/cykoef, iw, (int) (iw * q/cykoef + k/cykoef));
        g.drawLine((int) k, 0, (int) (ih * q + k), ih);

        g.drawString("" + getGoodSteer(), 50, 50);

    }
    private double k, q;

    /**
     * @see http://www.cs.princeton.edu/introcs/97data/LinearRegression.java.html
     */
    public void linearRegression(List<Point> points) {
        int n = points.size();

        // first pass: read in data, compute xbar and ybar
        double sumx = 0.0, sumy = 0.0, sumx2 = 0.0;
//        while (!StdIn.isEmpty()) {
//            x[n] = StdIn.readDouble();
//            y[n] = StdIn.readDouble();
//            sumx += x[n];
//            sumx2 += x[n] * x[n];
//            sumy += y[n];
//            n++;
//        }
        for (Point point : points) {

            sumx += point.x;
            sumx2 += point.x * point.x;
            sumy += point.y;

        }

        double xbar = sumx / n;
        double ybar = sumy / n;

        // second pass: compute summary statistics
        double xxbar = 0.0, yybar = 0.0, xybar = 0.0;
//        for (int i = 0; i < n; i++) {
//            xxbar += (x[i] - xbar) * (x[i] - xbar);
//            yybar += (y[i] - ybar) * (y[i] - ybar);
//            xybar += (x[i] - xbar) * (y[i] - ybar);
//        }
        for (Point point : points) {
            xxbar += (point.x - xbar) * (point.x - xbar);
            yybar += (point.y - ybar) * (point.y - ybar);
            xybar += (point.x - xbar) * (point.y - ybar);
        }
        double beta1 = xybar / xxbar;
        double beta0 = ybar - beta1 * xbar;

        // print results
//        System.out.println("y   = " + beta1 + " * x + " + beta0);

        q = beta1;
        k = beta0;

//        // analyze results
//        int df = n - 2;
//        double rss = 0.0;      // residual sum of squares
//        double ssr = 0.0;      // regression sum of squares
//        for (int i = 0; i < n; i++) {
//            double fit = beta1 * x[i] + beta0;
//            rss += (fit - y[i]) * (fit - y[i]);
//            ssr += (fit - ybar) * (fit - ybar);
//        }
//        double R2 = ssr / yybar;
//        double svar = rss / df;
//        double svar1 = svar / xxbar;
//        double svar0 = svar / n + xbar * xbar * svar1;
//        System.out.println("R^2                 = " + R2);
//        System.out.println("std error of beta_1 = " + Math.sqrt(svar1));
//        System.out.println("std error of beta_0 = " + Math.sqrt(svar0));
//        svar0 = svar * sumx2 / (n * xxbar);
//        System.out.println("std error of beta_0 = " + Math.sqrt(svar0));
//
//        System.out.println("SSTO = " + yybar);
//        System.out.println("SSE  = " + rss);
//        System.out.println("SSR  = " + ssr);

    }
}
