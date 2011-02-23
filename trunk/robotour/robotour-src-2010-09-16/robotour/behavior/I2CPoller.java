package robotour.behavior;

import robotour.util.I2CBuffer;

/**
 *
 * @author Kotuc
 */
public class I2CPoller implements Behavior {

    private final I2CBuffer i2cBuffer;

    public I2CPoller(I2CBuffer i2cBuffer) {
        this.i2cBuffer = i2cBuffer;
    }

    public boolean act() {
        i2cBuffer.pollAll();
        return false; // allow continuing with errors
//        try {
//
//            i2cBuffer.getCompass().getAzimuth();
//            for (Sonar sonar : i2cBuffer.getSonars().getSonars()) {
//                sonar.getDistance();
//            }
//            return false; // let program continue
//        } catch (MeasureException ex) {
//            Logger.getLogger(I2CPoller.class.getName()).log(Level.SEVERE, null, ex);
//            Thread.sleep(1000); // recover
//            return true; // error program loop over
//        }
    }
}