package vision.colors;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 *
 * @author Kotuc
 */
public class HSHistogram {

    private static final int HUE_SHADES = 500;
    private static final int SAT_SHADES = HUE_SHADES;
    int[] vals = new int[HUE_SHADES * SAT_SHADES];
    int sumtotal;
    int max = 0;

    public static BufferedImage roadTable() {
        BufferedImage image = new BufferedImage(HUE_SHADES, SAT_SHADES, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < HUE_SHADES; x++) {
            for (int y = 0; y < SAT_SHADES; y++) {

                float h = (float) x / HUE_SHADES;
                float s = (float) y / SAT_SHADES;
                float b = (s < 0.5) ? 1f : 0f;

                image.setRGB(x, y, Color.HSBtoRGB(h, s, b));

            }
        }
        return image;
    }

    public BufferedImage table() {
        BufferedImage image = new BufferedImage(HUE_SHADES, SAT_SHADES, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < HUE_SHADES; x++) {
            for (int y = 0; y < SAT_SHADES; y++) {

                float h = (float) x / HUE_SHADES;
                float s = (float) y / SAT_SHADES;
                float b = Math.min((float) vals[x + y * SAT_SHADES] / 5f, 1f);

                image.setRGB(x, y, Color.HSBtoRGB(h, s, b));

            }
        }
        return image;
    }

    public BufferedImage parse(BufferedImage image) {
        int[] rgbs = null;
        int bands = image.getRaster().getNumBands();
        rgbs = image.getRaster().getPixels(0, 0, image.getWidth(), image.getHeight(), rgbs);

        for (int i = 0; i < rgbs.length / bands; i++) {
            add(rgbs[bands * i + 0], rgbs[bands * i + 1], rgbs[bands * i + 2]);
        }

//        for (int i = 0; i < rgbs.length; i++) {
//            add(rgbs[i]);
//
//        }
//        for (int rgb : rgbs) {
//            add(rgb);
//        }
        sumtotal = rgbs.length;
        System.out.println("max " + max);
        return this.table();
    }

    private void add(int r, int g, int b) {
        int index = index(r, g, b);
//        System.out.println(index);
        vals[index]++;
        max = Math.max(max, vals[index]);
    }

    int index(int r, int g, int b) {
        float[] hsb = new float[3];
        hsb = Color.RGBtoHSB(r, g, b, hsb);
        int x = Math.round(hsb[0] * (HUE_SHADES - 1));
        int y = Math.round(hsb[1] * (SAT_SHADES - 1));

        return x + y * SAT_SHADES;
    }
//    private void add(int rgb) {
//        int index = index(rgb);
//        System.out.println(index);
//        vals[index]++;
//        max = Math.max(max, vals[index]);
//    }
//    int index(int rgb) {
//        float[] hsb = null;
//        hsb = Color.RGBtoHSB(Colors.getRed(rgb), Colors.getGreen(rgb), Colors.getBlue(rgb), hsb);
////        Color color = new Color(rgb);
////        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
//        System.out.println("hsb " + Arrays.toString(hsb));
//        int x = Math.round(hsb[0] * (HUE_SHADES - 1));
//        int y = Math.round(hsb[1] * (SAT_SHADES - 1));
//
//        return x + y * SAT_SHADES;
//    }
}
