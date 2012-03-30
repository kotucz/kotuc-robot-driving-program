package robotour.util.log.events;

/**
 *
 * @author Kotuc
 */
public class WheelCommandEvent extends Event {

    final double speed;
    final double steer;

    public WheelCommandEvent(double speed, double steer, long time) {
        super(EventType.wheels, time);
        this.speed = speed;
        this.steer = steer;
    }

    public double getSpeed() {
        return speed;
    }

    public double getSteer() {
        return steer;
    }

}
