package robotour.pathing.simple;

/**
 *
 * @author Kotuc
 */
public class DiffWheelParameters {

    /**
     * The distance between left and right wheel.
     */
    final double wheelGauge = 0.3;
    final double oneWheelGauge = wheelGauge/2.0;
    /**
     * Number of encoder units per one distance units (i.e. meters).
     */
    final double tickspm = 10000.0;
    final double mpticks = 1.0 / tickspm;

    /**
     * Diametral size of wheel in units (i.e. metres)
     */
    final double wheelDiameter = 0.115; // metres
    /**
     * Distance of the sum of wheel distances with opposite signs to make full 360 revolution.
     */
    final double fullRevolution = wheelGauge * Math.PI;
    
    //    final int PERIOD = 30;
    
//    final double WHEEL_TRACK = 0.23; // metres
//    final double WHEEL_FRONT_BACK_DIST = 0.13; // metres
//    final double WHEEL_CIRCLE_RADIUS = Math.sqrt(Math.pow(WHEEL_TRACK / 2.0, 2) + Math.pow(WHEEL_FRONT_BACK_DIST / 2.0, 2));
    /**
     * metres per sec when speed power is 1
     */
//    final double MAX_SPEED = 0.9; // mps
//    private static final double MAX_SPEED = 1.5; // mps
    /**
     * radians per sec when steer power is 1
     */
//    final double MAX_ANGULAR_SPEED = 0.91 * (/*0.5 **/WHEEL_FRONT_BACK_DIST / WHEEL_CIRCLE_RADIUS) / WHEEL_CIRCLE_RADIUS; // rads per sec
}
