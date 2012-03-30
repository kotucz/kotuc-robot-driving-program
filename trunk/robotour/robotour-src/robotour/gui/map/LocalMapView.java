package robotour.gui.map;

import robotour.navi.basic.*;
import robotour.navi.basic.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Kotuc
 * Date: 15.3.12
 * Time: 21:39
 * To change this template use File | Settings | File Templates.
 */
public class LocalMapView implements Paintable {




    private Graphics2D graphics;
    private final java.util.List<MapLayer> layers = new ArrayList<MapLayer>();

    public void paint(Graphics g) {

        this.graphics = (Graphics2D)g;
        
        synchronized (layers) {
            for (MapLayer mapLayer : layers) {
                mapLayer.paint(this);
            }
        }


    }

    public void addLayer(MapLayer layer) {
        synchronized (layers) {
            layers.add(layer);
        }
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
    @Override
    public Graphics2D getGraphics() {
        return graphics;


    }

    @Override
    public void setColor(Color color) {
       graphics.setColor(color);
        //To change body of implemented methods use File | Settings | File Templates.
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





    @Override
    public void drawLine(Point p1, robotour.navi.basic.Point p2, double lwidth) {

        Stroke stroke = graphics.getStroke();
        graphics.setStroke(new BasicStroke((float) lwidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        graphics.draw(new Line2D.Double(p1.toAwtPoint(), p2.toAwtPoint()));

        graphics.setStroke(stroke);



    }

    public void drawString(String text, Point p1) {
        AffineTransform transform = graphics.getTransform();
        AffineTransform transform2 = (AffineTransform)transform.clone();
        double scaleX = transform2.getScaleX();
        graphics.setTransform(transform2);
        graphics.scale(1/scaleX, -1/scaleX);
        graphics.drawString(text, (float) p1.getX(), (float) p1.getY());
//        graphics.scale(10, -10);
        graphics.setTransform(transform);
    }

    @Override
    public void drawTexture(Image robotImg, Point point, double scale, Azimuth azimuth) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void fillOval(Point center, double radius) {

        Stroke stroke = graphics.getStroke();
        graphics.setStroke(new BasicStroke((float) 0.001f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        graphics.fill(new Ellipse2D.Double(center.getX() - radius, center.getY() - radius,2* radius, 2* radius));

        graphics.setStroke(stroke);
    }

    public void drawOval(Point center, double radius) {
        Stroke stroke = graphics.getStroke();
        graphics.setStroke(new BasicStroke((float) 0.001f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        graphics.draw(new Ellipse2D.Double(center.getX() - radius, center.getY() - radius, 2* radius, 2*radius));
//        System.out.println("HERE");
        graphics.setStroke(stroke);
    }

    public void drawTexture(Image texture, Point center, double scale, Angle azimuth) {

//        Point cent = toPoint(center);
        Point2D cent = center.toAwtPoint();

        int w = (int) (texture.getWidth(null) * scale);


        int h = (int) (texture.getHeight(null) * scale);

        AffineTransform transform = graphics.getTransform();
        graphics.translate(cent.getX(), cent.getX());
        graphics.rotate(azimuth.radians());

        graphics.drawImage(texture, -w / 2, -h / 2, w, h, null);

        graphics.setTransform(transform);
//        graphics.rotate(-azimuth.radians());
//        graphics.translate(-cent.x, -cent.y);



    }

    public JFrame showInFrame() {
        JFrame frame = new JFrame("Map");
        frame.add(new MapViewPanel(this));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        return frame;
    }

    //    int getX(Point p) {
////        return (int) Math.round((p.getX() - eye.getLongMetres()) * DPM / scale2);
////        return (int) Math.round((p.getX() - eye.getX()) * DPM / scale2);
//        return metersToPixels(p.getX() - eye.getX());
//    }
//    int getY(Point p) {
////        return (int) -Math.round((p.getY() - eye.getLatMetres()) * DPM / scale2);
////        return (int) -Math.round((p.getY() - eye.getY()) * DPM / scale2);
//        return -metersToPixels(p.getY() - eye.getY());
//    }
//    Point toPoint(Point local) {
//        return local.toAwtPoint();
//        return new Point(getX(local), getY(local));
//    }

}
