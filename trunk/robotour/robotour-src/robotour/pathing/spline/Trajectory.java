package robotour.pathing.spline;

import java.awt.Color;
import java.util.ArrayList;
import robotour.gui.map.LocalPoint;
import robotour.gui.map.MapLayer;
import robotour.gui.map.MapView;

/**
 *
 * @author Kotuc
 */
public class Trajectory implements MapLayer {

    private final ArrayList<PointDef> defs = new ArrayList<PointDef>();
    
//    private final LinkedList<RoadPoint> path;
//    private final LinkedList<Point3d> points;
//    private final LinkedList<Vector3d> vectors;
//    private final double hand = 0.25; // hand right positive

//    public Trajectory(LinkedList<RoadPoint> path) {
//        this(path, 0);
//    }

//    public Trajectory(LinkedList<RoadPoint> path, double hand) {
//        this.path = path;
//
//        this.points = new LinkedList<Point3d>();
//        for (int i = 0; i < path.size(); i++) {
//            points.add(toHandPoint(i, hand));
//        }
//
//        this.vectors = new LinkedList<Vector3d>();
//        for (int i = 0; i < path.size(); i++) {
////            Point3d pos1 = pointClamp(i - 1);
//            Point3d pos1 = pointClamp(i);
//            Point3d pos2 = pointClamp(i + 1);
//            Vector3d vec = new Vector3d(pos2);
//            vec.sub(pos1);
//            if (vec.lengthSquared() > 0.001) {
//                vec.normalize();
//            }
//            vectors.add(vec);
//        }
//    }

    public Trajectory() {       
        
    }

    public LocalPoint interpolate(double t) {
        int floor = (int) Math.floor(t);

//        t -= floor;

        PointDef pd0 = get(floor);
        PointDef pd1 = get(floor+1);
        
        double[] w = getWeights(t - floor);

        return new LocalPoint(
                w[0] * pd0.p.getX() + w[1] * pd1.p.getX() + w[2] * pd0.v.getX() + w[3] * pd1.v.getX(),
                w[0] * pd0.p.getY() + w[1] * pd1.p.getY() + w[2] * pd0.v.getY() + w[3] * pd1.v.getY());

        // linear
//        Point3d pos0 = new Point3d(pointClamp(floor));
//        Point3d pos1 = pointClamp(floor + 1);

//        pos0.interpolate(pos1, t);
//        return pos0;
    }

//    private Point3d pointClamp(int i) {
//        return points.get(clamp(i));
//    }
//
//    private Vector3d vecClamp(int i) {
//        return vectors.get(clamp(i));
//    }

    /**
     *  @return point shifted to the hand side from path
     */
//    Point3d toHandPoint(int i, double hand) {
//        Point3d pos1 = path.get(clamp(i - 1)).getPos();
//        Point3d pos2 = path.get(clamp(i + 1)).getPos();
//        Vector3d vec = new Vector3d(pos2.y - pos1.y, pos1.x - pos2.x, 0);
//        Point3d pos = new Point3d(path.get(clamp(i)).getPos());
//        if (vec.lengthSquared() > 0.1) {
//            vec.normalize();
//            vec.scale(hand);
//
//            pos.add(vec);
//        }
//        return pos;
//    }

    private int clamp(int i) {
        return Math.max(0, Math.min(i, defs.size() - 1));
    }


    PointDef get(int i) {
        return defs.get(clamp(i));
    }

    /**
     * t in 0 .. 1
     * @param t
     * @return p0, p1, v0, v1
     */
    private double[] getWeights(double t) {
        // hermit
        return new double[]{
                    2 * t * t * t - 3 * t * t + 1,
                    -2 * t * t * t + 3 * t * t,
                    1 * t * t * t - 2 * t * t + 1 * t,
                    1 * t * t * t - 1 * t * t
                };
//        return new double[]{
//                    1 + t * (t * (-3 + t * (2))),
//                    t * (t * (3 + t * (-2))),
//                    t * (1 + t * (- 2 + t * (1))),
//                    t * (t * (-1 + t * (1)))
//                };

    }

    int numPoints() {
        return defs.size();
    }

    public void paint(MapView map) {
        
        for (PointDef pd : defs) {
            map.getGraphics().setColor(Color.red);
            map.drawOval(pd.p, 0.1);
            map.getGraphics().setColor(Color.yellow);
            map.drawLine(pd.p, pd.p.add(pd.v), 0.02);
        }

        map.getGraphics().setColor(Color.white);
        double t = 0;
        LocalPoint p = interpolate(t);
        while (t < 1) {
            t+=0.01;
            LocalPoint p2 = interpolate(t);
            map.drawLine(p, p2, 0.01);
            p = p2;
        }
    }

    void add(LocalPoint p, LocalPoint v) {
        defs.add(new PointDef(p, v));
    }

//    double length() {
//        return path.size();
//    }

    class PointDef {
        LocalPoint p;
        LocalPoint v;

        public PointDef() {
        }

        public PointDef(LocalPoint p, LocalPoint v) {
            this.p = p;
            this.v = v;
        }

    }



}
