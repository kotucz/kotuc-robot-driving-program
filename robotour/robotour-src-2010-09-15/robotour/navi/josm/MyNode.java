package robotour.navi.josm;

import java.util.Collection;
import java.util.HashSet;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.Node;
import robotour.navi.gps.GPSPoint;

/**
 *
 * @author Kotuc
 */
public class MyNode {

    final Collection<MyNode> neighs = new HashSet<MyNode>();
    final Node original;
    final GPSPoint gps;

    public MyNode(Node original) {
        this.original = original;
        LatLon coor = original.getCoor();
//        System.out.println("coor: " + coor);
        if (coor != null) {            
            gps = GPSPoint.fromDegrees(coor.lat(), coor.lon());
        } else {
            System.err.println("null coor");
            gps = GPSPoint.fromDegrees(0, 0);
        }
        
    }
}
