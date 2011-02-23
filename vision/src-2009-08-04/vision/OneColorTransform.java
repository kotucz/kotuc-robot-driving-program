/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vision;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;

/**
 *
 * @author Kotuc
 */
public class OneColorTransform  {
    
    public BufferedImage apply(BufferedImage source, ColorFunc function) {
        return applyBuffer(source, function);
//        return applyGS(source, function);
//        return applyArray(source, function);
    }
       
    public BufferedImage applyBuffer(BufferedImage source, ColorFunc function) {
    
        DataBuffer buffer = source.getRaster().getDataBuffer();

        for (int i = 0; i < buffer.getSize(); i++) {
            buffer.setElem(i, function.getColor(buffer.getElem(i)));
        }
        
        return source;
    }
    
    public BufferedImage applyArray(BufferedImage source, ColorFunc function) {
        int iw = source.getWidth();
        int ih = source.getHeight();
        int length = iw*ih;
        int[] array = source.getRGB(0, 0, iw, ih, new int[length], 0, length);
        
        for (int i = 0; i < array.length; i++) {
            array[i] = function.getColor(array[i]);
        }
        
        source.setRGB(0, 0, iw, ih, new int[length], 0, length);
        return source;
    }
    
    /**
     * 
     * uses for y, x {  getRGB, setRGB }
     * 
     * @param source
     * @param function
     * @return
     */
    public BufferedImage applyGS(BufferedImage source, ColorFunc function) {
        int iw = source.getWidth();
        int ih = source.getHeight();
        
        for (int x = 0; x < iw; x++) {
            
            for (int y = 0; y < ih; y++) {
                
                source.setRGB(x, y, function.getColor(source.getRGB(x, y)));
                
            }

        }
            
        return source;
    }

 
    
}
