package robotour.pathing.simple;

import robotour.navi.basic.RobotPose;

/**
 * Created by IntelliJ IDEA.
 * User: Kotuc
 * Date: 25.3.12
 * Time: 20:06
 * To change this template use File | Settings | File Templates.
 *
 *
 *
 */
class DiffMovement extends Movement {


    final double leftspeed;
    final double rightspeed;
    final double leftacc;
    final double rightacc;
    final long durationms;

    public DiffMovement(double leftspeed, double rightspeed, double leftacc, double rightacc, long duration) {
        this.leftspeed = leftspeed;
        this.rightspeed = rightspeed;

        this.leftacc = leftacc;
        this.rightacc = rightacc;

        this.durationms = duration;
    }


}
