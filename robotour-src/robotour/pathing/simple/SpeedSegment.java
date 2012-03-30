package robotour.pathing.simple;

/**
 * Created by IntelliJ IDEA.
 * User: Kotuc
 * Date: 25.3.12
 * Time: 21:01
 * To change this template use File | Settings | File Templates.
 *
 * Is a derivation of 1D trajectory. For one wheel
 */
public class SpeedSegment extends Speed {


//    float startTime = 0;
//    float endTime = ...;
    double duration;

//    double startDistance = 0; // always
//    double endDistance = ...;

    SpeedSegment(double startVelocity, double acceleration, double duration) {
        super(startVelocity, acceleration);
        this.duration = duration;

    }

    public double getEndVelocity() {
        return getVelocityAt(duration);
    }

    public double getDistance() {
        return getDistanceAt(duration);
    }





}
