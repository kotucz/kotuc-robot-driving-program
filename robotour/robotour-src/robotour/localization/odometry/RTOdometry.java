package robotour.localization.odometry;

import robotour.iface.Wheels;

/**
 * Determinates position and orientation from speed and steer setting.
 * Uses informations about robot speeds, wheels and proportions.
 * Plug this on wheels to deadreckon.
 * @author Kotuc
 */
public class RTOdometry  extends OdometryBase implements Wheels {
    
    
    /**
     * metres per sec when speed power is 1
     */
    final double MAX_SPEED = 0.9; // mps
//    private static final double MAX_SPEED = 1.5; // mps
    /**
     * radians per sec when steer power is 1
     */
    final double MAX_ANGULAR_SPEED = 0.91;

//    private List<Point> track = new LinkedList<Point>();
//    private Azimuth azimuth = Azimuth.valueOfRadians(0.0);
    private double lastSteer = 0;
    private double lastSpeed = 0;
    private final Wheels wheels;

    public RTOdometry(Wheels wheels) {
        this.wheels = wheels;
    }

    public void setSteer(double steer) {
        wheels.setSteer(steer);
        update();
        this.lastSteer = steer;
    }

    public void setSpeed(double speed) {
        wheels.setSpeed(speed);
        update();
        this.lastSpeed = speed;
    }

    public void stop() {
        wheels.stop();
        update();
        this.lastSpeed = 0;
        this.lastSteer = 0;
    }

    /**
     * Interpolation function to estimate speed from set power.
     * @param power
     * @return speed in m/s of moving with motors power
     */
    public double powerToSpeed(double power) {
        return MAX_SPEED * power;
    }

    /**
     * Interpolation function to estimate speed from set power.
     * @param steerPower
     * @return speed in m/s/s of moving with motors power
     */
    public double powerToAngularSpeed(double steerPower) {
        return MAX_ANGULAR_SPEED * steerPower;
    }
    private long lastNano = System.nanoTime();

    private void update() {

        long nowNano = System.nanoTime();
        final double interval = (nowNano - lastNano) / 1.0e9;

        double rotation = powerToAngularSpeed(this.lastSteer) * interval;

        this.lastNano = nowNano;

        double dist = lastSpeed * interval;

        pose.doMoveFwRight(dist, rotation);

//        pose.point = pose.getPoint().move(Azimuth.valueOfRadians(azimuth.radians() + 0.5 * rotation), dist);

//        this.azimuth = Azimuth.valueOfRadians(azimuth.radians() + rotation);

    }
}
