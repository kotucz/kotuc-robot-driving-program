/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vision;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * noninstantible
 * @author Kotuc
 */
final class HSBImage {

    private HSBImage() {
    }

    public static BufferedImage rgbToHSB(BufferedImage src) {
        BufferedImage dest = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
        dest = new ColorTransform() {

            @Override
            public int getColor(int... colors) {
                Color color = new Color(colors[0]);
                float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
                if (hsb[1] > 0.5/* && hsb[2] > 0.3*/) {
                    return Color.getHSBColor(hsb[0], 1, 1).getRGB();
                } else {
                    return Color.GRAY.getRGB();
                }
            }
        }.apply(src);
        return dest;
    }
}
