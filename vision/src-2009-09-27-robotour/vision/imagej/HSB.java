package vision.imagej;

import ij.ImagePlus;
import ij.process.ColorProcessor;
import java.awt.image.BufferedImage;

/**
 *
 * @author Kotuc
 */
public class HSB {

    public static BufferedImage pathTreshold(BufferedImage image) {
                ColorProcessor pcsr = new ColorProcessor(image);


        pcsr.autoThreshold();

        pcsr.filter(ColorProcessor.RGB_THRESHOLD);

        return pcsr.getBufferedImage();

    }
    
    public static BufferedImage rgbAutoTreshold(BufferedImage image) {
//        ImagePlus ip = new ImagePlus("Title954", image);
//        ip.setProcessor("pro5356", new ColorProcessor(image));

        ColorProcessor pcsr = new ColorProcessor(image);

        pcsr.autoThreshold();

        pcsr.filter(ColorProcessor.RGB_THRESHOLD);

        return pcsr.getBufferedImage();

    }
}
