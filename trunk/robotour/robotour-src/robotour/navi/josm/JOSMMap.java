package robotour.navi.josm;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.Way;
import org.openstreetmap.josm.data.projection.Mercator;
import org.openstreetmap.josm.io.IllegalDataException;
import org.openstreetmap.josm.io.OsmReader;
import org.openstreetmap.josm.tools.Pair;
import robotour.gui.map.gps.GPSReference;
import robotour.gui.map.MapLayer;
import robotour.gui.map.gps.MapView;
import robotour.gui.map.Paintable;

/**
 *
 * @author Kotuc
 */

public class JOSMMap  implements MapLayer{

    HashMap<Node, MyNode> mynodes = new HashMap<Node, MyNode>();

    public void load(File file) throws IOException, IllegalDataException {

        Main.proj = new Mercator();

//        DataSet parseDataSet = OsmReader.parseDataSet(new FileInputStream(file), NullProgressMonitor.INSTANCE);
        DataSet parseDataSet = OsmReader.parseDataSet(new FileInputStream(file), null);

//        Collection<Node> nodes = parseDataSet.getNodes();

        Collection<Way> ways = parseDataSet.getWays();
        for (Way way : ways) {
//            System.out.println("highway " + way.getKeys().get("highway"));
            if (way.getKeys().get("highway") != null) {
                System.out.println("highway " + way.getKeys().get("highway"));
                for (Node node : way.getNodes()) {
                    if (mynodes.get(node) == null) {
                        mynodes.put(node, new MyNode(node));
                    }
                }
                List<Pair<Node, Node>> nodePairs = way.getNodePairs(false);
                for (Pair<Node, Node> pair : nodePairs) {
                    MyNode noda = mynodes.get(pair.a);
                    MyNode nodb = mynodes.get(pair.b);
                    noda.neighs.add(nodb);
                    nodb.neighs.add(noda);
                }
            }
        }
    }

//    TODO
    GPSReference gpsref;
    
    public void paint(Paintable g) {


        g.setColor(Color.GREEN);

//        g.setStroke(new BasicStroke((int)(1*map.dpm/map.getScale()), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        for (MyNode myNode : mynodes.values()) {
            for (MyNode m : myNode.neighs) {
                g.drawLine(gpsref.toLocal(myNode.gps), gpsref.toLocal(m.gps), 1); // 1 meter line
            }
        }

        g.setColor(Color.YELLOW);

        for (MyNode myNode : mynodes.values()) {
            g.fillOval(gpsref.toLocal(myNode.gps), 0.5); // 0.5 meter dot
        }

        g.setColor(Color.RED);

//        Point minp = map.toPoint(minBounds);
//        Point maxp = map.toPoint(maxBounds);
//
//        g.drawRect(minp.x, minp.y, maxp.x - minp.x, maxp.y - minp.y);

//        g.setStroke(new BasicStroke((int)(1*map.dpm/map.getScale()), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
//
//        for (TrackPoint pt : track) {
//            g.fillOval(map.getX(pt)-2, map.getY(pt)-2, 4, 4);
//            if (pt0!=null) {
//                g.drawLine(map.getX(pt), map.getY(pt), map.getX(pt0), map.getY(pt0));
//            }
//            pt0 = pt;
//        }

    }

    public static void main(String[] args) throws IOException, IllegalDataException {
        JOSMMap map = new JOSMMap();
        map.load(new File("C:/Users/Kotuc/Desktop/botanicka2.osm"));

        MapView view = new MapView();
        view.addLayer(map);
        view.showInFrame();
    }
}
