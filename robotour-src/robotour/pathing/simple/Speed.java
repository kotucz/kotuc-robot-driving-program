package robotour.pathing.simple;

/**
 * Created by IntelliJ IDEA.
 * User: Kotuc
 * Date: 25.3.12
 * Time: 22:19
 * To change this template use File | Settings | File Templates.
 */
public class Speed {
    double startVelocity;
    // acc hold for entire segment
    double acceleration; // dependence with velocity

    public Speed(double startVelocity, double acceleration) {
        this.startVelocity = startVelocity;
        this.acceleration = acceleration;
    }

    double getVelocityAt(double atTimeSeconds) {
        return startVelocity + acceleration * atTimeSeconds;
    }

    double getDistanceAt(double atTimeSeconds) {
        return (startVelocity + 0.5 * acceleration * atTimeSeconds) * atTimeSeconds;
    }

    // time when max speed is reached
    double getTimeToAchieveVelocity(double targetVelocity) {
        return (targetVelocity - startVelocity) / acceleration;
    }

    Speed getSpeedAt(double atTimeSeconds)  {
        return new Speed(getVelocityAt(atTimeSeconds), acceleration);
    }

}
