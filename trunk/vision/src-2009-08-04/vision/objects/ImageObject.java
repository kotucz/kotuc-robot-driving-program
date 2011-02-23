package vision.objects;

import vision.ar.AugmentedPatternSearch;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.media.j3d.Transform3D;
import javax.vecmath.Matrix4d;
import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;
import vision.CameraProportions;
import vision.Dir4;
import vision.Dir8;
import vision.ar.CornerDetector;
import vision.ar.Pattern;
import vision.ar.PatternMatcher;

public class ImageObject {

    private int minx;
    private int maxx;
    private int miny;
    private int maxy;
    private int width;
    private int height;
    private int surface;
    private int color;
    Set<Point> area;
    int[] verticalProj = new int[width];
    int[] horizontalProj = new int[height];

    ImageObject() {
        this.area = new HashSet<Point>();
    }

    ImageObject(Set<Point> area) {
        parse(area);
    }

    public void parse(Set<Point> area) {

        this.area = area;

        this.area = dilate();
        
        minx = Integer.MAX_VALUE;
        maxx = Integer.MIN_VALUE;
        miny = Integer.MAX_VALUE;
        maxy = Integer.MIN_VALUE;

        massCenter = new Point();

        for (Point point : area) {
            if (point.x > maxx) {
                ecorn = point;
            }
            if (point.x < minx) {
                wcorn = point;
            }
            if (point.y < miny) {
                ncorn = point;
            }
            if (point.y > maxy) {
                scorn = point;
            }
            minx = Math.min(minx, point.x);
            maxx = Math.max(maxx, point.x);
            miny = Math.min(miny, point.y);
            maxy = Math.max(maxy, point.y);

            massCenter.x += point.x;
            massCenter.y += point.y;
        }

        width = maxx - minx;
        height = maxy - miny;

        surface = area.size();

        massCenter.x /= surface;
        massCenter.y /= surface;

        center = new Point(minx + (width / 2), miny + (height / 2));
    }
    private Point massCenter;

    public int getColor() {
        return color;
    }

    public int getHeight() {
        return height;
    }

    public int getMaxX() {
        return maxx;
    }

    public int getMaxY() {
        return maxy;
    }

    public int getMinX() {
        return minx;
    }

    public int getMinY() {
        return miny;
    }

    public int getSurface() {
        return surface;
    }

    public int getWidth() {
        return width;
    }
    Point center;

    public Point getCenter() {
        return center;
    }

    public Point getMassCenter() {
        return massCenter;
    }

    public void setColor(int color) {
        this.color = color;
    }
    private Point ncorn,  ecorn,  scorn,  wcorn;

    public Point[] getCorners() {
        return new Point[]{
                    ncorn, ecorn, scorn, wcorn
                };
    }

    public void focusCorners(BufferedImage original) {
        AugmentedPatternSearch aps = new AugmentedPatternSearch();
        ncorn = CornerDetector.getLocalCornerMaximum(original, ncorn.x - 10, ncorn.y - 10, 20, 20);
        ecorn = CornerDetector.getLocalCornerMaximum(original, ecorn.x - 10, ecorn.y - 10, 20, 20);
        scorn = CornerDetector.getLocalCornerMaximum(original, scorn.x - 10, scorn.y - 10, 20, 20);
        wcorn = CornerDetector.getLocalCornerMaximum(original, wcorn.x - 10, wcorn.y - 10, 20, 20);
    }

    /**
     *
     * borderLength*borderLength/surfaceSiźe
     *
     * draws an oval around the object's bounds
     * counts pixels being in oval xor in the object area
     */
    public double getEccentricity() {
        return Math.pow(border.size(), 2) / getSurface();
//        double radius = Math.max(width, height);
//        int eccentricity = 0;
//        Point point = new Point();
//        for (point.y = miny; point.y < maxy; point.y++) {
//            for (point.x = minx; point.x < maxx; point.x++) {
//                if ((Math.hypot(point.x - center.x, point.y - center.y) <= radius) !=
//                        (area.contains(point))) {
//                    eccentricity++;
//                }
//            }
//        }
//        return (double) eccentricity / (Math.PI * radius * radius);
    }
    List<Point> border;

