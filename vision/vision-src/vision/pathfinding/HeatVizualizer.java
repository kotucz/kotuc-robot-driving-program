package vision.pathfinding;

/**
 *
 * @author Tomas Kotula
 */
public interface HeatVizualizer {
    
    /**
     * 
     * 
     * @param heat
     * @return ARGB color depending on the heat
     */
    int color(double heat);

}