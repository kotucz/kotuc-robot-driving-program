/*
 * Spectrograph.java
 *
 * 
 */
package vision.colors;

import java.awt.Color;
import java.awt.image.BufferedImage;


/**
 * Static class for color methods
 * @author Tomas Kotula
 */
public final class Spectrograph {


    /**
     * noninstantiable
     */
    private Spectrograph() {
        
    }

    /**
     * 
     * all image colors are tramsformated to their normal intensity = 255
     * except dark colors are set to black due to smaller precision
     * 
     * @param image
     * @return new processed image
     */
    public static BufferedImage spectrograph(BufferedImage image) {

        int iw = image.getWidth();
        int ih = image.getHeight();

        BufferedImage bi = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_RGB);
        bi.createGraphics();

        for (int y = 0; y < ih; y++) {
            for (int x = 0; x < iw; x++) {

                bi.setRGB(x, y, toSpectralColor(new Color(image.getRGB(x, y))).getRGB());

            }
        }

        return bi;

    }

    /**
     * 
     * @param c color
     * @return normalized color
     */
    public static Color toSpectralColor(Color c) {

        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();

        //                float sum = r+g+b;
        double sum = Math.sqrt(r * r + g * g + b * b);

        if (sum < 51) {
            return Color.BLACK;
        } else {
            double intensity = 255f / sum;
            return new Color((int) (r * intensity), (int) (g * intensity), (int) (b * intensity));
        }
    }

    
    
    //     public static int toSpectralColor(int rgb) {
//                        
//                float r = (color&0xFF0000)>>32;
//                float g = (color&0xFF00)>>16;
//                float b = (color&0xFF);
////              
//                float sum = r+g+b;
//                
//                if (sum<25) return 0;
//                else {
//                    float intensity = 255f/(float)Math.sqrt(r*r+g*g+b*b);
//                    return ((int)(r*intensity), (int)(g*intensity), (int)(b*intensity));
//                }
//                               
//        
// 
//    }


    
}