    /**
     * not correct. should mark inner border edge pixels of ojbect
     *
     * Around the border counter clock vise
     *
     * Exploring this way:
     *
     *   321  came from 6
     *   4PX  P - current point
     *   56X  X - cannot be contained in 8neighbour
     *
     * @return
     */
    public List<Point> findBorder() {

        border = new LinkedList<Point>();

        Point point = ecorn;

        Dir8 dir = Dir8.N;

//        while (true) {
        for (int i = 0; i < getSurface(); i++) {

            dir = dir.rotate(1);
            Point next = new Point(point.x + dir.dx(), point.y + dir.dy());
            while (!contains(next)) {
                dir = dir.rotate(-1);
                next.x = point.x + dir.dx();
                next.y = point.y + dir.dy();
            }

            point = next;

//            System.out.println("" + point);

//            if (border.contains(next)) {
            if (next.equals(ecorn)) {
                break;
            }

            border.add(next);

        }


        System.out.println("border length: " + border.size());

        return border;

    }

    public void drawBorder(Graphics g) {
        for (Point point : findBorder()) {
            g.drawRect(point.x, point.y, 1, 1);
        }
    }

    public Set<Point> erode() {
        Point ep = new Point();
        Set<Point> newArea = new HashSet<Point>();
        big:for (Point point : area) {
            for (Dir4 dir : Dir4.values()) {
                ep.x = point.x + dir.dx();
                ep.y = point.y + dir.dy();
                if (!area.contains(ep)) {
                    continue big;
                }
            }
            newArea.add(point);
        }
        return newArea;
    }

    public Set<Point> dilate() {
        Point ep = new Point();
        Set<Point> newArea = new HashSet<Point>();
        for (Point point : area) {
            for (Dir4 dir : Dir4.values()) {
                ep.x = point.x + dir.dx();
                ep.y = point.y + dir.dy();
                if (newArea.add(ep)) {
                    ep = new Point();
                }
            }
        }
        return newArea;
    }

    public Collection<Point> findOutline() {
        Point ep = new Point();
        Set<Point> outline = new HashSet<Point>();
        for (Point point : area) {
            for (Dir8 dir : Dir8.values()) {
                ep.x = point.x + dir.dx();
                ep.y = point.y + dir.dy();
                if (!contains(ep)) {
                    outline.add(ep);
                    ep = new Point();
                    break;
                }
            }
        }
        return outline;
    }

    public void drawOutline(Graphics g) {
        for (Point point : findOutline()) {
            g.drawRect(point.x, point.y, 1, 1);
        }
    }

    public void findConvexBorder() {
    }


//    public boolean contains(int x, int y) {
//        return area.contains(new Point(x, y));
//    }
    public boolean contains(Point point) {
//        System.out.println("contains " + point + "?");
        return area.contains(point);
    }

    /**
     * @return normalized pattern of sqare object to be compared via PatternMatcher
     */
    public Pattern getPattern() {
        int pw = 90;
        int ph = 90;
        Pattern pattern = new Pattern(pw, ph);

        Vector2d a = new Vector2d(ncorn.x, ncorn.y);
        Vector2d b = new Vector2d(ecorn.x, ecorn.y);
        Vector2d c = new Vector2d(scorn.x, scorn.y);
        Vector2d d = new Vector2d(wcorn.x, wcorn.y);

        Vector2d m = new Vector2d();
        Vector2d n = new Vector2d();
        Vector2d f = new Vector2d();
        for (int x = 0; x < pw; x++) {
            for (int y = 0; y < ph; y++) {
                m.interpolate(a, b, (double) x / pw);
                n.interpolate(d, c, (double) x / pw);
                f.interpolate(m, n, (double) y / ph);
                pattern.set(x, y, this.contains(new Point((int) f.x, (int) f.y)));
            }
        }
        return pattern;
    }

