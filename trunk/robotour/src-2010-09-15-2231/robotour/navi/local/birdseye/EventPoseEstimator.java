package robotour.navi.local.birdseye;

import java.awt.Color;
import robotour.gui.map.LocalPath;
import robotour.navi.local.beacons.RobotPose;
import robotour.util.log.events.WheelCommandEvent;

/**
 *
 * @author Kotuc
 */
public class EventPoseEstimator {

    public LocalPath track = new LocalPath();
    RobotPose pose;
    PoseEstimator estimator = new PoseEstimator();
    WheelCommandEvent lastCommand;

    public EventPoseEstimator(RobotPose pose) {
        this.pose = pose;
        track.setColor(Color.MAGENTA);
    }

    public void update(WheelCommandEvent command) {
        System.out.println("estim update "+command);
        if (lastCommand != null) {
            pose = estimator.transform(pose, lastCommand.getSpeed(), lastCommand.getSteer(), (command.getTime() - lastCommand.getTime()) / 1000.0);
            track.append(pose.getPoint());
        }
        this.lastCommand = command;
    }
}
