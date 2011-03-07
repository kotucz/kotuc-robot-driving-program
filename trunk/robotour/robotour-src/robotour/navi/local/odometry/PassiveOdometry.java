package robotour.navi.local.odometry;

import robotour.iface.MeasureException;
import robotour.iface.Compass;
import robotour.iface.Tachometer;
import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.navi.basic.Azimuth;

/**
 * Uses compass (Azimuzh) and Encoder to evaluate position
 * @author Kotuc
 */
public class PassiveOdometry extends OdometryBase implements Runnable {
    
    private final Tachometer encoder;
    private final Compass cmps;
    private static final int PERIOD = 30;
    /**
     * metres per sec when speed power is 1
     */
//    private static final double MAX_SPEED = 0.75; // mps
    private static final double MAX_SPEED = 1.5; // mps

    
    /**
     *
     * @param encoder
     * @param cmps
     */
    public PassiveOdometry(Tachometer encoder, Compass cmps) {
        if (encoder == null) {
            throw new NullPointerException("encoder");
        }
        if (cmps == null) {
            throw new NullPointerException("cmps");
        }
        this.encoder = encoder;
        this.cmps = cmps;
    }

    /**
     * Interpolation function to estimate speed from set power.
     * @param power
     * @return speed in m/s of moving with motors power
     */
    private static double powerToSpeed(double power) {
        return MAX_SPEED * power;
    }
    
    public void update(double speed, Azimuth azimuth, double seconds) {

//            double dx = speed * azimuth.sin() * ms / 1000.0;
//            double dy = speed * azimuth.cos() * ms / 1000.0;
//            point = new LocalPoint(point.getX() + dx, point.getY() + dy);

        double dist = speed * seconds;

        pose.move(azimuth, dist);
    }
    private long lastNano = System.nanoTime();

    public void run() {
        while (true) {
            try {
                long nowNano = System.nanoTime();
                Azimuth azimuth = cmps.getAzimuth();
                double speed = powerToSpeed(encoder.getSpeed());

                update(speed, azimuth, (nowNano - lastNano) / 1.0e9);

                lastNano = nowNano;

            } catch (MeasureException ex) {
                Logger.getLogger(PassiveOdometry.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                Thread.sleep(PERIOD);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void start() {
        new Thread(this).start();
    }
}
