package vision.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;
import vision.ColorFunc;
import vision.OneColorTransform;

/**
 * Can not parse black color issue!
 * @author Tomas Kotula
 */
public class ObjectAnalysis {

    protected static final int JUMP = 1;
    private final int WHITE = 0xFFFFFF;
//    private final int VISITED = 0xFF8889;
    private final int MASK = 0xFFFFFF;
    // init curColor space        
    private final Map<Vector3f, Integer> colorSamples = new HashMap<Vector3f, Integer>();
    private final Vector3f whitevector = new Vector3f(new Color3f(Color.WHITE));

    public ObjectAnalysis() {

        colorSamples.put(new Vector3f(new Color3f(Color.GREEN)), 0x00FF00);
        colorSamples.put(new Vector3f(new Color3f(Color.RED)), 0xFF0000);
        colorSamples.put(new Vector3f(new Color3f(Color.BLUE)), 0x0000FF);
        colorSamples.put(new Vector3f(new Color3f(Color.YELLOW)), 0xFFFF00);
        colorSamples.put(new Vector3f(new Color3f(Color.CYAN)), 0x00FFFF);
    //        colorSamples.put(new Vector3f(new Color3f(Color.WHITE)),    WHITE); 

    }
    private final Map<Integer, Integer> quickMap = new HashMap<Integer, Integer>();

    public BufferedImage roundColors(BufferedImage image) {

        OneColorTransform ct = new OneColorTransform();

        return ct.apply(image, new ColorFunc() {

            public int getColor(int rgb) {
                return quickParse(rgb);
            }
        });

//        iw = image.getWidth();
//        ih = image.getHeight();
//
//        BufferedImage bi = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_RGB);
//        Graphics g = bi.createGraphics();
//
//        int pixels = 0;
//
//        for (int y = 0; y < ih; y++) {
//            for (int x = 0; x < iw; x++) {
//
//                bi.setRGB(x, y, quickParse(image.getRGB(x, y)));
//
//                pixels++;
//
//            }
//        }
//        System.out.println("prepair image: " + pixels + " pxs, quickMap: "+quickMap.size());
//        return bi;
    }

    private int quickParse(int rgb0) {
        int rgb = rgb0 & 0xF0F0F0;
        if (quickMap.containsKey(rgb)) {
            return quickMap.get(rgb);
        } else {
            int result = parseRGB(rgb);
            quickMap.put(rgb, result);
            return result;
        }
    }

    public int parseRGB(int rgb) {

        Vector3f color = new Vector3f(new Color3f(new Color(rgb)));

        int result = 0;
        float minangle = 0.3f; // set precision, angle in rads

        if (color.length() < 0.2f) { // avoid dark places
            result = 0;
            return result;
//            bi.setRGB(x, y, result);
//            continue;
        }

        for (Vector3f sample : colorSamples.keySet()) {
            float nma;
            if ((nma = sample.angle(color)) < minangle) {
                minangle = nma;
                result = colorSamples.get(sample);

//                bi.setRGB(x, y, result);
//                continue;
            }


        }

        if (result > 0) {
            return result;
        }

        // result == 0
        if (whitevector.angle(color) < 0.1) {
            result = 0xFFFFFFFF;
//            bi.setRGB(x, y, result);
//            continue;
        }

        return result;
    }
    private int iw;
    private int ih;
    private BufferedImage image;

