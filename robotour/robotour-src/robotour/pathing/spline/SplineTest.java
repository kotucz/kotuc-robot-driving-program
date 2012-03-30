package robotour.pathing.spline;

import robotour.gui.map.gps.MapView;
import robotour.navi.basic.Point;

/**
 *
 * @author Kotuc
 */
public class SplineTest {

    public static void main(String[] args) {
        
        Trajectory tr = new Trajectory();
        tr.add(new Point(0, 0), new Point(0, 1));

        tr.add(new Point(1, 1), new Point(1, 0));

        MapView mv = new MapView();
        mv.addLayer(tr);

        mv.showInFrame();


    }
    
}
