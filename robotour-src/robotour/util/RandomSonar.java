package robotour.util;

import robotour.iface.MeasureException;
import robotour.iface.Sonar;

/**
 *
 * @author Tomas
 */
public class RandomSonar implements Sonar {

    public double getDistance() throws MeasureException {
        return Math.random();
    }

}
