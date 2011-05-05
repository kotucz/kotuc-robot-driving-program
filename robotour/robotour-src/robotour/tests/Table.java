package robotour.tests;

import robotour.behavior.impl.PathDriver;
import robotour.localization.odometry.PassiveOdometry;
import robotour.gui.map.LocalPath;
import robotour.navi.basic.LocalPoint;
import java.awt.Color;
import robotour.driving.BlindCompassPilot;
import robotour.gui.map.MapView;
import robotour.util.RobotSystems;

/**
 *
 * @author Tomas
 */
public class Table {

    public static void main(String[] args) {
        RobotSystems systems = RobotSystems.getDefault();

        MapView view = new MapView();
        view.showInFrame();

        LocalPath localPath = new LocalPath();
        localPath.append(new LocalPoint(-1, -1));
        localPath.append(new LocalPoint(-1, 2));
        localPath.append(new LocalPoint(1, 2));
        localPath.append(new LocalPoint(1, -1));
        localPath.append(new LocalPoint(-1, -1));
        view.addLayer(localPath);

        final PassiveOdometry odometry = new PassiveOdometry(systems.getEncoder(), systems.getCompass());
//        view.addLayer(
//                new RobotImgLayer(odometry, systems.getCompass()));
        odometry.start();

        LocalPath drivePath = new LocalPath();
        drivePath.setColor(Color.GREEN);
        drivePath.append(new LocalPoint(0, 0));
        drivePath.append(new LocalPoint(-0.5, 0.5));
        drivePath.append(new LocalPoint(0, 1));
        drivePath.append(new LocalPoint(0.5, 0.5));
        drivePath.append(new LocalPoint(0, 0));
        drivePath.append(new LocalPoint(-0.5, 0.5));
        drivePath.append(new LocalPoint(0, 1));
        drivePath.append(new LocalPoint(0.5, 0.5));
        drivePath.append(new LocalPoint(0, 0));
        
        view.addLayer(drivePath);

        BlindCompassPilot pilot = new BlindCompassPilot(systems.getWheels(), systems.getCompass());
//        pilot.travel(1, false);
//        pilot.rotateTo(Azimuth.valueOfDegrees(135), false);
        PathDriver pathDriver = new PathDriver(drivePath, pilot);
        pathDriver.run();

        System.exit(0);
    }
}
