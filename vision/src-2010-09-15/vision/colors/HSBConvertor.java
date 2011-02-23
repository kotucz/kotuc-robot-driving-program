package vision.colors;

import java.awt.Color;
import java.awt.image.BufferedImage;
import vision.CommonParameters;

/**
 * noninstantible
 * @author Kotuc
 */
public final class HSBConvertor {
   
    // never used!!!!
    private HSBConvertor() {
        throw new AssertionError();
    }

    public static BufferedImage rgbToHSB(BufferedImage src) {
        BufferedImage dest = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
        dest = new ColorTransform() {

            @Override
            public int getColor(int... colors) {
                Color color = new Color(colors[0]);
                float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
                if (hsb[1] > 0/* && hsb[2] > 0.3*/) {
                    return Color.getHSBColor(hsb[0], 1, 1).getRGB();
                } else {
                    return Color.GRAY.getRGB();
                }
            }
        }.apply(src);
        return dest;
    }

    public static BufferedImage hue(BufferedImage src) {
        BufferedImage dest = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
        dest = new ColorTransform() {

            @Override
            public int getColor(int... colors) {
                Color color = new Color(colors[0]);
                float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
                if (hsb[1] > CommonParameters.saturationTreshold.getValue()/* && hsb[2] > 0.3*/) {
                    return Color.getHSBColor(hsb[0], 1, 1).getRGB();
                } else {
                    return Color.GRAY.getRGB();
                }
            }
        }.apply(src);
        return dest;
    }

    public static BufferedImage saturation(BufferedImage src) {
        BufferedImage dest = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
        dest = new ColorTransform() {

            @Override
            public int getColor(int... colors) {
                Color color = new Color(colors[0]);
                float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
                return Color.getHSBColor(1, 0, hsb[1]).getRGB();
            }
        }.apply(src);
        return dest;
    }
}
