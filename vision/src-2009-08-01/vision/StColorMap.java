/*
 * ColorMap.java
 *
 */
package vision;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

/**
 *
 * @author Tomas Kotula
 */
public class StColorMap {
     
    
    static HashMap<Integer, Integer> table = new HashMap<Integer, Integer>();

    public BufferedImage getTable() {

        BufferedImage dest = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);

        int x=0, y=dest.getHeight()-1, i=0;

//        for (int code:table.keySet()) {
//            if (x>500) {
//                x=0;
//                y--;
//            }
//            dest.setRGB(x, y, toRGB(code));
//            x++;
//
//        }

//        int yyy0 = (int)(100*(1+Math.sqrt(3)));

        for (y = 0; y < 400; y++) {
            for (x = 0; x < 400; x++) {

                int rgb = Color.getHSBColor(
                        (float)(Math.atan2(200-y, x-200)/(2*Math.PI)),
                        (float)Math.hypot(x-200, y-200)/200f, 1).getRGB();

//                int rgb = ((0xFF&(int)(255*v3.x))<<16) + ((0xFF&(int)(255*v3.y))<<8) + (0xFF&(int)(255*v3.z));

//                if ((v3.x<0)||(v3.y<0)||(v3.z<0)) rgb = 0;
                final int value = getValue(rgb);
                if (value>30) {
                    dest.setRGB(x, y, 0xFFFF0000);
                } else if (value>5) {
                    dest.setRGB(x, y, 0xFF0000FF);
                } else {
                    dest.setRGB(x, y, 0xFF000000 + rgb);
                }
            }
        }

        Graphics2D g = (Graphics2D)dest.getGraphics();
        g.setColor(Color.WHITE);
        g.drawString(table.size()+" colors", 40, 400);
        return dest;

    }

    public BufferedImage getTableOld() {
        
        BufferedImage dest = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
        
        int x=0, y=dest.getHeight()-1, i=0;
        
        for (int code:table.keySet()) {
            if (x>500) {
                x=0;
                y--;
            }
            dest.setRGB(x, y, toRGB(code));
            x++;
            
        }
        
        int yyy0 = (int)(100*(1+Math.sqrt(3)));
        
        for (y = 0; y < 400; y++) {
            for (x = 0; x < 400; x++) {
                
                Vector2f vr = new Vector2f(x-100, y-100);
                Vector2f vg = new Vector2f(x-300, y-100);
                Vector2f vb = new Vector2f(x-200, y-yyy0);
                
                Vector3f v3 = new Vector3f(200-vr.length(), 200-vg.length(), 200-vb.length());
                v3.normalize();
                
//                Color3f cv = new Color3f(v3);
                
                int rgb = ((0xFF&(int)(255*v3.x))<<16) + ((0xFF&(int)(255*v3.y))<<8) + (0xFF&(int)(255*v3.z));
                
//                if ((v3.x<0)||(v3.y<0)||(v3.z<0)) rgb = 0;
                
                if (getValue(rgb)>3) {
                    dest.setRGB(x, y, 0xFFFF0000);
                } else if (getValue(rgb)>0) {
                    dest.setRGB(x, y, 0xFF0000FF);
                } else {
                    dest.setRGB(x, y, 0xFF000000 + rgb);
                }
            }
        }
        
        Graphics2D g = (Graphics2D)dest.getGraphics();
        g.setColor(Color.WHITE);
        g.drawString(table.size()+" colors", 40, 400);
        return dest;
        
    }
    
    static int getValue(int color) {
        int code = code(color);
        if (table.containsKey(code)) {
            return table.get(code);
        } else {
            return 0;
        }
    }
    
    static void add(int color) {
        int code = code(color);
        table.put(code, getValue(color)+1);
    }
    
    static int code(int rgb) {
        rgb = Spectrograph.toSpectralColor(new Color(rgb)).getRGB();
//        return rgb;
        return rgb&0x00FEFEFE;
//        return rgb&0x00FCFCFC;
//          return rgb&0x00F0F0F0;
    }
    
    static int toRGB(int code) {
        return code;
    }
    
    public static void load(BufferedImage bi) {
        for (int y = 0; y < bi.getHeight(); y++) {
            for (int x = 0; x < bi.getWidth(); x++) {
                int rgb = Spectrograph.toSpectralColor(new Color(bi.getRGB(x, y))).getRGB();
                if ((0xFFFFFF&rgb) > 0) {
                    add(rgb);
                }
            }
            
        }
        
    }
    
}
