/*
 * 
 */

package vision;

import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;

/**
 *
 * Collection of operations that may look good, but was replaced by ops with
 * more functions
 * 
 * @author Tomas Kotula
 */
public class UselessOps {
    
    /**
     * 
     * @param image
     * @return
     */
    public static BufferedImage niceSpectrograph(BufferedImage image) {
        int iw = image.getWidth();
        int ih = image.getHeight();

        BufferedImage bi = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_RGB);
        bi.createGraphics();

        for (int y = 0; y < ih; y++) {
            for (int x = 0; x < iw; x++) {

                int color = image.getRGB(x, y);

                int r = (color & 0xFF0000) >> 32;
                int g = (color & 0xFF00) >> 16;
                int b = (color & 0xFF);
                int sum = r + g + b;

                color = sum;

                bi.setRGB(x, y, color);

            }
        }

        return bi;

    }
    
    
    
    /**
     * 
     * @param image
     * @return new processed image
     */
    static BufferedImage spectrofilter(BufferedImage image) {
        int iw = image.getWidth();
        int ih = image.getHeight();

        BufferedImage bi = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_RGB);
        bi.createGraphics();

        // grass color
        Point3f colorg = new Point3f(0.4f, 0.9f, 0.4f);
        // road color
        Point3f colorr = new Point3f(0.7f, 0.6f, 0.7f);
        // bahno color
//        Point3f colorb = new Point3f(85, 85, 85);

        for (int y = 0; y < ih; y++) {
            for (int x = 0; x < iw; x++) {

                Point3f pix = new Point3f(new Color3f((new Color(image.getRGB(x, y)))));

                Point3f fin;

                if (pix.distance(colorg) < pix.distance(colorr)) {
                    fin = colorg;
                } else {
                    fin = colorr;
                }

                bi.setRGB(x, y, new Color3f(fin).get().getRGB());

            }
        }

        return bi;

    }

    
    public BufferedImage circles(BufferedImage input) {
        
        BufferedImage dest = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
        
        int x=0, y=0, i=0;
        
        for (y = 0; y < 255; y++) {
            for (x = 0; x < 255; x++) {
                int rgb = x*(y);
                dest.setRGB(x, y, 0xFF000000+rgb);
            }
        }
        
        return dest;
        
    }
    
    
    
    /**
     * 
     * @param image
     * @return
     */
    public static BufferedImage wowSoCloseSpectrograph(BufferedImage image) {
        int iw = image.getWidth();
        int ih = image.getHeight();

        BufferedImage bi = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_RGB);
        bi.createGraphics();

        for (int y = 0; y < ih; y++) {
            for (int x = 0; x < iw; x++) {

                int color = image.getRGB(x, y);

                int r = (color & 0xFF0000) >> 32;
                int g = (color & 0xFF00) >> 16;
                int b = (color & 0xFF);
                //                int sum = r+g+b;

                float c = 1;//255/(r+g+b+10);

                color = ((byte) (r * c) << 32) + ((byte) (g * c) << 16) + (byte) (b * c);

                bi.setRGB(x, y, color);

            }
        }

        return bi;

    }

    
    
    
}
