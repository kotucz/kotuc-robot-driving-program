package robot.navi.diff;

import robotour.gui.map.LocalPoint;
import robotour.navi.basic.Angle;
import robotour.navi.basic.RobotPose;

/**
 *
 * @author Kotuc
 */
public class DiffPlanner {

    final DiffWheelParameters params = new DiffWheelParameters();

    public DiffPlanner() {
        
    }

    /**
     * @param rightTurn angle clockwise.
     * @param centerx right from robot center.
     * @return
     */
    void distsArc(Angle rightTurn, double centerx) {

        double leftRadius = centerx+params.oneWheelGauge;
        double rightRadius = centerx-params.oneWheelGauge;

        double leftDist = rightTurn.radians()*leftRadius;
        double rightDist = rightTurn.radians()*rightRadius;        

    }
    
    void arc(RobotPose from, LocalPoint to) {
        
    }

    


}
