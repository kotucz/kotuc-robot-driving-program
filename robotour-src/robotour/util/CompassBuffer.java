package robotour.util;

import robotour.iface.MeasureException;
import robotour.iface.Bufferable;
import robotour.iface.Compass;
import robotour.navi.basic.Azimuth;

/**
 *
 * @author Tomas
 */
public class CompassBuffer implements Compass, Bufferable {

    private final Compass compass;

    // volatile so another thread can read updated information
    private volatile Azimuth azimuth;
    private volatile MeasureException error;

    public CompassBuffer(Compass srf) {
        this.compass = srf;
    }

    public synchronized void readToBuffer() {
        try {
            this.azimuth = compass.getAzimuth();
            this.error = null;
        } catch (MeasureException ex) {
            this.error = ex;
        }
    }

    public synchronized Azimuth getAzimuth() throws MeasureException {
        if (error != null) {
            throw error;
        }
        return azimuth;
    }

    @Override
    public String toString() {
        return "Cmps(" + azimuth + ")";
    }
}
