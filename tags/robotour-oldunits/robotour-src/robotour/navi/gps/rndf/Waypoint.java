package robotour.navi.gps.rndf;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.vecmath.Point3f;

import robotour.gui.map.gps.MapView;
import robotour.navi.gps.GPSPoint;

/**
 * single waypoint
 */
public class Waypoint {

    WPID wpid;
    GPSPoint point;
    boolean stop = false;
    int checkpoint = -1;
    ArrayList<Waypoint> nexts = new ArrayList<Waypoint>();
    ArrayList<Waypoint> prevs = new ArrayList<Waypoint>();

    void addNext(Waypoint wp) {
        nexts.add(wp);
        wp.prevs.add(this);
    }

    Point3f getPoint3f() {
        throw new UnsupportedOperationException();
    }

    void paint(Graphics g, MapView map) {
        for (Waypoint wp : nexts) {
            g.setColor(Color.GREEN);
//                g.fillOval(getX()-7, getY()-7, 14, 14);
            map.drawLine(point, wp.point, 1);
        }
        if (stop) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.YELLOW);
        }
        if (checkpoint > 0) {
            map.drawDot(point, 1.3);
            g.setColor(Color.BLACK);
        } else {
            map.drawDot(point, 0.6);
        }
    }

    public GPSPoint getPoint() {
        return point;
    }

    @Override
    public String toString() {
        return "(" + wpid + "):" + point;
    }
}
