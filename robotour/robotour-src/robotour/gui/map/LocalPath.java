package robotour.gui.map;

import robotour.navi.basic.Point;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Tomas
 */
public class LocalPath implements MapLayer {

    private final List<Point> waypoints = new LinkedList<Point>();
    private Color color = Color.GREEN;

    public LocalPath() {
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void paint(Paintable map) {

        if (waypoints.isEmpty()) {
            return;
        }

        map.getGraphics().setColor(color);

        synchronized (waypoints) {
            Point p1 = waypoints.get(0);
            for (Point point : waypoints) {
                map.drawLine(p1, point, 0.03);
                p1 = point;
            }
        }

    }

    public void append(Point point) {
        synchronized (waypoints) {
            waypoints.add(point);
        }
    }

    public List<Point> getWaypoints() {
        return waypoints;
    }
}
