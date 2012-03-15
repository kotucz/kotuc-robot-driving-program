package robotour.navi.gps.rndf;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import robotour.gui.map.gps.MapView;
import robotour.navi.gps.GPSPoint;

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
