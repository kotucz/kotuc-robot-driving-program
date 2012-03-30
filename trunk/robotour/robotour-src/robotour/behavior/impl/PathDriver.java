package robotour.behavior.impl;

import robotour.gui.map.LocalPath;
import robotour.navi.basic.Point;
import java.util.Iterator;
import robotour.driving.BlindCompassPilot;

/**
 *
 * @author Tomas
 */
public class PathDriver implements Runnable {

    private final LocalPath path;
    private final BlindCompassPilot pilot;

    public PathDriver(LocalPath path, BlindCompassPilot pilot) {
        this.path = path;
        this.pilot = pilot;
    }

    public void run() {
        Iterator<Point> iterator = path.getWaypoints().iterator();
        Point last = iterator.next();
        for (; iterator.hasNext();) {
            Point next = iterator.next();

            pilot.rotateTo(last.getAzimuthTo(next), false);
            pilot.travel(last.getDistanceTo(next), false);

            last = next;
        }
    }

    public void drive() {
        run();
    }
}
