package vision.colors;

/**
 *
 * @author Tomas Kotula
 */
public interface HeatFunction {
    /** 
     * 
     * @param color AARRGGBB (hexadecimal) format
     * @return percentage [0..1] if color equals road color
     */
    float heat(int rgb);
}
