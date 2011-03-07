package robotour.navi.local.birdseye;

import java.awt.Color;
import robotour.gui.map.LocalPath;
import robotour.iface.Wheels;
import robotour.navi.basic.RobotPose;
import robotour.util.log.events.WheelCommandEvent;

/**
 *
 * @author Kotuc
 */
public class PosePredictor extends RobotPose implements Wheels {

    public LocalPath track = new LocalPath();
    PoseEstimator estimator = new PoseEstimator();
    WheelCommandEvent lastCommand;

    public PosePredictor(RobotPose pose) {
        super(pose);
        track.setColor(Color.MAGENTA);
    }
    double oldspeed = 0;
    double oldsteer = 0;
    long lastt = 0;

    public void update(WheelCommandEvent command) {
        this.update(command.getSpeed(), command.getSteer(), command.getTime());
    }

    public void update(double newspeed, double newsteer, long time) {
//        System.out.println("estim update "+command);
        if (lastt != 0) {
            this.setTo(estimator.transform(this,
                    oldspeed, oldsteer,
                    (time - lastt) / 1000.0));
            track.append(getPoint());
        }
        this.oldspeed = newspeed;
        this.oldsteer = newsteer;
        this.lastt = time;
    }

    public void update() {
        update(oldspeed, oldsteer, System.currentTimeMillis());
    }

    public void setSpeed(double speed) {
        update(speed, oldsteer, System.currentTimeMillis());
    }

    public void setSteer(double steer) {
        update(oldspeed, steer, System.currentTimeMillis());
    }

    public void stop() {
        update(0, 0, System.currentTimeMillis());
    }
//        public void update(WheelCommandEvent command) {
////        System.out.println("estim update "+command);
//        if (lastCommand != null) {
//            this.setTo(estimator.transform(this,
//                    lastCommand.getSpeed(), lastCommand.getSteer(),
//                    (command.getTime() - lastCommand.getTime()) / 1000.0));
//            track.append(getPoint());
//        }
//        this.lastCommand = command;
//    }
}
