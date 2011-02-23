package vision.pathfinding;

/**
 *
 * @author Tomas Kotula
 */
interface HeatFunction {
    /** 
     * 
     * @param color
     * @return percentage [0..1] if color equals road color
     */
    float heat(int color);
}
