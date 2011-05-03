package cambot;

import cambot.DrivingThread;
import java.awt.Color;
import robotour.gui.map.LocalPoint;
import robotour.gui.map.MapView;
import robotour.gui.map.RobotImgLayer;
import robotour.iface.Wheels;
import robotour.navi.basic.Azimuth;
import robotour.navi.basic.RobotPose;
import robotour.navi.local.birdseye.BirdMap;
import robotour.localization.odometry.PosePredictor;

/**
 *
 * @author Kotuc
 */
public class VirtualRobotSimulator {

    public final PosePredictor estimator = new PosePredictor(
            new RobotPose(new LocalPoint(0, 0), Azimuth.NORTH));
    private final Wheels wheels = estimator;
    final BirdMap birdMap = new BirdMap();

    public VirtualRobotSimulator() {
        int x0 = birdMap.floor.getX(0);
        int y0 = birdMap.floor.getX(0);

        birdMap.floor.fillRect(x0 - 3, y0 - 3, 5, 20, 0);

        birdMap.floor.fillRect(x0 - 3, y0 + 19, 30, 5, 0);

        birdMap.floor.fillRect(x0 + 20, y0 + 21, 5, 50, 0);


        birdMap.density.getSolids().getGraphics().setColor(Color.white);
        birdMap.density.getSolids().getGraphics().fillRect(x0 - 1, y0 + 15, 2, 2);

//        System.out.println("get density " + density.getSolids().getImage().getRGB(x0 - 1, y0 + 15));
//        System.out.println("get density "+density.getFieldDensity(x0-1, y0+15));
        birdMap.exploring.setTarget(new LocalPoint(5, 10));

        showView();
    }

    public void startDrivingThread() {
        new DrivingThread(wheels, estimator, birdMap.exploring).start();
    }

//    void startDrivingThread() {
//        new Thread() {
//
//            @Override
//            public void run() {
//                while (true) {
//                    try {
//                        double speed = 0.4;
//                        double steer = exploring.randomSearch(estimator, speed);
//
//                        wheels.setSpeed(speed);
//                        wheels.setSteer(steer);
//                        Thread.sleep(10);
//                    } catch (Exception ex) {
//                        Logger.getLogger(VirtualRobotSimulator.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//            }
//        }.start();
//    }
    public Wheels getWheels() {
        return wheels;
    }

    public static void main(String[] args) throws Exception {

        final VirtualRobotSimulator robot = new VirtualRobotSimulator();

//        robot.birdMap

        robot.startDrivingThread();

    }

    void showView() {
        MapView mapView = birdMap.mapView;
        mapView.eyelock = estimator;

//        mapView.addLayer(new FloorLayer(density.getSolids()));
        mapView.addLayer(estimator.track);
        mapView.addLayer(new RobotImgLayer(estimator));
    }
}
