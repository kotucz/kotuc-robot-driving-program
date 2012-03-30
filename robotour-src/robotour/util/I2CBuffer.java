package robotour.util;

import robotour.iface.Bufferable;
import robotour.iface.Compass;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.hardware.CMPS03;
import robotour.hardware.I2CUSB;
import robotour.hardware.SRF08;
import robotour.rt.RealTime;

/**
 * This class permanently measures the distance of each sonar and compass azimuth
 * so the data are available immediately
 *
 * @author Kotuc
 */
public class I2CBuffer implements Runnable {

    private List<Bufferable> bufferables = new ArrayList<Bufferable>();
    private Sonars sonars;
    private Compass compass;

    public I2CBuffer(I2CUSB i2c) {
        this.sonars = new Sonars(
                new SonarBuffer(new SRF08(i2c, 0xE2)),
                new SonarBuffer(new SRF08(i2c, 0xE0)),
                new SonarBuffer(new SRF08(i2c, 0xE4)));
        this.compass = new CompassBuffer(new CMPS03(i2c));
        bufferables.add((Bufferable) sonars.getLeft());
        bufferables.add((Bufferable) sonars.getCenter());
        bufferables.add((Bufferable) sonars.getRight());
        bufferables.add((Bufferable) this.compass);
    }

    public static I2CBuffer createBuffer(I2CUSB i2c) {
        I2CBuffer buffer = new I2CBuffer(i2c);
        return buffer;
    }

    public Compass getCompass() {
        return compass;
    }

    public Sonars getSonars() {
        return sonars;
    }

    public void startPollingThread() {
        RealTime.newThread(this, 20).start();
    }

    public void pollAll() {
        for (Bufferable bufferable : bufferables) {
            bufferable.readToBuffer();
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(I2CBuffer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void run() {
        while (true) {
//            long t = System.nanoTime();
//            for (Bufferable bufferable : bufferables) {
//                bufferable.readToBuffer();
//                try {
//                    Thread.sleep(10);
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(I2CBuffer.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
            pollAll();
//            System.out.println("read " + (System.nanoTime() - t)+" ns");
//            System.out.println("" + compass + " " + sonars);
        }
    }
}
