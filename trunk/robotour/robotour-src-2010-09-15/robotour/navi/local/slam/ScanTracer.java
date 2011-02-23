package robotour.navi.local.slam;

import java.awt.Color;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import robotour.gui.map.LocalPoint;
import robotour.gui.map.MapLayer;
import robotour.gui.map.MapView;
import robotour.navi.gps.Azimuth;
import robotour.navi.local.beacons.RobotPose;

/**
 *
 * @author Kotuc
 */
public class ScanTracer implements MapLayer {

    RobotPose pose;
    double[] scan;
    LocalPoint[] absolute;
    double startAngle = -Math.PI / 2;
    double endAngle = +Math.PI / 2;
    List<LineSegment> findLines;

    public ScanTracer(RobotPose pose, double[] scan) {
        this.pose = pose;
//        this.scan = scan;
//        this.scan = medif(scan);
        this.scan = medifsubsample(scan);
        toAbsolute();
        findLines = findLinesIterative1();
        System.out.println("" + findLines.size() + " lines");
    }

    static double[] medif(double[] in) {
        double[] medi = new double[in.length];
        int border = 2;
        for (int i = border; i < medi.length - border; i++) {
            medi[i] = median(in[i - 2], in[i - 1], in[i], in[i + 1], in[i + 2]);
        }
        return medi;
    }

    static double[] medifsubsample(double[] in) {
        double[] medi = new double[in.length / 5];
        for (int i = 0; i < medi.length; i++) {
//            medi[i] = median(in[5 * i], in[5 * i + 1], in[5 * i + 2], in[5 * i + 3], in[5 * i + 4]);
            medi[i] = median2(in[5 * i], in[5 * i + 1], in[5 * i + 2], in[5 * i + 3], in[5 * i + 4]);
        }
        return medi;
    }

    static double median(double... values) {
        Arrays.sort(values);
        return values[values.length / 2];
    }

    static double median2(double... values) {
        Arrays.sort(values);
        return (values[0] + 2 * values[1] + 4 * values[2] + 2 * values[3] + values[4]) / 10.0;
    }

    List<LineSegment> findLinesIterative2() {
        List<LineSegment> lines = new LinkedList<LineSegment>();
        List<LocalPoint> linepoints = new LinkedList<LocalPoint>();
        int border = 2;
        int starti = border;
        LineSegment line = null;
        for (int endi = starti; endi < absolute.length - border; endi++) {
            if (scan[endi] > 0.8) { // probably erroneous
                continue;
            }

            LocalPoint localPoint = absolute[endi];

            if (linepoints.size() > 5) {
                if (line == null) {
                    line = LineSegment.createSegment(absolute[starti], absolute[endi]);
                    lines.add(line);
//                    line.totalLeastSquaresOptimize(linepoints);
                }
//            System.out.println(""+endi);
                if (line.distanceTo(localPoint) < 0.15) {
                    linepoints.add(localPoint);
                    line.totalLeastSquaresOptimize(linepoints);
                } else {
                    line = null;
                    linepoints.clear();
                    starti = endi;
                }
            } else {
                linepoints.add(localPoint);
            }


        }
//        LineSegment prevline = LineSegment.createSegment(
//                absolute[starti], absolute[absolute.length - 1-border]);
//        lines.add(prevline);
        return lines;
    }

    List<LineSegment> findLinesIterative1() {
        List<LineSegment> lines = new LinkedList<LineSegment>();
        LinkedList<LocalPoint> linepoints = new LinkedList<LocalPoint>();
        int border = 2;
        int starti = border;
        for (int endi = starti; endi < absolute.length - border; endi++) {
//            System.out.println(""+endi);
            if (scan[endi] > 0.8) { // probably not precise scan
                continue;
            }
            LocalPoint localPoint = absolute[endi];
            linepoints.add(localPoint);
            if (starti + 5 < endi) {
                LineSegment line = LineSegment.createSegment(absolute[starti], absolute[endi]);
                line.totalLeastSquaresOptimize(linepoints);
                if (line.totalLeastSquaresSum(linepoints) > 0.0003 * (linepoints.size())) {
//            if (line.totalLeastSquaresSum(linepoints) > 5) {
                    // break and start new line
                    if ((linepoints.size() > 18)) { // only long enough lines
                        linepoints.removeLast();
                        LineSegment prevline = LineSegment.createSegment(
                                absolute[starti], absolute[endi - 1]);
                        prevline.totalLeastSquaresOptimize(linepoints);

                        lines.add(prevline);
                    }
                    linepoints.clear();
                    linepoints.add(localPoint);
                    starti = endi;
                }
            }
        }
//        LineSegment prevline = LineSegment.createSegment(
//                absolute[starti], absolute[absolute.length - 1-border]);
//        lines.add(prevline);
        return lines;
    }

    void toAbsolute() {
        LocalPoint center = pose.getPoint();
        Azimuth azim = pose.getAzimuth();

        absolute = new LocalPoint[scan.length];

        for (int i = 0; i < scan.length; i++) {
            absolute[i] = absolutePos(
                    center, azim, i, scan[i]);
//                    Azimuth.valueOfRadians(azim.radians() - (2 * Math.PI * i / scan.length)), d);

//            map.drawLine(end, end0, 0.001);
        }

    }

    private LocalPoint absolutePos(LocalPoint center, Azimuth azim, int i, double d) {
        LocalPoint end = center.move(Azimuth.valueOfRadians(azim.radians() + startAngle + ((endAngle - startAngle) * i / scan.length)), d);
        //                    Azimuth.valueOfRadians(azim.radians() - (2 * Math.PI * i / scan.length)), d);
        return end;
    }

    public void paint(MapView map) {

        map.getGraphics().setColor(Color.red);

        LocalPoint end0 = absolute[0];

        for (int i = 0; i < scan.length; i++) {
            LocalPoint end = absolute[i];
//                    Azimuth.valueOfRadians(azim.radians() - (2 * Math.PI * i / scan.length)), d);

//            map.drawLine(end, end0, 0.001);

            map.drawDot(end, 0.01);

            end0 = end;
        }

//        end0 = absolute[0];

        map.getGraphics().setColor(Color.green);

//        for (int i = 0; i < scan.length; i += 200) {
//            LocalPoint end = absolute[i];
//            //                    Azimuth.valueOfRadians(azim.radians() - (2 * Math.PI * i / scan.length)), d);
//            LineSegment segment = LineSegment.createSegment(end0, end);
//
//            map.drawLine(segment.getStartPoint(), segment.getEndPoint(), 0.001);
//
////            map.drawDot(end, 0.01);
//
//            end0 = end;
//        }

        for (LineSegment segment : findLines) {
            map.drawLine(segment.getStartPoint(), segment.getEndPoint(), 0.001);
        }


    }
}
