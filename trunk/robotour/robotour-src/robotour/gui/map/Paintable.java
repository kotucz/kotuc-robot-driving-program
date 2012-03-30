package robotour.gui.map;

import robotour.navi.basic.Azimuth;
import robotour.navi.basic.Point;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Kotuc
 * Date: 15.3.12
 * Time: 22:13
 * To change this template use File | Settings | File Templates.
 */
public interface Paintable {
    //    public void zoomTo(Track track) {
//        if (track == null) {
//            throw new NullPointerException("track");
//        }
//        GPSPoint center = GPSPoint.getPoint(
//                Latitude.valueOfRadians((track.topLeft.latitude().radians() + track.bottomRight.latitude().radians()) / 2.0),
//                Longitude.valueOfRadians((track.topLeft.longitude().radians() + track.bottomRight.longitude().radians()) / 2.0));
//        zoomTo(center, track.topLeft.getDistanceMetres(track.bottomRight) * 4);
//    }
    Graphics2D getGraphics();

    void setColor(Color color);
    void drawLine(Point p1, Point p2, double lwidth);
    void drawOval(Point p1, double rad);
    void fillOval(Point p1, double rad);
    void drawString(String text, Point p1);

    void drawTexture(Image robotImg, Point point, double scale, Azimuth azimuth);
}
