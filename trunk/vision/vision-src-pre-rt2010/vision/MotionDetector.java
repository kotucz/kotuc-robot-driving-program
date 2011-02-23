package vision;

import vision.colors.ColorTransform;
import java.awt.image.BufferedImage;

/**
 *
 * @author Kotuc
 */
public class MotionDetector extends ColorTransform {

    private BufferedImage previous;

    private int treshold = 120;
    
    /**
     * 
     * compares image to second parameter
     * marks red all changes over specific treshold 
     * 
     * @param image
     * @param previous 
     * @return the image with Color.RED highlited motion
     */
    public BufferedImage getMotion(BufferedImage image, BufferedImage previous) {

        return apply(image, previous);
        
//        DataBuffer imagedb = image.getRaster().getDataBuffer();
//        DataBuffer prevdb = previous.getRaster().getDataBuffer();
//
//        for (int i = 0; i < imagedb.getSize(); i++) {
//            float d = compare(imagedb.getElem(i), prevdb.getElem(i));
//
//            if (d > treshold) {
//                imagedb.setElem(i, 0xFF0000);
//            }
//        }
//        return image;        

//            int iw = image.getWidth();
//            int ih = image.getHeight();
//
//            for (int x = 0; x < iw; x++) {
//                for (int y = 0; y < ih; y++) {
////                int i = bi.getRGB(x, y);
////                int r = (i&0xFF0000)>>16;
////                int g = (i&0xFF00)>>8;
////                int b = i&0xFF;
//////                i = Math.min((int)(7*Math.sqrt(Math.pow(r-g, 2)+Math.pow(r-b, 2)+Math.pow(b-g, 2)))250);//0x010101*(256/(r+g+b));
////                i = (20<Math.sqrt(Math.pow(r-g, 2)+Math.pow(r-b, 2)+Math.pow(b-g, 2)))?255:0xFF0000;
////                bi.setRGB(x, y, i);
//
////                    int d = Math.abs((previous.getRGB(x, y) & 0xFF) - (image.getRGB(x, y) & 0xFF));
//
//                    float d = compare(image.getRGB(x, y), previous.getRGB(x, y));
//                    
//                    if (d > 120) {
//                        image.setRGB(x, y, 0xFF0000);
//                    }
//
//                }
//            }
        
//        return image;
    }

    public int getColor(int ... colors) {
        float d = compare(colors[0], colors[1]);

        if (d > treshold) {
            return 0xFF0000; // red
        } else {
            return colors[0]; // original
        }
    }
    
    /**
     * 
     * compares image to the one or more previous used with this method
     * marks red all changes over specific treshold 
     * 
     * @param image
     * @return the image with Color.RED highlited motion
     */
    public BufferedImage getMotion(BufferedImage image) {

        BufferedImage copy = BasicOps.copy(image);

        if (previous != null) {
            image = getMotion(image, previous);
        }
        previous = copy;

        return image;

    }

    int compare(int rgb1, int rgb2) {
        int r = Math.abs((rgb1 & 0xFF0000) - (rgb2 & 0xFF0000)) >> 16;
        int g = Math.abs((rgb1 & 0xFF00) - (rgb2 & 0xFF00)) >> 8;
        int b = Math.abs((rgb1 & 0xFF) - (rgb2 & 0xFF));
        return r + g + b;
    }

    public void setTreshold(int treshold) {
        this.treshold = treshold;
    }
    
//    private BufferedImage detectMotion(BufferedImage nbi) {
//
//        int iw = nbi.getWidth();
//        int ih = nbi.getHeight();
//
//        for (int x = 0; x < iw; x++) {
//            for (int y = 0; y < ih; y++) {
//                //                int i = bi.getRGB(x, y);
////                int r = (i&0xFF0000)>>16;
////                int g = (i&0xFF00)>>8;
////                int b = i&0xFF;
//////                i = Math.min((int)(7*Math.sqrt(Math.pow(r-g, 2)+Math.pow(r-b, 2)+Math.pow(b-g, 2)))250);//0x010101*(256/(r+g+b));
////                i = (20<Math.sqrt(Math.pow(r-g, 2)+Math.pow(r-b, 2)+Math.pow(b-g, 2)))?255:0xFF0000;
////                bi.setRGB(x, y, i);
//
//                int d = Math.abs((previous.getRGB(x, y) & 0xFF) - (nbi.getRGB(x, y) & 0xFF));
//
//                if (d > 120) {
//                    nbi.setRGB(x, y, 0xFF0000);
//                }
//
//            }
//        }
//
//        return nbi;
//    }
    }