    /**
     * 
     * there is a treshold set to avoid picking mess, which is useless
     * 
     * @param image where pixels of one curColor which neighbours
     * are considered as one object
     * 
     * @return array of found objects which are large enought
     */
    public Collection<ImageObject> findObjects(BufferedImage image) {

        this.image = image;

        iw = image.getWidth();
        ih = image.getHeight();

        List<ImageObject> objs = new ArrayList<ImageObject>();

        for (int y = 0; y < ih; y += 4) {
            for (int x = 0; x < iw; x += 4) {
                int color;
                if (((color = image.getRGB(x, y)) & 1) != 0) {

                    Set<Point> larea = getCompactAreaDFS(x, y);
                    if (larea.size() > 150) {
                        System.out.println("Araa size: " + larea.size());
                        ImageObject obj = new ImageObject(larea);
                        obj.setColor(color);
                        objs.add(obj);
                    }
                }
            }
        }
        return objs;

    }

//    /**
//     *
//     * @param x
//     * @param y
//     * @param image
//     * @return
//     * @deprecated
//     */
//    private Set<Point> getCompactAreaDeep(int x, int y, BufferedImage image) {
//
//        area = new HashSet<Point>();
////        area.clear();
//
//        int color = image.getRGB(x, y) & 0xFFFFFF;
//
//        if (color == 0xFF0000) {
//            exploreR(x, y, image, color, area);
//        }
//        return area;
//    }

//    /**
//     *
//     * @param x
//     * @param y
//     * @param image
//     * @param color
//     * @param area
//     * @deprecated
//     */
//    private void exploreR(int x, int y, BufferedImage image, int color, Set<Point> area) {
////        curColor = curColor & 0xFFFFFF;
//
//        // out of bounds
//        if ((x < 0) || (iw <= x) || (y < 0) || (ih <= y)) {
//            return;
//        }
//
//        Point p = new Point(x, y);
//
//        // if is valid count and extend
//        if (((image.getRGB(x, y) & 0xFFFFFF) == color) && (!area.contains(p))) {
//
//            image.setRGB(x, y, 0xFFFF8888); // prevent next visiting
//
//            area.add(p);
//
//            exploreR(x + 1, y, image, color, area);
//            exploreR(x, y + 1, image, color, area);
//            exploreR(x - 1, y, image, color, area);
//            exploreR(x, y - 1, image, color, area);
//        }
//    }
    private Set<Point> getCompactAreaDFS(int sx, int sy) {

        final int curColor = image.getRGB(sx, sy) & MASK;
//        System.out.println("Color " + Integer.toHexString(curColor));

        // exploring only area pixels
        List<Point> toExplore = new LinkedList<Point>();

        // area contains all visited or toExplore pixels
        Set<Point> area = new HashSet<Point>();

        area.add(new Point(sx, sy));
        toExplore.add(new Point(sx, sy));

        while (!toExplore.isEmpty()) {

            Point curr = toExplore.get(0);
            toExplore.remove(0);

//                tryExplore(curr.x + 2, curr.y);
//                tryExplore(curr.x, curr.y + 2);
//                tryExplore(curr.x - 2, curr.y);
//                tryExplore(curr.x, curr.y - 2);

            for (Point p : new Point[]{
                        new Point(curr.x + JUMP, curr.y),
                        new Point(curr.x, curr.y + JUMP),
                        new Point(curr.x - JUMP, curr.y),
                        new Point(curr.x, curr.y - JUMP)}) {
//                    System.out.println("p " + p);
                // out of bounds
                if ((p.x < 0) || (iw <= p.x) || (p.y < 0) || (ih <= p.y)) {
                    continue;
                }

// if is valid count and extend
                if (((image.getRGB(p.x, p.y) & MASK) == curColor) && (!area.contains(p))) {
                    image.setRGB(p.x, p.y, image.getRGB(p.x, p.y) & 0xFFFFFFFE); // prevent next visiting
                    area.add(p);
                    toExplore.add(p);
                }

            }
        }
        return area;
    }

//    private void tryExplore(int x, int y) {
//
//        Point p = new Point(x, y);
//
//        // out of bounds
//        if ((x < 0) || (iw <= x) || (y < 0) || (ih <= y)) {
//            return;
//        }
//
//        // if is valid count and extend
//        if (((image.getRGB(x, y) & 0xFFFFFF) == curColor) && (!area.contains(p))) {
//            image.setRGB(x, y, 0xFFFF8888); // prevent next visiting
//
//            area.add(p);
//            toExplore.add(p);
//        }
//
//
//    }
    /**
     *
     * @param image
     */
    public void analyze(BufferedImage image) {

        Graphics g = image.getGraphics();

        for (ImageObject obj : findObjects(image)) {
            g.setColor(Color.RED);
            g.drawRect(obj.getMinX(), obj.getMinY(), obj.getWidth(), obj.getHeight());

            g.setColor(Color.GREEN);
            obj.drawBorder(g);

//            g.setColor(Color.GREEN);
//            for (Point p1 : obj.getCorners()) {
//                for (Point p2 : obj.getCorners()) {
//                    g.drawLine(p1.x, p1.y, p2.x, p2.y);
//                }
//            }


        }

    }

    public void findWhiteVericalLine(BufferedImage image) {

        iw = image.getWidth();
        ih = image.getHeight();

        Graphics g = image.getGraphics();

        int maxcount = 30; // set lowest acceptable
        int bestx = -1;

        for (int x = 0; x <
                iw; x++) {
            int count = 0;
            for (int y = 0; y <
                    ih; y++) {
                if (image.getRGB(x, y) == WHITE) {
                    count++;
                }

            }
            if (count > maxcount) {
                maxcount = count;
                bestx =
                        x;
            }

        }

        g.setColor(Color.RED);
        g.drawLine(bestx, 0, bestx, ih);

    }

    public BufferedImage findBalls(BufferedImage image) {
        Graphics g = image.getGraphics();
        g.setColor(Color.GREEN);
        for (ImageObject imageObject : findObjects(image)) {
            System.out.println("Object " + imageObject.getEccentricity() + " x " + imageObject.getMinX());
            if (imageObject.getEccentricity() < 0.23) {
                g.drawOval(imageObject.getMinX(), imageObject.getMinY(),
                        imageObject.getWidth(), imageObject.getHeight());
            } else {
                g.drawRect(imageObject.getMinX(), imageObject.getMinY(),
                        imageObject.getWidth(), imageObject.getHeight());
            }
        }
        return image;
    }
}
