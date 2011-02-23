/*
 * Comparator.java
 *
 * 
 */

package vision;

import vision.colors.Spectrograph;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author Tomas Kotula
 */
public class ImageComparator {
 
    public static BufferedImage compare(BufferedImage image1, BufferedImage image2) {
        image1 = Spectrograph.spectrograph(image1);
        image2 = Spectrograph.spectrograph(image2);
        BufferedImage dest = new BufferedImage(image1.getWidth(), image1.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        int percent = 0;
        int samples = 0;
        for (int y = 0; y < image2.getHeight(); y++) {
            for (int x = 0; x < image2.getWidth(); x++) {
                int rgb = image1.getRGB(x, y);
                
                int r1 = (rgb>>16)&0xFF;
                int g1 = (rgb>>8)&0xFF;
                int b1 = (rgb>>0)&0xFF;
                
                rgb = image2.getRGB(x, y);
                
                int r2 = (rgb>>16)&0xFF;
                int g2 = (rgb>>8)&0xFF;
                int b2 = (rgb>>0)&0xFF;
                
                int r = (r1+r2)/2;
                int g = (g1+g2)/2;
                int b = (b1+b2)/2;
                
                int p = Math.abs(r1-r2)+Math.abs(g1-g2)+Math.abs(b1-b2);
                
                percent+=p;                        
                        
                samples+=3;
                
                rgb = (((r+p)&0xFF)<<16)+((g&0xFF)<<8)+(b&0xFF);
                               
                dest.setRGB(x, y, rgb);
                
//                int rgb = image1.getRGB(x, y)^image2.getRGB(x, y);
//                 dest.setRGB(x, y, rgb);
//                percent+=(((r)>>16)&0xFF)+((g>>8)&0xFF)+((b>>0)&0xFF);
//                samples+=3;
            }
        }
        Graphics2D g = (Graphics2D)dest.getGraphics();
        g.setColor(Color.RED);
        g.drawString((percent/samples)+"%", image1.getWidth()/2, image1.getHeight()/2);
        return dest;
    }
    
}
