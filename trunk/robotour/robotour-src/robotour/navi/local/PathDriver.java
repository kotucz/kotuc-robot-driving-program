package robotour.navi.local;

import robotour.gui.map.LocalPath;
import robotour.gui.map.LocalPoint;
import java.util.Iterator;

/**
 *
 * @author Tomas
 */
public class PathDriver implements Runnable {

    private final LocalPath path;
    private final BlindPilot pilot;

    public PathDriver(LocalPath path, BlindPilot pilot) {
        this.path = path;
        this.pilot = pilot;
    }

    public void run() {
        Iterator<LocalPoint> iterator = path.getWaypoints().iterator();
        LocalPoint last = iterator.next();
        for (; iterator.hasNext();) {
            LocalPoint next = iterator.next();

            pilot.rotateTo(last.getAzimuthTo(next), false);
            pilot.travel(last.getDistanceTo(next), false);

            last = next;
        }
    }

    public void drive() {
        run();
    }
}
