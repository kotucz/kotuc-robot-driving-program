package vision.pathfinding.knowledge;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import vision.colors.Colors;

/**
 *
 * @author Kotuc
 */
public class HSHistogram {

    private static final int HN = 32;
    private static final int SN = 30;
//    private static final int MAX_HUE = 1;
//    private static final int MAX_SAT = 1;
    /**
     * int [hue][sat]
     */
    double[] buckets = new double[HN * SN];

    int indexi(int hue, int sat) {
        return hue + HN * sat;
    }

    int indexf(float hue, float sat) {
        return indexi(Math.round(hue * (HN - 1)), Math.round(sat * (SN - 1)));
    }

    public static HSHistogram createFrom(BufferedImage image, Rectangle rect) {
        HSHistogram hSHistogram = new HSHistogram();
        hSHistogram.initFrom(image, rect);
        return hSHistogram;
    }

    void initFrom(BufferedImage image, Rectangle rect) {
        float[] hsb = null;
        for (int i = rect.x; i < rect.x + rect.width; i++) {
            for (int j = rect.y; j < rect.y + rect.height; j++) {
                int rgb = image.getRGB(i, j);

                hsb = Color.RGBtoHSB(Colors.getRed(rgb), Colors.getGreen(rgb), Colors.getBlue(rgb), hsb);

                buckets[indexf(hsb[0], hsb[1])]++;

//                info.edge.add(Colors.getGray(conv.getRGB(i, j)));
            }
        }
    }

    void paintDebug(BufferedImage im, int x0, int y0) {
        for (int h = 0; h < HN; h++) {
            for (int s = 0; s < SN; s++) {
//                g.setColor(Color.getHSBColor((float)h/HN, (float)s/SN, (float)indexi(h, s)/100));
                im.setRGB(x0 + h, y0 + s, Color.HSBtoRGB((float) h / HN, (float) s / SN, (float) indexi(h, s) / 100));

            }
        }
    }

    int intersection(HSHistogram other) {
        if (this.buckets.length != other.buckets.length) {
            throw new IllegalArgumentException("dimension mismatch " + this.buckets.length + " " + other.buckets.length);
        }
        int intersection = 0;
        for (int i = 0; i < buckets.length; i++) {
            intersection += Math.min(buckets[i], other.buckets[i]);
        }
        return intersection;
    }
}
