package robotour.hardware.arduino;

import robotour.iface.MeasureException;
import robotour.iface.Sonar;

/**
 *
 * @author Kotuc
 */
public class ArdSonar implements Sonar {

    // cm
    private int lastcm;

    public double getDistance() throws MeasureException {
        if (lastcm<0) {
            throw new MeasureException("negative value: "+lastcm);
        }
        return lastcm/100.0;
    }

    void measuredCM(int cm) {
        this.lastcm = cm;
    }

    


}
