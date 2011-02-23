/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vision;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author Kotuc
 */
public class SphericalTransform {
    public static BufferedImage toSphere(BufferedImage src) {
        final int width = src.getWidth();
        final int height = src.getHeight();
        final int halfWidth = width/2;
        final int halfHeight = height/2;
        
        BufferedImage dest = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int x2 = (int)((Math.sin((double)(x-halfWidth)/halfWidth)+1)*halfWidth);
                int y2 = (int)((Math.sin((double)(y-halfHeight)/halfHeight)+1)*halfHeight);
                dest.setRGB(x, y, src.getRGB(x2, y2));                
            }
        }
        return dest;
    }
    
    public static BufferedImage fromSphere(BufferedImage src) {
        final int width = src.getWidth();
        final int height = src.getHeight();
        final int halfWidth = width/2;
        final int halfHeight = height/2;
        final int dist = (int)(width/0.9);
        
        BufferedImage dest = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // the angle that is viewed on the width of the dest image
                double a = (Math.PI/8)*(x-halfWidth)/halfWidth;
                double b = (Math.PI/10)*(y-halfHeight)/halfHeight;
                int x0 = (int)(Math.tan(a)*dist)+halfWidth;
                int y0 = (int)(Math.tan(b)*dist)+halfHeight;//y;//(int)((Math.sin((double)(y-halfHeight)/halfHeight)+1)*halfHeight);
                dest.setRGB(x, y, src.getRGB(x0, y0));
            }
        }

        Graphics g = dest.getGraphics();
        g.setColor(Color.GREEN);
// vertical lines        
        g.drawLine(width/2, 0, width/2, height);
        g.drawLine(width/3, 0, width/3, height);
        g.drawLine(2*width/3, 0, 2*width/3, height);
        g.drawLine(width/6, 0, width/6, height);
        g.drawLine(5*width/6, 0, 5*width/6, height);
// horizontal lines        
        g.drawLine(0, height/2, width, height/2);
        g.drawLine(0, height/3, width, height/3);
        g.drawLine(0, 2*height/3, width, 2*height/3);
        g.drawLine(0, height/6, width, height/6);
        g.drawLine(0, 5*height/6, width, 5*height/6);
        

        return dest;
    }
    
}
