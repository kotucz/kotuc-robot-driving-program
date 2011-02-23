package robotour.navi.local.birdseye;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import robotour.gui.map.LocalPoint;
import robotour.gui.map.MapLayer;
import robotour.gui.map.MapView;
import robotour.navi.local.beacons.RobotPose;
import vision.pathfinding.birdseye.RealFloor;
import vision.pathfinding.knowledge.DoubleMatrix;

/**
 *
 * @author Kotuc
 */
public class Exploring implements MapLayer {

    final RealFloor floor;
    final SonarDensity density;
    PoseEstimator estimator = new PoseEstimator();

    public Exploring(RealFloor floor, SonarDensity density) {
        this.floor = floor;
        this.density = density;
    }
    RobotPose pose0;

    public void explore(RobotPose pose0) {
        this.pose0 = pose0;
    }

    public void paint(MapView map) {
//        simpleSearch(map, pose0);
//        recurSearch(map, pose0, 15);
        randomSearch(pose0, 0.5, map);
    }

    void paintPaths(List<Path> paths, MapView map) {
        for (Path path : paths) {
            // bla bla
        }
    }

    class Path {

        double dist;
        List<Double> steers = new ArrayList<Double>();
        List<RobotPose> poses = new ArrayList<RobotPose>();

        void addSteer(double steer) {
            steers.add(steer);
        }

        void addPose(RobotPose pose) {
            poses.add(pose);
        }
        double weight;
    }

    // return best steer
    public double randomSearch(RobotPose start, final double speed, MapView map) {
//        System.out.println("cost "+cost);
//        final double speed = 0.5;
        final double period = 0.5;//0.25;

        double steersum = 0;
        double weightsum = 0;

        int i = 1000;
        int j = 100;

        List<Path> paths = new ArrayList<Path>();

        while (i > 0 && j > 0) {
            j--;
            double cost = 15;
            RobotPose pose = start;

            Path path = new Path();
            paths.add(path);

            while (cost > 0) {
                i--;
//                    double steer = j * 0.3;
                double steer = (2 * (Math.random() - 0.5));
                path.addSteer(steer);

                RobotPose pose1 = estimator.transform(pose, speed, steer, period);
                double sum = getSum(pose1.getPoint());
                if (map != null) {
                    map.getGraphics().setColor(new Color((int) (50 * sum), 255, 0));
//            map.getGraphics().setColor(Color.yellow);
                    map.drawLine(pose.getPoint(), pose1.getPoint(), 0.03);
                }
                cost -= sum; // to ensure convergence
                pose = pose1;
            }
            double csteer = path.steers.get(0);
            path.dist = start.getPoint().getDistanceTo(pose.getPoint());
            path.weight = Math.pow(path.dist, 5);

            steersum += csteer * path.weight;
            weightsum += path.weight;
        }
        double beststeer = steersum / weightsum;
        if (map != null) {
//                    map.getGraphics().setColor(new Color((int) (50 * sum), 255, 0));
            map.getGraphics().setColor(Color.blue);
            for (int k = 0; k < 100; k++) {
                RobotPose pose1 = estimator.transform(start, speed, beststeer, period);
                map.drawLine(start.getPoint(), pose1.getPoint(), 0.03);
                start = pose1;
            }

        }
        return beststeer;
    }

    double recurSearch(MapView map, RobotPose pose, double cost) {
//        System.out.println("cost "+cost);
        if (cost > 0) {
            for (int j = -2; j < 3; j++) {
                double speed = 0.5;
                double steer = j * 0.3;
//                double steer = (2 * (Math.random() - 0.5));
                RobotPose pose1 = estimator.transform(pose, speed, steer, 0.25);
                double sum = getSum(pose1.getPoint());
                map.getGraphics().setColor(new Color((int) (50 * sum), 255, 0));
//            map.getGraphics().setColor(Color.yellow);
                map.drawLine(pose.getPoint(), pose1.getPoint(), 0.1);
                recurSearch(map, pose1, cost - sum);
            }
        }
        return 0;
    }

    public void simpleSearch(MapView map, RobotPose pose) {
        for (int i = 0; i < 10; i++) {
            RobotPose bestPose = null;
            double bestsum = 5;
            for (int j = -2; j < 3; j++) {
                double speed = 0.5;
                double steer = j * 0.3;
//                double steer = (2 * (Math.random() - 0.5));
                RobotPose pose1 = estimator.transform(pose, speed, steer, 0.25);
                double sum = getSum(pose1.getPoint());
                map.getGraphics().setColor(new Color((int) (50 * sum), 255, 0));
//            map.getGraphics().setColor(Color.yellow);
                map.drawLine(pose.getPoint(), pose1.getPoint(), 0.1);
                if (sum < bestsum) {
                    bestsum = sum;
                    bestPose = pose1;
                }
            }
            pose = bestPose;
        }
    }

    double getSum(LocalPoint point) {
        int x = floor.getX(point.getX());
        int y = floor.getY(point.getY());

        DoubleMatrix fmatrix = floor.matrix;

        double sum = 0;

        sum += fmatrix.get(x, y);
        sum += fmatrix.get(x + 1, y);
        sum += fmatrix.get(x - 1, y);
        sum += fmatrix.get(x, y + 1);
        sum += fmatrix.get(x, y - 1);


        double exp = 2;
        double mul = 5;

        sum += Math.pow(density.getFieldDensity(x, y), exp) * mul;
        sum += Math.pow(density.getFieldDensity(x + 1, y), exp) * mul;
        sum += Math.pow(density.getFieldDensity(x - 1, y), exp) * mul;
        sum += Math.pow(density.getFieldDensity(x, y + 1), exp) * mul;
        sum += Math.pow(density.getFieldDensity(x, y - 1), exp) * mul;


        return sum;
    }
}
