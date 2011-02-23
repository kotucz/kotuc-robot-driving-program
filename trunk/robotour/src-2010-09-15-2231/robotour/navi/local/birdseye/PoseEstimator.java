package robotour.navi.local.birdseye;

import robotour.navi.gps.Azimuth;
import robotour.navi.local.beacons.RobotPose;

/**
 *
 * @author Kotuc
 */
public class PoseEstimator {

    double speedConst = 1;
    double steerConst = 50;

    //  speed -1 .. 1, steer -1 .. 1, time seconds
    RobotPose transform(RobotPose start, double speed, double steer, double time) {

        RobotPose pose = new RobotPose(start);
        while (time > 0) {

            double dt = time;

            time = dt - 0.1;

            if (dt > 0.1) {
              dt -= time;
            }            

            double azim0deg = pose.getAzimuth().degrees();
            // tangent (tecna) azimuth
            double azimhdeg = azim0deg + 0.5 * steer * steerConst * dt;
            // end azimuth
            double azim1deg = azim0deg + steer * steerConst * dt;

            double dist = speed * speedConst * dt;

            pose = new RobotPose(
                    pose.getPoint().move(Azimuth.valueOfDegrees(azimhdeg), dist),
                    Azimuth.valueOfDegrees(azim1deg));
        }
        return pose;
    }

    

}
