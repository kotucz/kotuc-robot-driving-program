package robotour.navi.local.odometry;

import robotour.iface.Wheels;

/**
 * Determinates position and orientation from speed and steer setting.
 * Uses informations about robot speeds, wheels and proportions.
 * Plug this on wheels to deadreckon.
 * @author Kotuc
 */
public class RTOdometry  extends OdometryBase implements Wheels {
    
    private static final int PERIOD = 30;
    private static final double WHEEL_DIAMETER = 0.115; // metres
    private static final double WHEEL_TRACK = 0.23; // metres
    private static final double WHEEL_FRONT_BACK_DIST = 0.13; // metres
    private static final double WHEEL_CIRCLE_RADIUS = Math.sqrt(Math.pow(WHEEL_TRACK / 2.0, 2) + Math.pow(WHEEL_FRONT_BACK_DIST / 2.0, 2));
    /**
     * metres per sec when speed power is 1
     */
    private static final double MAX_SPEED = 0.9; // mps
//    private static final double MAX_SPEED = 1.5; // mps
    /**
     * radians per sec when steer power is 1
     */
    private static final double MAX_ANGULAR_SPEED = 0.91 * (/*0.5 **/WHEEL_FRONT_BACK_DIST / WHEEL_CIRCLE_RADIUS) / WHEEL_CIRCLE_RADIUS; // rads per sec
//    private List<LocalPoint> track = new LinkedList<LocalPoint>();
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
    public static double powerToSpeed(double power) {
        return MAX_SPEED * power;
    }

    /**
     * Interpolation function to estimate speed from set power.
     * @param steerPower
     * @return speed in m/s/s of moving with motors power
     */
    public static double powerToAngularSpeed(double steerPower) {
        return MAX_ANGULAR_SPEED * steerPower;
    }
    private long lastNano = System.nanoTime();

    private void update() {

        long nowNano = System.nanoTime();
        final double interval = (nowNano - lastNano) / 1.0e9;

        double rotation = RTOdometry.powerToAngularSpeed(this.lastSteer) * interval;

        this.lastNano = nowNano;

        double dist = lastSpeed * interval;

        pose.doMoveFwRight(dist, rotation);

//        pose.point = pose.getPoint().move(Azimuth.valueOfRadians(azimuth.radians() + 0.5 * rotation), dist);

//        this.azimuth = Azimuth.valueOfRadians(azimuth.radians() + rotation);

    }
}