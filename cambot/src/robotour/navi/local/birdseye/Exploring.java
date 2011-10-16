package robotour.navi.local.birdseye;

import robotour.localization.odometry.PoseEstimator;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import robotour.navi.basic.LocalPoint;
import robotour.gui.map.MapLayer;
import robotour.gui.map.MapView;
import robotour.navi.basic.RobotPose;
import vision.pathfinding.birdseye.RealFloor;
import vision.pathfinding.knowledge.DoubleMatrix;

/**
 *
 * @author Kotuc
 */
public class Exploring implements MapLayer {

    final private RealFloor floor;
    final private SonarDensity density;
    final private PoseEstimator estimator = new PoseEstimator();
    private LocalPoint target;

    public Exploring(RealFloor floor, SonarDensity density) {
        this.floor = floor;
        this.density = density;
    }

    public void setTarget(LocalPoint target) {
        this.target = target;
    }

    public void paint(MapView map) {
//        simpleSearch(map, pose0);
//        recurSearch(map, pose0, 15);
//        randomSearch(pose0, 0.5, map);
        paintPaths(rpaths, map);
    }

    private List<Path> rpaths = new ArrayList<Path>();

    class Path {

        double length;
        double totarget;
        boolean hasobstacle = false;
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
    public double recomendedSpeed;
    public double beststeer;

    public double randomSearch(RobotPose start, final double speed) {
//        System.out.println("cost "+cost);
//        final double speed = 0.5;
        final double period = 0.5;//0.25;

        double steersum = 0;
        double weightsum = 0;

        double minToTarget = Double.POSITIVE_INFINITY;

        double sumlength = 0;
        int numobstacle = 0;

        int i = 1000; // max length sum
        int j = 50; // num paths

        List<Path> paths = new ArrayList<Path>();

        while (i > 0 && j > 0) {
            j--;
            double cost = 15;
            RobotPose pose = new RobotPose(start);

            Path path = new Path();
            paths.add(path);
            path.addPose(pose);

            while (cost > 0) {
                i--;
//                    double steer = j * 0.3;
                double steer = (2 * (Math.random() - 0.5));
                path.addSteer(steer);

                RobotPose pose1 = estimator.transform(pose, speed, steer, period);
                double sum = getSum(pose1.getPoint());
                if (sum > 5) {
                    path.hasobstacle = true;
                    numobstacle += 1;
                }
                cost -= sum+1; // to ensure convergence
                path.addPose(pose1);
                pose = pose1;
            }

            path.length = start.getPoint().getDistanceTo(pose.getPoint());
            path.weight = Math.pow(path.length, 5);
            path.totarget = pose.getPoint().getDistanceTo(target);
            minToTarget = Math.min(minToTarget, path.totarget);
        }

//        double avglength = sumlength / paths.size();
        double probobstacle = (double) numobstacle / paths.size();

        recomendedSpeed = speed * (1 - probobstacle);
//        recomendedSpeed = speed * (avglength / 1.0);

        for (Path path : paths) {
//            path.weight -= 1 - Math.pow((path.totarget + 1) / (minToTarget + 1), 2);
//            path.weight = Math.max(0, path.weight);
            double csteer = path.steers.get(0);
            steersum += csteer * path.weight;
            weightsum += path.weight;
        }

        this.rpaths = paths;

        beststeer = (1 + 2*probobstacle) * steersum / weightsum;
        return beststeer;
    }

