package robotour.navi.rndf;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import robotour.navi.gps.GPSPoint;
import robotour.gui.map.MapView;

public class Segment {

    List<Lane> lanes = new ArrayList<Lane>();
    String name;
    int id;

    void paint(Graphics g, MapView map) {

        for (Lane lane : lanes) {
            lane.paint(g, map);
        }

        GPSPoint point = lanes.get(0).center;
        map.drawText(point, this.toString());
    }

    public int getId() {
        return id;
    }

    Lane getLane(int laneId) {
        return lanes.get(laneId - 1);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
