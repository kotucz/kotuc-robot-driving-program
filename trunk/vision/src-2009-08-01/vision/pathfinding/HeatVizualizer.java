package vision.pathfinding;

/**
 *
 * @author Tomas Kotula
 */
interface HeatVizualizer {
    
    /**
     * 
     * 
     * @param heat
     * @return ARGB color depending on the heat
     */
    int color(double heat);

}