    /**
     *
     * moved to PatternMatcher
     * @param original
     * @return normalized pattern of sqare object to be compared via PatternMatcher
     */
    public Pattern getPattern(BufferedImage original) {
        return PatternMatcher.snapPattern(original, ncorn, ecorn, scorn, wcorn);
//        int pw = 90;
//        int ph = 90;
//        BufferedImage pattern = new BufferedImage(pw, ph, BufferedImage.TYPE_INT_RGB);
//        Graphics g = pattern.createGraphics();
//        Vector2d a = new Vector2d(ncorn.x, ncorn.y);
//        Vector2d b = new Vector2d(ecorn.x, ecorn.y);
//        Vector2d c = new Vector2d(scorn.x, scorn.y);
//        Vector2d d = new Vector2d(wcorn.x, wcorn.y);
//
//        Vector2d m = new Vector2d();
//        Vector2d n = new Vector2d();
//        Vector2d f = new Vector2d();
//        for (int x = 0; x < pw; x++) {
//            for (int y = 0; y < ph; y++) {
//                m.interpolate(a, b, (double) x / pw);
//                n.interpolate(d, c, (double) x / pw);
//                f.interpolate(m, n, (double) y / ph);
//                pattern.setRGB(x, y, original.getRGB((int) f.x, (int) f.y));
//            }
//        }
//        return pattern;
    }

    /**
     *
     * @return array containing x and y axis of the object's coordinates
     */
    public Vector2d[] getAxes2d() {

        Vector2d a = new Vector2d(ncorn.x, ncorn.y);
        Vector2d b = new Vector2d(ecorn.x, ecorn.y);
        Vector2d c = new Vector2d(scorn.x, scorn.y);
        Vector2d d = new Vector2d(wcorn.x, wcorn.y);

        Vector2d ctod = new Vector2d();
        ctod.sub(d, c);
        Vector2d atod = new Vector2d();
        atod.sub(d, a);
        Vector2d atob = new Vector2d();
        atob.sub(b, a);

        Vector2d x = new Vector2d();
//        x.scaleAdd(2, atob, a);
        x.scaleAdd(-(ctod.x * atod.y - ctod.y * atod.x) / (atob.x * ctod.y - atob.y * ctod.x), atob, a);


        Vector2d dtoa = new Vector2d();
        dtoa.sub(a, d);
        Vector2d btoa = new Vector2d();
        btoa.sub(a, b);
        Vector2d btoc = new Vector2d();
        btoc.sub(c, b);

        Vector2d y = new Vector2d();
//        x.scaleAdd(2, atob, a);
        y.scaleAdd(-(dtoa.x * btoa.y - dtoa.y * btoa.x) / (btoc.x * dtoa.y - btoc.y * dtoa.x), btoc, b);

        return new Vector2d[]{x, y};

    }

