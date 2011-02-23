package robotour.util.log.events;

import robotour.iface.Compass;
import robotour.navi.gps.Azimuth;

/**
 *
 * @author Kotuc
 */
public class CompassEvent extends ArduinoEvent implements Compass {

    private final Azimuth azimuth;

    public CompassEvent(Azimuth azimuth, int arduinoTime, long time) {
        super(EventType.compass, arduinoTime, time);
        this.azimuth = azimuth;
    }

    public static CompassEvent createCompassEvent10deg(short azimuth10deg, int arduinoTime, long time) {
        return new CompassEvent(Azimuth.valueOfDegrees(azimuth10deg / 10.0), arduinoTime, time);
    }

    public Azimuth getAzimuth() {
        return azimuth;
    }
}
