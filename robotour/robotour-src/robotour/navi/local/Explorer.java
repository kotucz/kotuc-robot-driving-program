package robotour.navi.local;

import robotour.navi.local.odometry.RTOdometry;
import robotour.gui.map.RobotImgLayer;
import robotour.gui.map.LocalObject;
import robotour.gui.map.LocalMap;
import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.behavior.Arbitrator;
import robotour.iface.Compass;
import robotour.util.RobotSystems;
import robotour.iface.Sonar;
import robotour.util.Sonars;
import robotour.behavior.XPadDriving;
import robotour.navi.basic.Azimuth;
import robotour.gui.map.MapView;

/**
 *
 * @author Tomas
 */
public class Explorer implements Runnable {

    private LocalMap map = new LocalMap();
    private Sonars sonars;
    private Compass cmps;
    private RTOdometry odometry;
    final RobotImgLayer robotImgLayer = new RobotImgLayer();

    public Explorer(RobotSystems systems) {
        this.sonars = systems.getSonars();
        this.cmps = systems.getCompass();
        this.odometry = new RTOdometry(systems.getWheels());
        MapView view = new MapView();
        view.addLayer(map);

        view.addLayer(robotImgLayer);
        view.showInFrame();
        // odometry.start();
//        new OrthoXPadDriving(systems).start();
    }

    public void run() {
        XPadDriving xPadDriving = new XPadDriving(odometry);
        new Arbitrator(xPadDriving).start();

        while (true) {
            try {
                Azimuth azimuth = cmps.getAzimuth();
                double angle = azimuth.radians();
                angle -= Math.PI / 6.0;

                for (Sonar sonar : sonars.getSonars()) {
                    double dist = sonar.getDistance();
                    if (0.0 < dist && dist < 0.5) {
                        map.addObject(new LocalObject(
                                //                                new LocalPoint(
                                //                                odometry.getPoint().getX() + dist * Math.sin(angle),
                                //                                odometry.getPoint().getY() + dist * Math.cos(angle)),
                                odometry.getPoint().move(Azimuth.valueOfRadians(angle), dist),
                                0.1,
                                //                                Color.ORANGE
                                Color.getHSBColor((float) Math.sin(System.currentTimeMillis() / 10009.0), 1, 1) //  Color.getHSBColor((float) Math.random(), 1, 1)
                                ));
                    }
                    angle += Math.PI / 6.0;
                }

                robotImgLayer.setAzimuth(odometry.getAzimuth());
                robotImgLayer.setPoint(odometry.getPoint());

//                map.addObject(new LocalObject(
//                        new LocalPoint(odometry.getX(), odometry.getY()), 0.1));

                Thread.sleep(100);
            } catch (Exception ex) {
                Logger.getLogger(LocalMap.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void main(String[] args) {
        new Thread(new Explorer(RobotSystems.getDefault())).start();
    }
}
