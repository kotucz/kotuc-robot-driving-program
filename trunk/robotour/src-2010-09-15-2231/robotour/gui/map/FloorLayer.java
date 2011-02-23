package robotour.gui.map;

import java.awt.Point;
import java.awt.geom.Rectangle2D.Double;
import vision.pathfinding.birdseye.Floor;

public class FloorLayer implements MapLayer {

    final Floor floor;

    public FloorLayer(Floor floor) {
        this.floor = floor;
    }

    public void paint(MapView map) {
        Double rect = floor.getRect();
        int w = map.metersToPixels(rect.width);
        int h = -map.metersToPixels(rect.height);
        LocalPoint origin = new LocalPoint(rect.x, rect.y);
        Point toPoint = map.toPoint(origin);
        map.getGraphics().drawImage(floor.getImage(), toPoint.x , toPoint.y, w, h, null);
//        map.getGraphics().drawImage(floor.getImage(), map.getX(origin), map.getY(origin), w, h, null);
        map.getGraphics().drawString("+", toPoint.x, toPoint.y);
//        throw new UnsupportedOperationException("Not supported yet.");
    }
}
