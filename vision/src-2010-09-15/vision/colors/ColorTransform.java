package vision.colors;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;

/**
 *
 * @author Kotuc
 */
public abstract class ColorTransform {
        
    /**
     * applies getColor() function to images and stores the result color 
     * into the one at first resp. 0 position of parameters
     * 
     * apply(image1) transforms image1 into getColor(image1)
     * 
     * the tranform is invariant on pixel position on image.
     * however, these are not changed
     * 
     * @param images
     * @return
     */
    public BufferedImage apply(BufferedImage ... images) {
//        BufferedImage result = new BufferedImage(images[0].getWidth(), images[0].getHeight(), images[0].getType());
//        result.getRaster();
        int numi = images.length;        
        DataBuffer[] buffers = new DataBuffer[numi];
        for (int i = 0; i < numi; i++) {
            buffers[i] = images[i].getRaster().getDataBuffer();
        }
        
        for (int i = 0; i < buffers[0].getSize(); i++) {
            applyToBuffers(i, buffers);
        }
                     
        return images[0];
    }
    
    /**
     * 
     * @param index
     * @param buffers
     */
    public void applyToBuffers(int index, DataBuffer ... buffers) {
        int numi = buffers.length;
        int[] colors = new int[numi];
        for (int j = 0; j < numi; j++) {
            colors[j] = buffers[j].getElem(index);
        }
        buffers[0].setElem(index, getColor(colors));
    }
    
    /**
     * 
     * @param colors
     * @return 
     */
    public abstract int getColor(int ... colors);
    
}
