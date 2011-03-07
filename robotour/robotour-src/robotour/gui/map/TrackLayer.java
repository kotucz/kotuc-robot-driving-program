package robotour.gui.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;
import robotour.navi.gps.GPSPoint;
import robotour.navi.gps.Track;
import robotour.navi.gps.TrackPoint;

/**
 *
 * @author PC
 */
public class TrackLayer implements MapLayer {

    final Track track;
    GPSPoint minBounds = GPSPoint.fromDegrees(180, 90);
    GPSPoint maxBounds = GPSPoint.fromDegrees(-180, -90);
//    protected LinkedList<TrackPoint> track = new LinkedList<TrackPoint>();;
    Color roadColor = Color.RED;
    Color pointColor = Color.ORANGE;

    public Track track() {
        return track;
    }

    /** Creates a new instance of Track */
    public TrackLayer(Track track) {
        this.track = track;
    }

    public void paint(MapView map) {
        List<TrackPoint> points = track.track();

        if (points.isEmpty()) {
            return;
        }
//        if (track.isEmpty()) return;

        Graphics2D g = map.getGraphics();

        if (roadColor != null) {
            g.setColor(roadColor);
        }

        TrackPoint pt0 = points.get(0);
//        TrackPoint pt0 = track.getFirst();

//        g.setStroke(new BasicStroke((int)(1*map.dpm/map.getScale()), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        for (TrackPoint pt : points) { //track
            map.drawLine(pt.getPoint(), pt0.getPoint(), 1); // 1 meter line

//            g.fillOval(map.getX(pt)-2, map.getY(pt)-2, 4, 4);
            pt0 = pt;
        }

        if (pointColor != null) {
            g.setColor(pointColor);
        }

        for (TrackPoint pt : points) {// track
            map.drawDot(pt.getPoint(), 0.5); // 0.5 meter dot
        }

        {
        g.setColor(Color.RED);
        
//        Point minp = map.toPoint(minBounds);
//        Point maxp = map.toPoint(maxBounds);

            System.err.println("TODO track layer bounds");

//        g.drawRect(minp.x, minp.y, maxp.x - minp.x, maxp.y - minp.y);
        }




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
}
