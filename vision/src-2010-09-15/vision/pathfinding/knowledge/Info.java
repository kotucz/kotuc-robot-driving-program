package vision.pathfinding.knowledge;

import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import vision.colors.Colors;

public class Info {

    Stats sat = new Stats();
    Stats hue = new Stats();
    Stats red = new Stats();
    Stats green = new Stats();
    Stats blue = new Stats();

    HSHistogram hist = new HSHistogram();

    String name;
    public Rectangle rect;
    double[] vector;
    Image image;
    Info match;
    //        public Stats edge = new Stats();
    int color;
    boolean good;
    double distance;

    private Info() {
    }

    public double diff(Info info) {
        if (info==null) {
            throw new NullPointerException("info");
        }
//        System.out.println("info"+info +" rect "+rect+" hist "+hist);
        double diff = 0;
//        diff += (rect.getWidth()*rect.getHeight())-this.hist.intersection(info.hist);

        diff += 1-this.hist.intersection(info.hist);
//        diff += this.hist.difference(info.hist);

//        for (int i = 0; i < vector.length; i++) {
//            diff += Math.abs(this.vector[i] - info.vector[i]);
//        }
        return diff;
    }

    public boolean isGood() {
        return good;
    }

    public static Info createFrom(BufferedImage image, Rectangle rect) {
        Info info = new Info();
        info.initFrom(image, rect);
        return info;
    }

    private void initFrom(BufferedImage image, Rectangle rect) {

        this.rect = rect;

        this.hist = HSHistogram.createFrom(image, rect);

        for (int i = rect.x; i < rect.x + rect.width; i++) {
            for (int j = rect.y; j < rect.y + rect.height; j++) {
                int rgb = image.getRGB(i, j);

                red.add(Colors.getRed(rgb));
                green.add(Colors.getGreen(rgb));
                blue.add(Colors.getBlue(rgb));

                float[] hsb = null;
                hsb = Color.RGBtoHSB(Colors.getRed(rgb), Colors.getGreen(rgb), Colors.getBlue(rgb), hsb);
                hue.add(hsb[0]);
                sat.add(hsb[1]);

//                info.edge.add(Colors.getGray(conv.getRGB(i, j)));
            }
        }

        this.vector = new double[]{
                    red.avg(),
                    green.avg(),
                    blue.avg(),
                    hue.avg(),
                    sat.avg()
                };

    }
}
