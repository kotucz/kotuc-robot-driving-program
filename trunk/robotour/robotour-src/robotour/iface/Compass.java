package robotour.iface;

import robotour.*;
import robotour.navi.basic.Azimuth;

/**
 *
 * @author Kotuc
 */
public interface Compass {

    /**
     * Returns orientation in world in radians.
     * 0 is North
     * PI/2 East
     * PI South
     * 3*PI/2 West
     * 
     * @return azimuth in radians
     * @throws MeasureException if device is disconnected or read data is invalid
     */
    Azimuth getAzimuth() throws MeasureException;
}
