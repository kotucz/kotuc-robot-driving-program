package robotour.gui.map.gps;

import robotour.gui.map.LocalMapView;
import robotour.navi.basic.Point;

import java.awt.Image;

import robotour.navi.basic.Angle;
import robotour.navi.gps.GPSPoint;

/**
 *
 * @author Tomas Kotula
 */
public class MapView extends LocalMapView {

    private GPSReference gpsReference;


    public MapView() {
//        gpsReference = new GPSReference(
//                GPSPoint.at(
//                Latitude.valueOf(Angle.parseDMS("49-12-23.524N")),
//                Longitude.valueOf(Angle.parseDMS("16-36-30.931E"))));

    }

    //     *  inversion to gpsToPoint
    //     */
    //    GPSPoint clickToGPS(Point click) {
    //        GPSPoint gp = GPSPoint.getPoint(
    //                Latitude.valueOfRadians(eye.latitude().radians() - (scale2 * click.y / EARTH_R / DPM)),
    //                Longitude.valueOfRadians(eye.longitude().radians() + (scale2 * click.x / GPSPoint.LONG_EARTH_R / DPM)));
    //        return gp;
    //    }
    //    /**
    //     *  method for painting transform
    //     */
    //    int getX(GPSPoint p) {
    //        // metres per degree 72602,289332233937399765867967468
    //        return (int) Math.round(metersPerLongitudeRadian * (p.longitude().radians() - zeroGPS.longitude().radians()) * DPM / scale2);
    //    }
    //
    //    /**
    //     *  method for painting transform
    //     */
    //    int getY(GPSPoint p) {
    //        return (int) -Math.round(metersPerLatitudeRadian * (p.latitude().radians() - zeroGPS.latitude().radians()) * DPM / scale2);
    //    }

    Point toLocal(GPSPoint gps) {
        if (gps == null) {
            throw new NullPointerException("gps");
        }
        if (gpsReference == null) {
            gpsReference = new GPSReference(gps);
        }
        return gpsReference.toLocal(gps);

    } //    Point toPoint(GPSPoint gps) {
    //        return toPoint(toLocal(gps));
    //    }


    public void drawLine(GPSPoint p1, GPSPoint p2, double lwidth) {
        drawLine(toLocal(p1), toLocal(p2), lwidth);


    }

    //    public void drawRect(Point p1, Point p2, double lwidth) {
//
//        Point minp = map.toPoint(p1);
//        Point maxp = map.toPoint(p2);
//
//        g.drawRect(minp.x, minp.y, maxp.x - minp.x, maxp.y - minp.y);
//
//    }
    public void drawText(GPSPoint center, String text) {
        drawString(text, toLocal(center));


    }

    public void drawDot(GPSPoint center, double radius) {
        fillOval(toLocal(center), radius);


    }

    public void drawTexture(Image texture, GPSPoint center, double scale, Angle azimuth) {
        drawTexture(texture, toLocal(center), scale, azimuth);


    }

}
