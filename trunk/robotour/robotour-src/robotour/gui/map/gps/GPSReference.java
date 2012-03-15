package robotour.gui.map.gps;

import robotour.navi.basic.LocalPoint;
import robotour.navi.gps.GPSPoint;

/**
 * 
 * Linearization of GPS coordinates near to specified zeroGPS point.
 *
 * Transforms to meters.
 *
 * @author Kotuc
 */
public class GPSReference {

    final GPSPoint zeroGPS;
    /**
     * Earth radius constant in metres.
     */
    static final double EARTH_RADIUS = 6378000.0;
    final double metersPerLatitudeRadian = EARTH_RADIUS;
    // earth radius at longitude 49.2
    // cos(Latitude)*Earth radius
    final double metersPerLongitudeRadian;

    public GPSReference(GPSPoint zeroGPS) {
        this.zeroGPS = zeroGPS;

        // earth radius at longitude 49.2
//        metersPerLongitudeRadian = 4159804.7617246843651082125283963;
        metersPerLongitudeRadian = zeroGPS.latitude().cos()*EARTH_RADIUS;
    }

    public LocalPoint toLocal(GPSPoint p) {
        return new LocalPoint(
                metersPerLongitudeRadian * (p.longitude().radians() - zeroGPS.longitude().radians()),
                metersPerLatitudeRadian * (p.latitude().radians() - zeroGPS.latitude().radians()));
    }
}
