package robotour.gui.map;

import robotour.navi.basic.LocalPoint;
import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Tomas
 */
public class LocalPath implements MapLayer {

    private final List<LocalPoint> waypoints = new LinkedList<LocalPoint>();
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
            LocalPoint p1 = waypoints.get(0);
            for (LocalPoint localPoint : waypoints) {
                map.drawLine(p1, localPoint, 0.03);
                p1 = localPoint;
            }
        }

    }

    public void append(LocalPoint point) {
        synchronized (waypoints) {
            waypoints.add(point);
        }
    }

    public List<LocalPoint> getWaypoints() {
        return waypoints;
    }
}