    /**
     *
     * @param camProp
     * @return array containing x, y and z axis of the object's coordinates
     */
    public Vector3d[] getAxes3d(CameraProportions camProp) {

        Point2d a = new Point2d(ncorn.x, ncorn.y);
        Point2d b = new Point2d(ecorn.x, ecorn.y);
        Point2d c = new Point2d(scorn.x, scorn.y);
        Point2d d = new Point2d(wcorn.x, wcorn.y);

//        Vector2d ctod = new Vector2d();
//        ctod.sub(d, c);
//        Vector2d atod = new Vector2d();
//        atod.sub(d, a);
//        Vector2d atob = new Vector2d();
//        atob.sub(b, a);

//        Vector2d x2 = new Vector2d();
//        double tX = -(ctod.x * atod.y - ctod.y * atod.x) / (atob.x * ctod.y - atob.y * ctod.x);
//        x2.scaleAdd(tX, atob, a);

        // x axis vanish point
        Point2d x2 = new Point2d();
        double tX = getIntersection(a, b, d, c, x2);
        Vector3d x3;
        if (Double.isInfinite(tX)) {
            System.err.println("infinite tx");
            x3 = new Vector3d(b.x - a.x, b.y - a.y, 0);
        } else {
            x3 = camProp.getProjectionVector(x2.x, x2.y);
            if (tX < 0) {
                x3.scale(-1);
            }
        }

//        Vector2d dtoa = new Vector2d();
//        dtoa.sub(a, d);
//        Vector2d btoa = new Vector2d();
//        btoa.sub(a, b);
//        Vector2d btoc = new Vector2d();
//        btoc.sub(c, b);
//
//        Vector2d y2 = new Vector2d();
////        x.scaleAdd(2, atob, a);
//        double tY = -(dtoa.x * btoa.y - dtoa.y * btoa.x) / (btoc.x * dtoa.y - btoc.y * dtoa.x);
//        y2.scaleAdd(tY, btoc, b);

        // y axis vanish point
        Point2d y2 = new Point2d();
        double tY = getIntersection(b, c, a, d, y2);
        Vector3d y3;
        if (Double.isInfinite(tY)) {
            System.err.println("infinite ty");
            y3 = new Vector3d(c.x - b.x, c.y - b.y, 0);
        } else {
            y3 = camProp.getProjectionVector(y2.x, y2.y);
            if (tY > 0) {
                y3.scale(-1);
            }
        }

        Vector3d z3 = new Vector3d();
        z3.cross(x3, y3);

        return new Vector3d[]{x3, y3, z3};

    }

    /**
     *
     * @param camProp
     * @return transform of the object in camera coordinates
     */
    public Transform3D getTransform(CameraProportions camProp) {
        Vector3d[] axes = getAxes3d(camProp);
        for (Vector3d vector3d : axes) {
            if (Double.isInfinite(vector3d.length())) {
                System.err.println("Infinite: " + vector3d);
                vector3d.normalize();
                System.err.println("Normalized: " + vector3d);
            }
            vector3d.normalize();
        }

        Point2d a = new Point2d(ncorn.x, ncorn.y);
        Point2d b = new Point2d(ecorn.x, ecorn.y);
        Point2d c = new Point2d(scorn.x, scorn.y);
        Point2d d = new Point2d(wcorn.x, wcorn.y);

        // square center - intersection of diagonals
        Point2d centerP = new Point2d();
        getIntersection(a, c, d, b, centerP);
        Vector3d centerDir = camProp.getProjectionVector(centerP.x, centerP.y);

//        centerDir.normalize();


        double realSize = 0.5; // half of 1 m
        Vector3d stob = new Vector3d();
        stob.add(axes[0], axes[1]);
        stob.scale(realSize);

        Vector3d bVect = camProp.getProjectionVector(b.x, b.y);

        // most difficult :-) get centerDir size from left view
        centerDir.scale(-getIntersection(
                new Point2d(), new Point2d(centerDir.z, centerDir.x),
                new Point2d(stob.z, stob.x),
                new Point2d(bVect.z, bVect.x), new Point2d()));



        Point3d pos = new Point3d(centerDir);
        Matrix4d mat = new Matrix4d(
                axes[0].x, axes[1].x, axes[2].x, pos.x,
                axes[0].y, axes[1].y, axes[2].y, pos.y,
                axes[0].z, axes[1].z, axes[2].z, pos.z,
                0, 0, 0, 1);
        return new Transform3D(mat);
    }

    /**
     *
     * intersection of: <br />
     *
     * A + t*(B-A) = D + s*(C-D)
     *
     * @return t vector a scaling coeficient
     */
    private double getIntersection(
            Point2d a, Point2d b, Point2d d, Point2d c, Point2d result) {

        Vector2d ctod = new Vector2d();
        ctod.sub(d, c);
        Vector2d atod = new Vector2d();
        atod.sub(d, a);
        Vector2d atob = new Vector2d();
        atob.sub(b, a);

        double t = -(ctod.x * atod.y - ctod.y * atod.x) / (atob.x * ctod.y - atob.y * ctod.x);
//        System.out.println("t "+t);
        result.scaleAdd(t, atob, a);
        return t;
    }
}
