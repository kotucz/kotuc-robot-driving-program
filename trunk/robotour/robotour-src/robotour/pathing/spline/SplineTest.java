package robotour.pathing.spline;

import robotour.navi.basic.LocalPoint;
import robotour.gui.map.MapView;

/**
 *
 * @author Kotuc
 */
public class SplineTest {

    public static void main(String[] args) {
        
        Trajectory tr = new Trajectory();
        tr.add(new LocalPoint(0, 0), new LocalPoint(0, 1));

        tr.add(new LocalPoint(1, 1), new LocalPoint(1, 0));

        MapView mv = new MapView();
        mv.addLayer(tr);

        mv.showInFrame();


    }
    
}
