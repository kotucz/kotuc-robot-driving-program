package robotour.hardware.arduino;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import robotour.gui.map.FloorLayer;
import robotour.gui.map.LocalPoint;
import robotour.gui.map.MapView;
import robotour.gui.map.RobotImgLayer;
import robotour.iface.Wheels;
import robotour.navi.gps.Azimuth;
import robotour.navi.local.beacons.RobotPose;
import robotour.navi.local.birdseye.EventPoseEstimator;
import robotour.navi.local.birdseye.Exploring;
import robotour.navi.local.birdseye.SonarDensity;
import vision.pathfinding.birdseye.RealFloor;

/**
 *
 * @author Kotuc
 */
public class VirtualRobotSimulator {

    public final EventPoseEstimator estimator = new EventPoseEstimator(
            new RobotPose(new LocalPoint(0, 0), Azimuth.NORTH));
    private final Wheels wheels = estimator;
    RealFloor floor = new RealFloor();
    SonarDensity density = new SonarDensity(null);
    Exploring exploring = new Exploring(floor, density);

    public VirtualRobotSimulator() {
        int x0 = floor.getX(0);
        int y0 = floor.getX(0);

        floor.fillRect(x0 - 3, y0 - 3, 5, 20, 0);

        floor.fillRect(x0 - 3, y0 + 19, 30, 5, 0);

        floor.fillRect(x0 + 20, y0 + 21, 5, 50, 0);

    }

    void startDrivingThread() {
        new Thread() {

            @Override
            public void run() {
                while (true) {
                    try {
                        exploring.explore(estimator);

                        double speed = 0.4;
                        double steer = exploring.randomSearch(estimator, speed, null);
                        wheels.setSpeed(speed);
                        wheels.setSteer(steer);
                        Thread.sleep(10);
                    } catch (Exception ex) {
                        Logger.getLogger(VirtualRobotSimulator.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }.start();
    }

    public Wheels getWheels() {
        return wheels;
    }

    public static void main(String[] args) throws Exception {

        final VirtualRobotSimulator robot = new VirtualRobotSimulator();

        robot.showView();

        robot.startDrivingThread();

    }

    public void showView() {
        MapView mapView = new MapView();
        mapView.eyelock = estimator;

        mapView.addLayer(new FloorLayer(floor));
        mapView.addLayer(estimator.track);
        mapView.addLayer(new RobotImgLayer(estimator));
        final Exploring expl = new Exploring(floor, density);
        expl.explore(estimator);
        mapView.addLayer(expl);

//        System.err.println("Showing");

        JFrame showInFrame = mapView.showInFrame();
        showInFrame.setLocation(500, 0);
    }
}
