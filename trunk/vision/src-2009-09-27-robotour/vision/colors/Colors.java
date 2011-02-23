package vision.colors;

import java.awt.Color;

/**
 * Static class for color methods
 * @author Kotuc
 */
public final class Colors {

    // nonintantible
    private Colors() {
    }

    /**
     * 
     * @param rgb1
     * @param rgb2
     * @return abs substraction
     */
    public static final double dist(int rgb1, int rgb2) {
        return Math.abs(sub(rgb1, rgb2));
    }

    /**
     * 
     * @param rgb1
     * @param rgb2
     * @return gray substraction of rgb1 - rgb2
     */
    public static final double sub(int rgb1, int rgb2) {
        return (Math.abs(getRed(rgb1) - getRed(rgb2)) +
                Math.abs(getGreen(rgb1) - getGreen(rgb2)) +
                Math.abs(getBlue(rgb1) - getBlue(rgb2))) / (3.0 * 255);
    }

    public static final double hsbDist(int rgb1, int rgb2) {
        final float[] hsb1 = Color.RGBtoHSB(getRed(rgb1), getGreen(rgb1), getBlue(rgb1), null);
        final float[] hsb2 = Color.RGBtoHSB(getRed(rgb2), getGreen(rgb2), getBlue(rgb2), null);

        return 0.7 * Math.abs((hsb1[0] - hsb2[0]) % 1.0) + 0.3 * Math.abs(hsb1[1] - hsb2[1]);
    }

    public static final int getRed(int rgb) {
        return 0xFF & (rgb >> 16);
    }

    public static final int getGreen(int rgb) {
        return 0xFF & (rgb >> 8);
    }

    public static final int getBlue(int rgb) {
        return 0xFF & (rgb >> 0);
    }

    public static final int getGray(int rgb) {
        return (getRed(rgb) + getGreen(rgb) + getBlue(rgb)) / 3;
    }

    public static final int getColor(int red, int green, int blue) {
        return 0xFF000000 | (red << 16) | (green << 8) | (blue);
    }
}
