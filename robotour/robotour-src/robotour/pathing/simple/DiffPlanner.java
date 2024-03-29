package robotour.pathing.simple;

import robotour.navi.basic.Point;
import robotour.navi.basic.Angle;
import robotour.navi.basic.Pose;
import robotour.navi.local.Transforms;

/**
 *
 * @author Kotuc
 */
public class DiffPlanner {

    final DiffWheelParameters params = new DiffWheelParameters();

    public DiffPlanner() {
        
    }

    public double oneWheelOnPlaceRotateDist(Angle angle) {
        return angle.arcLengthRadius(params.oneWheelGauge);
    }

    /**
     * @param rightTurn angular clockwise.
     * @param centerx right from robot center.
     * @return
     */
    public void distsArc(Angle rightTurn, double centerx) {

        double leftRadius = centerx+params.oneWheelGauge;
        double rightRadius = centerx-params.oneWheelGauge;

        double leftDist = rightTurn.radians()*leftRadius;
        double rightDist = rightTurn.radians()*rightRadius;        

    }
    
    void arc(Pose from, Point globalTo) {
        Point to = Transforms.globalToLocal(from, globalTo);

    }

    


}
