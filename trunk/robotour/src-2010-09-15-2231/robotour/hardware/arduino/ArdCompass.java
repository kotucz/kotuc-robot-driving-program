package robotour.hardware.arduino;

import robotour.iface.Compass;
import robotour.iface.MeasureException;
import robotour.navi.gps.Azimuth;

/**
 *
 * @author Kotuc
 */
public class ArdCompass implements Compass {

    // degrees*10
    private int degree10;

    void measuredDegTen(int degten) {
        this.degree10 = degten;
    }

    public Azimuth getAzimuth() throws MeasureException {
        if (degree10 < 0) {
            throw new MeasureException("negative value: " + degree10);
        }
        return Azimuth.valueOfDegrees(degree10 / 10.0);
    }
}
