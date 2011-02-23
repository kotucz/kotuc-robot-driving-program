package robotour.hardware;

import java.io.IOException;
import robotour.iface.Compass;
import robotour.iface.MeasureException;
import robotour.navi.gps.Azimuth;

/**
 *
 * @author Kotuc
 */
public class CMPS03 implements Compass {

    private final I2CUSB.I2CDevice dev;

    public CMPS03(I2CUSB i2c) {
        this.dev = i2c.getDevice(0xC0);
    }

    public synchronized Azimuth getAzimuth() throws MeasureException {
        synchronized (dev.getBus()) {
            try {
                return Azimuth.valueOfDegrees(dev.read(0x02) * 0.1);
            } catch (IOException ex) {
                throw new MeasureException(ex);
            }
        }
    }
}