    private void paintPaths(List<Path> paths, MapView map) {
//        List<Path> paths = new ArrayList<Path>();

        RobotPose start = null;

        for (Path path : paths) {

            RobotPose pose0 = path.poses.get(0);
            start = pose0;

            for (RobotPose pose1 : path.poses) {

                double sum = getSum(pose1.getPoint());

///                map.getGraphics().setColor(new Color((int) (50 * sum), 255, 0));
//            map.getGraphics().setColor(Color.yellow);
                if (sum > 5) {
                    map.getGraphics().setColor(Color.red);
                } else {
                    map.getGraphics().setColor(new Color((int) (50 * sum), 255, 0));
                }
                map.drawLine(pose0.getPoint(), pose1.getPoint(), 0.03);
                pose0 = pose1;
            }
        }
//                    map.getGraphics().setColor(new Color((int) (50 * sum), 255, 0));
        if (start != null) {
            map.getGraphics().setColor(Color.blue);
            for (int k = 0; k < 100; k++) {
                RobotPose pose1 = estimator.transform(start, recomendedSpeed, beststeer, 0.5);
                map.drawLine(start.getPoint(), pose1.getPoint(), 0.03);
                start = pose1;
            }
        }

    }

//// return best steer
//    public double randomSearchMerged(RobotPose start, final double speed, MapView map) {
////        System.out.println("cost "+cost);
////        final double speed = 0.5;
//        final double period = 0.5;//0.25;
//
//        double steersum = 0;
//        double weightsum = 0;
//
//        double minToTarget = Double.POSITIVE_INFINITY;
//
//        double sumlength = 0;
//        int numobstacle = 0;
//
//        int i = 1000;
//        int j = 100;
//
//        List<Path> paths = new ArrayList<Path>();
//
//        while (i > 0 && j > 0) {
//            j--;
//            double cost = 15;
//            RobotPose pose = start;
//
//            Path path = new Path();
//            paths.add(path);
//
//            while (cost > 0) {
//                i--;
////                    double steer = j * 0.3;
//                double steer = (2 * (Math.random() - 0.5));
//                path.addSteer(steer);
//
//                RobotPose pose1 = estimator.transform(pose, speed, steer, period);
//                double sum = getSum(pose1.getPoint());
//                if (sum > 5) {
//                    path.hasobstacle = true;
//                    numobstacle +=
//                            1;
//                }
//
//                if (map != null) {
////                    map.getGraphics().setColor(new Color((int) (50 * sum), 255, 0));
////            map.getGraphics().setColor(Color.yellow);
//                    if (sum > 5) {
//                        map.getGraphics().setColor(Color.red);
//                    } else {
//                        map.getGraphics().setColor(new Color((int) (50 * sum), 255, 0));
//                    }
//
//                    map.drawLine(pose.getPoint(), pose1.getPoint(), 0.03);
//                }
//
//                cost -= sum; // to ensure convergence
//                pose =
//                        pose1;
//            }
//
//            path.length = start.getPoint().getDistanceTo(pose.getPoint());
//            path.weight = Math.pow(path.length, 5);
//            path.totarget = pose.getPoint().getDistanceTo(target);
//            minToTarget =
//                    Math.min(minToTarget, path.totarget);
//        }
//
////        double avglength = sumlength / paths.size();
//        double probobstacle = numobstacle / paths.size();
//
//        recomendedSpeed =
//                speed * (probobstacle / 1.0);
////        recomendedSpeed = speed * (avglength / 1.0);
//
//        for (Path path : paths) {
////            path.weight -= 1 - Math.pow((path.totarget + 1) / (minToTarget + 1), 2);
////            path.weight = Math.max(0, path.weight);
//            double csteer = path.steers.get(0);
//            steersum +=
//                    csteer * path.weight;
//            weightsum +=
//                    path.weight;
//        }
//
//        double beststeer = steersum / weightsum;
//        if (map != null) {
////                    map.getGraphics().setColor(new Color((int) (50 * sum), 255, 0));
//            map.getGraphics().setColor(Color.blue);
//            for (int k = 0; k <
//                    100; k++) {
//                RobotPose pose1 = estimator.transform(start, speed, beststeer, period);
//                map.drawLine(start.getPoint(), pose1.getPoint(), 0.03);
//                start =
//                        pose1;
//            }
//
//        }
//        return beststeer;
//    }
    double recurSearch(MapView map, RobotPose pose, double cost) {
//        System.out.println("cost "+cost);
        if (cost > 0) {
            for (int j = -2; j <
                    3; j++) {
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
        for (int i = 0; i <
                10; i++) {
            RobotPose bestPose = null;
            double bestsum = 5;
            for (int j = -2; j <
                    3; j++) {
                double speed = 0.5;
                double steer = j * 0.3;
//                double steer = (2 * (Math.random() - 0.5));
                RobotPose pose1 = estimator.transform(pose, speed, steer, 0.25);
                double sum = getSum(pose1.getPoint());
                if (sum > 5) {
                    map.getGraphics().setColor(Color.red);
                } else {
                    map.getGraphics().setColor(new Color((int) (50 * sum), 255, 0));
                }
//            map.getGraphics().setColor(Color.yellow);

                map.drawLine(pose.getPoint(), pose1.getPoint(), 0.1);
                if (sum < bestsum) {
                    bestsum = sum;
                    bestPose =
                            pose1;
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

        sum +=
                fmatrix.get(x, y);
        sum +=
                fmatrix.get(x + 1, y);
        sum +=
                fmatrix.get(x - 1, y);
        sum +=
                fmatrix.get(x, y + 1);
        sum +=
                fmatrix.get(x, y - 1);


        x =
                density.solids.getX(point.getX());
        y =
                density.solids.getY(point.getY());
        double exp = 1;
        double mul = 15;

        sum +=
                Math.pow(density.getFieldDensity(x, y), exp) * mul;
        sum +=
                Math.pow(density.getFieldDensity(x + 1, y), exp) * mul;
        sum +=
                Math.pow(density.getFieldDensity(x - 1, y), exp) * mul;
        sum +=
                Math.pow(density.getFieldDensity(x, y + 1), exp) * mul;
        sum +=
                Math.pow(density.getFieldDensity(x, y - 1), exp) * mul;


        return sum;
    }
}
