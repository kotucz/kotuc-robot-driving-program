package vision.colors;

/**
 *
 * @author Kotuc
 */
public interface ColorFunc {
    
    /**
     * 
     * @param rgb
     * @return transformed color
     * @see ColorTransform
     */
    int getColor(int rgb);
    
}
