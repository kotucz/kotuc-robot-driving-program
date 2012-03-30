package robotour.pathing.simple;

import robotour.navi.basic.Azimuth;
import robotour.navi.basic.Point;
import robotour.navi.basic.Pose;

/**
 * Created by IntelliJ IDEA.
 * User: Kotuc
 * Date: 25.3.12
 * Time: 20:04
 * To change this template use File | Settings | File Templates.
 * <p/>
 * Class describing movement of a robot.
 * TODO make more abstract for different robots
 * <p/>
 * Is change of Position (Pose) - i.e. its derivation
 */
public class Movement {

    Speed velocity;
    Speed angular;

    public Movement(Speed velocity, Speed angular) {
        this.angular = angular;
        this.velocity = velocity;
    }

    /**
     * @param time time since start of this movement
     */
    Movement getMovementAt(double time) {
        return new Movement(velocity.getSpeedAt(time), angular.getSpeedAt(time));
    }

    public Pose transformation(final Pose start, final double time) {

        // numerical calculation

        // TODO iterate by max angle steps rather than time steps
        Pose pose = new Pose(start);

        final double startazimrads = start.getAzimuth().radians();

        double t1 = 0;

        final double dt = 0.1;

        Point loc = pose.getPoint();


        while (t1 < time) {

            // segment from time t1 to t2
            
            double t2 = t1 + dt;            

            if (time < t2) {
                t2 = time; // TODO ensure this ends after this loop
            }

            double halfsteptime = (t1 + t2)/2.;

            // TODO this might not be accurate for angular acceleration constant velocities vere assumed

            // tangent (tecna) azimuth
            double azimhalfrad = startazimrads + angular.getDistanceAt(halfsteptime);

            double dist = velocity.getDistanceAt(t2) - velocity.getDistanceAt(t1);

            // TODO tangent correction
            loc = loc.move(Azimuth.valueOfRadians(azimhalfrad), dist);

            t1 = t2;


        }



        pose = new Pose(
                loc, Azimuth.valueOfRadians(startazimrads+angular.getDistanceAt(time)));

//        pose.setAzimuth(start.getAzimuth().radians());
        return pose;
    }

}
