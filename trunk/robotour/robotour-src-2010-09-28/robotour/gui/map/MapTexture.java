package robotour.gui.map;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import robotour.navi.gps.Angle;
import robotour.navi.gps.GPSPoint;
import robotour.navi.gps.Latitude;
import robotour.navi.gps.Longitude;

class MapTexture {

    final GPSPoint point;
    final Image texture;
    final double tscale;
//    final MapTextureManager outer;

    static MapTexture load(String imgfile, String latitude, String longitude, double scale) {
        return new MapTexture(
                Toolkit.getDefaultToolkit().createImage(imgfile),
                GPSPoint.at(Latitude.valueOf(Angle.parseDMS(latitude)),
                Longitude.valueOf(Angle.parseDMS(longitude))),
                scale);
    }

    private MapTexture(Image imgfile, GPSPoint point, double scale) {
        this.texture = imgfile;
        this.point = point;
        this.tscale = scale;
    }

//        MapTexture(String imgfile, double latitude, double longitude, double scale) {
//            texture = Toolkit.getDefaultToolkit().createImage(Setup.getRoot() + imgfile);
//            this.point = GPSPoint.fromDegrees(latitude, longitude);
//            this.tscale = scale;
//        }
//
//        MapTexture(String imgfile, String latitude, String longitude, double scale) {
//            texture = Toolkit.getDefaultToolkit().createImage(Setup.getRoot() + imgfile);
//            this.point = GPSPoint.getPoint(
//                    Latitude.valueOf(Angle.parseDMS(latitude)),
//                    Longitude.valueOf(Angle.parseDMS(longitude)));
//            this.tscale = scale;
//        }
    public void paint(Graphics g, MapView map) {
        int w = (int) (texture.getWidth(null) * tscale / map.getScale());
        int h = (int) (texture.getHeight(null) * tscale / map.getScale());
        Point toPoint = map.toPoint(point);
        g.drawImage(texture, toPoint.x - w / 2, toPoint.y - h / 2, w, h, null);
        g.drawString("+", toPoint.x, toPoint.y);
    }
}
