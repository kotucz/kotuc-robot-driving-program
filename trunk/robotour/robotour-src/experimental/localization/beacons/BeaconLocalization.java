/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package experimental.localization.beacons;

import robotour.gui.map.Paintable;
import robotour.gui.map.gps.MapView;
import robotour.navi.basic.Point;
import robotour.navi.basic.Pose;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

import robotour.gui.map.MapLayer;
import robotour.gui.map.RobotImgLayer;
import robotour.navi.basic.Azimuth;

/**
 *
 * @author Kotuc
 */
public class BeaconLocalization implements MapLayer {

    List<Point> beacons = new ArrayList<Point>();
    Pose pose;
    RobotImgLayer robotImg;
    Pose sol;

    public BeaconLocalization() {
        beacons.add(new Point(-1.5, 2.1));
        beacons.add(new Point(-1.5, 0));

//        beacons.add(new Point(1.5, 1.05));

        beacons.add(new Point(1.5, 2.1));
        beacons.add(new Point(1.5, 0));

        Point center = new Point(0.25, 1.5);
        Azimuth azim = Azimuth.valueOfDegrees(0);

        pose = new Pose(center, azim);

        robotImg = new RobotImgLayer(pose);

    }

    double[] detectBeacons() {
        return detectBeaconsSimulate(pose);
    }

    /**
     *
     * @param pose
     * @return angles relative to robot's front
     */
    double[] detectBeaconsSimulate(Pose pose) {
        double[] angles = new double[beacons.size()];

        int i = 0;
        for (Point beacon : beacons) {
            angles[i++] = pose.getAzimuth().radians() - pose.getPoint().getAzimuthTo(beacon).radians();
        }

        return angles;
    }

    public void paint(Paintable map) {

        map.getGraphics().setColor(Color.ORANGE);

        map.getGraphics().setColor(Color.RED);

        for (Point point : beacons) {
            map.fillOval(point, 0.04);
        }

        robotImg.paint(map);

        map.getGraphics().setColor(Color.YELLOW);

        visializeScan(map, pose);

        map.getGraphics().setColor(Color.CYAN);

//        visializeScan(map, sol);
        visializeScan(map, sol, detectBeacons());

        map.getGraphics().setColor(Color.RED);

        visializeAngleCircles(map, pose);

        int i = 0;
        for (Point point : beacons) {
            map.drawString("" + solvdists[i], point);
            i++;
        }


    }

    void visializeAngleCircles(Paintable map, Pose pose) {

        Point2d pos = pose.getPoint().toPoint2d();

        int bsize = beacons.size();

        for (int i = 0; i < bsize; i++) {
            Point2d beac1 = beacons.get((i + 0) % bsize).toPoint2d();
            Point2d beac2 = beacons.get((i + 1) % bsize).toPoint2d();

            Vector2d vec1 = new Vector2d();
            vec1.sub(beac1, pos);
            Vector2d vec2 = new Vector2d();
            vec2.sub(beac2, pos);

            double dist12 = beac1.distance(beac2);

            double alpha = vec1.angle(vec2);
//        double alpha = Math.PI-vec1.angle(vec2);

            double radius = dist12 / (2 * Math.sin(alpha));

            double dist12S = radius * Math.cos(alpha);

            Point2d center = new Point2d();
            center.interpolate(beac1, beac2, 0.5);

//        map.drawOval(new Point(center.x, center.y), radius);

            Vector2d vecB = new Vector2d();
            vecB.sub(beac2, beac1);
            if (vecB.x * vec2.y - vecB.y * vec2.x > 0) {
                vecB.scale(-1);
            }
            vecB = new Vector2d(-vecB.y, vecB.x);
            vecB.normalize();
            vecB.scale(dist12S);


            center.add(vecB);

            map.drawOval(new Point(center.x, center.y), radius);
        }
    }
    double[] solvdists = new double[4];

    void visializeScan(Paintable map, Pose pose) {

        Point center = pose.getPoint();
        Azimuth azim = pose.getAzimuth();

        double[] scan = simulateScan(detectBeaconsSimulate(pose));

        Point end0 = center;

        for (int i = 0; i < scan.length; i++) {
            double d = scan[i];

            Point end = center.move(
                    Azimuth.valueOfRadians(azim.radians() - (2 * Math.PI * i / scan.length)), d);

            map.drawLine(end, end0, 0.001);

            end0 = end;
        }
    }

    void visializeScan(Paintable map, Pose pose, double[] angles) {

        Point center = pose.getPoint();
        Azimuth azim = pose.getAzimuth();

        double[] scan = simulateScan(angles);

        Point end0 = center;

        for (int i = 0; i < scan.length; i++) {
            double d = scan[i];
//            d = 1 / (Math.random() + 0.01);
            Point end = center.move(
                    Azimuth.valueOfRadians(azim.radians() - (2 * Math.PI * i / scan.length)), d);

            map.drawLine(end, end0, 0.001);

            end0 = end;
        }
    }

    double[] simulateScan(double[] relativeBeaconAngles) {
        double[] scan = new double[360];

//        for (int i = 0; i < scan.length; i++) {
//            scan[i] = 0.5 + 0.01*Math.random();
//        }

        for (double d : relativeBeaconAngles) {
            // fuck negative modulo
            int ang = (int) Math.floor((3600 + Math.toDegrees(d)));
//            scan[(ang + 1) % scan.length] = 3;
            scan[ang % scan.length] = 5;
//            scan[(ang - 1) % scan.length] = 3;
        }
        return scan;
    }

    public static void main(String[] args) {

        BeaconLocalization localization = new BeaconLocalization();

        PoseSolver solver = new PoseSolver(localization, localization.sol =
                new Pose(new Point(0, 1.05), Azimuth.valueOfDegrees(0)));

        MapView view = new MapView();
        view.addLayer(localization);
        view.addLayer(new RobotImgLayer(solver.sol));

//TOOD        view.zoomTo(new Point(0, 1), 25);
        view.showInFrame().setSize(640, 480);

        new Thread(solver).start();

    }
}
