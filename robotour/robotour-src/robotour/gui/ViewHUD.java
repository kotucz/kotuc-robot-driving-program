package robotour.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import robotour.iface.Compass;
import robotour.iface.MeasureException;
import robotour.iface.Sonar;
import robotour.navi.basic.Azimuth;
import robotour.util.RobotSystems;
import robotour.util.Sonars;
import vision.ImageFrame;
import vision.ar.PlaneCanvas;
import vision.input.VideoInput;

/**
 *
 * @author Kotuc
 */
public class ViewHUD {

    private Sonars sonars;
    private Compass compass;
//    private VideoInput video;
    private ImageFrame iframe = new ImageFrame("ViewHUD");
    PlaneCanvas pcanvas = new PlaneCanvas();

    public ViewHUD(RobotSystems systems) {
        this.sonars = systems.getSonars();
        this.compass = systems.getCompass();
//        this.video = systems.getVideo();
//        Odometry odometry = new Odometry(systems.getWheels(), systems.getCompass());
//        odometry.startOdometryThread();

    }
    private Azimuth azimuth;

    public void setAzimuth(Azimuth azimuth) {
        this.azimuth = azimuth;
    }

    public void show(BufferedImage image) {
        Graphics g = image.getGraphics();
        paintGrid(g);
        paintCompass(g);
        paintSonars(g);
        iframe.setImage(image);
    }

    public void paintGrid(Graphics g) {
        pcanvas.getPlaneProjection().setCamera(new Point3d(0, 0, 0.3), azimuth.radians(), Math.toRadians(75));
        pcanvas.setGraphics(g);
        pcanvas.drawGrid(0, 0);
    }

    /**
     * From azimuth x on screen
     * @param azimuth
     */
    public Point azimuthToPoint(Azimuth azimuth) {
        Point3d pos = new Point3d();
        Vector3d look = new Vector3d(azimuth.sin(), azimuth.cos(), 0);
        pos.add(look);
        return pcanvas.getPlaneProjection().getPositionOnImage(pos);
    }

    /**
     * From azimuth x on screen
     * @param azimuth
     */
    public Azimuth azimuth(Point point) {
        Point3d pos = pcanvas.getPlaneProjection().getPositionOnPlanePixels(point);
        Vector3d look = new Vector3d(pos);
        return Azimuth.valueOfRadians(Math.atan2(pos.x, pos.y));
    }

    public void paintCompass(Graphics g) {
        ((Graphics2D) g).setStroke(new BasicStroke(3f));
        g.setFont(Font.decode("Arial BOLD 16"));

        g.setColor(Color.GREEN);
        g.drawRect(0, 0, 320, 240);

//            Azimuth azimuth = compass.getAzimuth();

        for (Azimuths az : Azimuths.values()) {
//                Point p = azimuth(Azimuth.valueOf(az.getAzimuth().sub(azimuth)));
            Point p = azimuthToPoint(az.azimuth());
//            System.out.println("" + p);
            g.drawString(az.toString(), p.x, p.y);
        }

//            final int FULL = 320;
//            final double RAD = FULL / (2 * Math.PI);
//            int nx = (160 - (int) (RAD * azimuth)) % FULL;
//            int sx = (160 - (int) (RAD * (azimuth + Math.PI))) % FULL;
//            int ex = (160 - (int) (RAD * (azimuth + 0.5 * Math.PI))) % FULL;
//            int wx = (160 - (int) (RAD * (azimuth - 0.5 * Math.PI))) % FULL;

//            int nx = (int) (160 * (1 + azimuth.sin()));

//            int sx = (int) (160 * (1 + Math.sin(azimuth + Math.PI)));
//            int ex = (int) (160 * (1 + Math.sin(azimuth + 0.5 * Math.PI)));
//            int wx = (int) (160 * (1 + Math.sin(azimuth - 0.5 * Math.PI)));
//            g.drawString("N", nx, 20);
//            g.drawString("S", sx, 20);
//            g.drawString("E", ex, 20);
//            g.drawString("W", wx, 20);
        g.drawString("" + azimuth, 10, 40);
//        System.out.println("azimuth: " + azimuth);

    }

    public void paintSonars(Graphics g) {

        g.setColor(Color.RED);
        int sonx = 110;
        int sonang = 105;
        for (Sonar sonar : sonars.getSonars()) {
            try {
                g.drawOval(sonx, 230 - (int) (100 * sonar.getDistance()), 10, 10);
                int d = (int) (500 * sonar.getDistance());
                g.drawArc(sonx - d, 240 - d, 2 * d, 2 * d, sonang, 30);
            } catch (MeasureException ex) {
                g.drawRect(sonx, 220, 20, 20);
                Logger.getLogger(ViewHUD.class.getName()).log(Level.SEVERE, null, ex);
            }
            sonang -= 30;
            sonx += 50;
        }
//        System.out.println(sonars);
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        RobotSystems systems = RobotSystems.getDefault();
        final VideoInput video = systems.getVideo();
        final ViewHUD viewHUD = new ViewHUD(systems);
        new Thread() {

            @Override
            public void run() {
                while (true) {

                    //            pcanvas.getPlaneProjection().setCamera(new Point3d(0, 0, 1), compass.getAzimuth().radians(), Math.toRadians(45));
//                    viewHUD.setAzimuth(Azimuth.valueOfRadians(System.currentTimeMillis() / 10000.0));
                    viewHUD.show(video.snap());
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ViewHUD.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }.start();
    }
    // Variables declaration - do not modify
    // End of variables declaration
}
