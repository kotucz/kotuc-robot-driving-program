package robotour.pathing.simple;

import robotour.navi.basic.RobotPose;

/**
 * Created by IntelliJ IDEA.
 * User: Kotuc
 * Date: 25.3.12
 * Time: 20:04
 * To change this template use File | Settings | File Templates.
 *
 * Class describing movement of a robot.
 * TODO make more abstract for different robots
 *
 * Is change of Position (RobotPose) - i.e. its derivation
 *
 */
public class Movement {

    Speed velocity;
    Speed angular;

    public RobotPose transformation(RobotPose origin) {
        return new RobotPose();
    }

    /**
     *
     * @param time time since start of this movement
     */
    Movement getMovementAt(double time) {

    }

}
