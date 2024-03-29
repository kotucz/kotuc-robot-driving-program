package robotour.gui.map;

import robotour.gui.map.gps.GPSReference;
import robotour.gui.map.gps.MapView;
import robotour.navi.basic.Point;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import robotour.navi.basic.Angle;
import robotour.navi.gps.GPSPoint;
import robotour.navi.basic.Pose;

/**
 *
 * @author Tomas Kotula
 * @deprecated
 */
public class MapViewOld extends MapView {

    private static final int MAX_SCALE = 10000;//1024 * 1024 * 1024;
    private static final int MIN_SCALE = 1;
    private GPSReference gpsReference;
    public Pose eyelock = null;
    public boolean eyelockon = true;

    public MapViewOld() {
//        gpsReference = new GPSReference(
//                GPSPoint.at(
//                Latitude.valueOf(Angle.parseDMS("49-12-23.524N")),
//                Longitude.valueOf(Angle.parseDMS("16-36-30.931E"))));
        setScale(20);
    }
//    private GPSPoint eye = GPSPoint.fromDegrees(0, 0);
    private Point eye = new Point(0, 0);
    private Graphics2D graphics;
    private final List<MapLayer> layers = new ArrayList<MapLayer>();

    public void paint(Graphics g) {

        if (eyelockon) {
            if (eyelock != null) {
                this.setEye(eyelock.getPoint());
            }
        }

        this.graphics = (Graphics2D) g;

        synchronized (layers) {
            for (MapLayer mapLayer : layers) {
                mapLayer.paint(this);
            }
        }

        drawGrid(this);

    }

    public void addLayer(MapLayer layer) {
        synchronized (layers) {
            layers.add(layer);
        }
    }

    public void setEye(Point neweye) {
        this.eye = neweye;
    }

    //    /**
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
    int metersToPixels(double meters) {
        return (int) Math.round(meters * DPM / scale2);
    }

    Point toLocal(GPSPoint gps) {
        if (gps == null) {
            throw new NullPointerException("gps");
        }
        if (gpsReference == null) {
            gpsReference = new GPSReference(gps);
        }
        return gpsReference.toLocal(gps);
    }

    java.awt.Point toPoint(GPSPoint gps) {
        return toPoint(toLocal(gps));
    }

    Point clickToLocal(java.awt.Point click) {
        Point lp = new Point(
                eye.getX() + (scale2 * click.x / DPM),
                eye.getY() - (scale2 * click.y / DPM));
        return lp;
    }

    int getX(Point p) {
//        return (int) Math.round((p.getX() - eye.getLongMetres()) * DPM / scale2);
//        return (int) Math.round((p.getX() - eye.getX()) * DPM / scale2);
        return metersToPixels(p.getX() - eye.getX());
    }

    int getY(Point p) {
//        return (int) -Math.round((p.getY() - eye.getLatMetres()) * DPM / scale2);
//        return (int) -Math.round((p.getY() - eye.getY()) * DPM / scale2);
        return -metersToPixels(p.getY() - eye.getY());
    }

    java.awt.Point toPoint(Point local) {
        return new java.awt.Point(getX(local), getY(local));
    }

    public Point getEye() {
        return eye;
    }
//    double scale = 0.0000001;
//    private double scale2 = 10000000.0;
    private double scale2;
    /**
     * dots (pixels) per inch (2.54 cm)
     */
    public static final double DPI = 98;// 72;
    /**
     *  how many dots (pixels) in map with scale 1:1 is one meter in real
     */
    public static final double DPM = DPI * 100.0 / 2.54;

    public double getScale() {
        return scale2;
    }

    public void setScale(double scale) {
        scale = Math.max(MIN_SCALE, Math.min(scale, MAX_SCALE));
        this.scale2 = scale;
    }

    public void zoomTo(Point center, double scale) {
        setEye(center);
        setScale(scale);
    }

//    public void zoomTo(Track track) {
//        if (track == null) {
//            throw new NullPointerException("track");
//        }
//        GPSPoint center = GPSPoint.getPoint(
//                Latitude.valueOfRadians((track.topLeft.latitude().radians() + track.bottomRight.latitude().radians()) / 2.0),
//                Longitude.valueOfRadians((track.topLeft.longitude().radians() + track.bottomRight.longitude().radians()) / 2.0));
//        zoomTo(center, track.topLeft.getDistanceMetres(track.bottomRight) * 4);
//    }
    public Graphics2D getGraphics() {
        return graphics;
    }
    double mets = 0.01;

