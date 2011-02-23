package videogame;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import vision.ColorTransform;

/**
 *
 * @author Kotuc
 */
public class ImageProcess extends ColorTransform {

    public static final int ATTACK_RED = 0xFFFF0000;
    public static final int BODY_BLUE = 0xFF0000FF;
    public static final int OPAQUE = 0xFF000000;
    public static final int TRANSPARENT = 0;
    /**
     * previous frame - to detect motion
     */
//    private BufferedImage previous;
    /**
     * the background - is filtered and so can be replaced with one more interresting
     */
    private BufferedImage staticBackground;
    /**
     * zones stores 
     * RED pixels - where player moves - he attacks
     * BLUE pixels - where player is - can be attacked
     */
    BufferedImage zones;

    /**
     * 
     * processes input that only object in foreground are visible 
     * other pixels are transparent
     * 
     *  
     * 
     * @param input
     * @return foreground
     */
    public BufferedImage process(BufferedImage input) {
        if (staticBackground == null) {
            throw new IllegalStateException("staticBackground == null");
        }
        BufferedImage foreground = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_INT_ARGB);
        if (zones == null) {
            zones = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_INT_ARGB);
        }
        foreground = apply(foreground, zones, input, staticBackground);
//            result = apply(result, previous, input, staticBackground);
        return foreground;// = zones;
    }

    @Override
    public void applyToBuffers(int index, DataBuffer... buffers) {
        // where player occures
        // difference input, background
        if (difference(buffers[2].getElem(index), buffers[3].getElem(index)) > 60) {
            // where player just moved
            if (0x000000 == buffers[1].getElem(index)) {
                // first touch
                buffers[1].setElem(index, ATTACK_RED);
            } else {
                // players body
                buffers[1].setElem(index, BODY_BLUE);
            }
            // zones - is player

            // set current color where player is
            buffers[0].setElem(index, OPAQUE | buffers[2].getElem(index));
        } else {
            // no player - transparent - background can be seen
//            return 0x00000000;
            buffers[1].setElem(index, TRANSPARENT);
        }

    }

    /**
     * is not used, because applyToBuffers is overridden
     * 
     * @param colors
     * @return
     */
    @Override
    public int getColor(int... colors) {
//
//        // where player occures
//        if (difference(colors[2], colors[3]) > 60) {
//            // where player moves
////            if (difference(colors[1], 0x000000) > 10) {
//            if ((ATTACK_RED != colors[1]) && (difference(colors[1], colors[2]) > 100)) {
//                return ATTACK_RED; // red - attack
//            } else {
//                return OPAQUE | colors[2]; // current color - contains player
//            }
//        } else {
//            // no player
//            return TRANSPARENT;
//        }
        return 0;
    }

    public int difference(int rgb1, int rgb2) {
        int r = Math.abs((rgb1 & 0xFF0000) - (rgb2 & 0xFF0000)) >> 16;
        int g = Math.abs((rgb1 & 0xFF00) - (rgb2 & 0xFF00)) >> 8;
        int b = Math.abs((rgb1 & 0xFF) - (rgb2 & 0xFF));
        return r + g + b;
    }

    public void setStaticBackground(BufferedImage staticBackground) {
        this.staticBackground = staticBackground;
    }
}
