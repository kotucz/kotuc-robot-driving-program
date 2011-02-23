package robotour.navi.rndf;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import robotour.navi.gps.GPSPoint;
import robotour.gui.map.MapView;
import robotour.navi.gps.Track;
import robotour.navi.gps.TrackPoint;

public class Lane {

    List<Waypoint> waypoints = new ArrayList<Waypoint>();
    int width = 5;
    String leftBoundary;
    String rightBoundary;
    GPSPoint topLeft;
    GPSPoint bottomRight;
    GPSPoint center;

    void refreshBounds() {
        topLeft = null;
        bottomRight = null;
        for (Waypoint waypoint : waypoints) {
            if (topLeft == null || waypoint.point.latitude().radians() < topLeft.latitude().radians()) {
                topLeft = waypoint.point;
            }
            if (topLeft == null || waypoint.point.latitude().radians() < topLeft.latitude().radians()) {
                topLeft = waypoint.point;
            }
            if (bottomRight == null || waypoint.point.longitude().radians() < bottomRight.longitude().radians()) {
                bottomRight = waypoint.point;
            }
            if (bottomRight == null || waypoint.point.longitude().radians() < bottomRight.longitude().radians()) {
                bottomRight = waypoint.point;
            }
            this.center = GPSPoint.fromDegrees(
                    (topLeft.latitude().degrees() + bottomRight.latitude().degrees()) / 2,
                    (topLeft.longitude().degrees() + bottomRight.longitude().degrees()) / 2);
        }
    }

    void paint(Graphics g, MapView map) {
        g.setColor(Color.BLUE);
//        System.out.println("line " + num_waypoints);

        Waypoint prev = null;
        for (Waypoint waypoint : waypoints) {
            if (prev != null) {
                map.drawLine(prev.getPoint(), waypoint.getPoint(), width);
            }
            prev = waypoint;
        }
        for (Waypoint waypoint : waypoints) {
            waypoint.paint(g, map);
        }
    }

    public Track getTrack() {
        Track track = new Track();
        for (Waypoint waypoint : waypoints) {
            track.append(new TrackPoint(waypoint.getPoint()));
        }
        return track;
    }
}