    private void drawGrid(MapViewOld map) {
        Graphics2D g = map.getGraphics();
        g.setColor(Color.DARK_GRAY);

//        double lon = eye.longitude().radians();//getLongitude();
//        double prec = 0.001;
//        lon = (int) Math.round(lon / prec);
//        lon = lon * prec;

        mets = 0.01;
        // pixel minimum diff
        while (metersToPixels(mets) < 30) {
            mets *= 10;
        }

        for (int i = -10; i < 10; i++) {

            Point px1 = new Point(-1000, Math.round(eye.getY()) + i * mets);
            Point px2 = new Point(1000, Math.round(eye.getY()) + i * mets);

            Point py1 = new Point(Math.round(eye.getX()) + i * mets, -1000);
            Point py2 = new Point(Math.round(eye.getX()) + i * mets, 1000);

            map.drawLine(px1, px2, 0.001);
            map.drawLine(py1, py2, 0.001);

//            map.drawLine(GPSPoint.fromDegrees(47, i), GPSPoint.fromDegrees(55, i), 1);
//            g.drawLine((int)(e.x+i), (int)(e.y-2000), (int)((e.x+i)), (int)(e.y+2000));
//            g.drawLine((int)(e.x-2000), (int)((e.y+i)), (int)(e.x+2200), (int)((e.y+i)));
//            g.drawLine((int)(zoom*i), -2000, (int)(zoom*i), 2000);
//            g.drawLine(-2000, (int)(zoom*i), 2000, (int)(zoom*i));
        }


//        Point e = gpsToPoint(eye);
//        
//        Point d = new Point(111, 111);
//        
//        for (int i; ; lon+=prec) {
//            
//            map.drawLine(new GPSPoint(40, lon+prec), new GPSPoint(60, lon+prec), 1);
//            g.drawString(""+(lon+prec), getX(new GPSPoint(50, lon+prec)), 50);
//            
//        }


//        map.drawLine(new GPSPoint(40, lon), new GPSPoint(60, lon), 1);
//        g.drawString(""+lon, getX(new GPSPoint(50, lon)), 50);
//        
//        map.drawLine(new GPSPoint(40, lon+prec), new GPSPoint(60, lon+prec), 1);
//        g.drawString(""+(lon+prec), getX(new GPSPoint(50, lon+prec)), 50);
//        

//        for (int i = 5; i < 25; i++) {
//            map.drawLine(GPSPoint.fromDegrees(47, i), GPSPoint.fromDegrees(55, i), 1);
//            g.drawLine((int)(e.x+i), (int)(e.y-2000), (int)((e.x+i)), (int)(e.y+2000));
//            g.drawLine((int)(e.x-2000), (int)((e.y+i)), (int)(e.x+2200), (int)((e.y+i)));
//            g.drawLine((int)(zoom*i), -2000, (int)(zoom*i), 2000);
//            g.drawLine(-2000, (int)(zoom*i), 2000, (int)(zoom*i));
//        }

    }

    public void drawLine(GPSPoint p1, GPSPoint p2, double lwidth) {
        drawLine(toLocal(p1), toLocal(p2), lwidth);
    }

    public void drawLine(Point p1, Point p2, double lwidth) {

        Stroke stroke = graphics.getStroke();
        graphics.setStroke(new BasicStroke((int) (lwidth * DPM / getScale()), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        graphics.drawLine(getX(p1), getY(p1), getX(p2), getY(p2));

        graphics.setStroke(stroke);

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
        drawText(toLocal(center), text);
    }

    public void drawText(Point center, String text) {
        graphics.drawString(text, getX(center), getY(center));
    }

    public void drawDot(GPSPoint center, double radius) {
        drawDot(toLocal(center), radius);
    }

    public void drawDot(Point center, double radius) {
        int r = metersToPixels(radius);

        graphics.fillOval(getX(center) - r / 2, getY(center) - r / 2, r, r);
    }

    public void drawOval(Point center, double radius) {
        int r = metersToPixels(radius);

        graphics.drawOval(getX(center) - r, getY(center) - r, 2 * r, 2 * r);
    }

    public void drawTexture(Image texture, GPSPoint center, double scale, Angle azimuth) {
        drawTexture(texture, toLocal(center), scale, azimuth);
    }

    public void drawTexture(Image texture, Point center, double scale, Angle azimuth) {

        java.awt.Point cent = toPoint(center);

        int w = (int) (texture.getWidth(null) * scale / getScale());
        int h = (int) (texture.getHeight(null) * scale / getScale());

        graphics.translate(cent.x, cent.y);
        graphics.rotate(azimuth.radians());

        graphics.drawImage(texture, -w / 2, -h / 2, w, h, null);

        graphics.rotate(-azimuth.radians());
        graphics.translate(-cent.x, -cent.y);

    }

    public JFrame showInFrame() {
        JFrame frame = new JFrame("Map");
        frame.add(new MapViewPanel(this));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        return frame;
    }
}
