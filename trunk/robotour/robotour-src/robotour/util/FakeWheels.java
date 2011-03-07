package robotour.util;

import robotour.iface.MeasureException;
import robotour.iface.Wheels;
import robotour.iface.Compass;
import robotour.iface.Tachometer;
import robotour.navi.basic.Azimuth;
import robotour.navi.local.odometry.RTOdometry;

/**
 *
 * @author Tomas
 */
public class FakeWheels implements Wheels/*, Encoder, Compass*/ {

    private double speed;
    private double steer;
    private final Wheels wheels;

    public FakeWheels() {
        wheels = null;
    }

    public FakeWheels(Wheels wheels) {
        this.wheels = wheels;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
        if (wheels == null) {
//            System.out.println("no speed: " + speed);
        } else {
            wheels.setSpeed(speed);
        }
    }
//    private long lastSteerNano = System.currentTimeMillis();

    public void setSteer(double steer) {
//        long nowNano = System.nanoTime();
//        this.azimuth = Azimuth.valueOfRadians(azimuth.radians() +
//                Odometry.powerToAngularSpeed(this.steer) * (nowNano - lastSteerNano) / 1.0e9);
//        this.lastSteerNano = nowNano;
        this.steer = steer;
        if (wheels == null) {
//            System.out.println("no steer: " + steer);
        } else {
            wheels.setSteer(steer);
        }
    }

    public double getSpeed() {
        return this.speed;
    }

    public void stop() {
        this.speed = 0;
        this.steer = 0;
        if (wheels == null) {
            System.out.println("No wheels: no stop.");
        } else {
            wheels.stop();
        }
    }

//    public Azimuth getAzimuth() throws MeasureException {
//        return this.azimuth;
//    }
}
