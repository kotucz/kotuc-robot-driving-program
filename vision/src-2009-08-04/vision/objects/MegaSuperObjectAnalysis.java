package vision.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * Objectless faster SuperObjectAnalysis version
 *
 * @author Tomas
 */
public class MegaSuperObjectAnalysis {
    protected static final int MINIMUM_OBJECT_SIZE = 150;

    private int[] ids;
    private int[] colors;
    private int bands;
    private int iw;
    private int ih;
    int[] size;

    /**
     * After ids[index] has the unique number of its coherent components.
     * Components are equivalent if their has same ids and areas.
     * @param image
     */
    public void prepair(BufferedImage image) {

        iw = image.getWidth();
        ih = image.getHeight();
        bands = image.getSampleModel().getNumBands();

        // [x, y]
//        final Ent[][] ents = new Ent[iw][ih];

        this.colors = image.getRaster().getPixels(0, 0, iw, ih, colors);

        this.ids = new int[iw * ih];

        try {
            for (int i = 0; i < ids.length; i++) {

                ids[i] = i;

                // up neighbour
                if (iw < i && same(i, i - iw)) {
                    merge(i, i - iw);
                }

                // left neighbour
                if (0 < (i % iw) && same(i, i - 1)) {
                    merge(i, i - 1);
                }

            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }

        size = new int[iw * ih];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = id(i);
            size[ids[i]]++;
        }
    }
    List<ImageObject> objects;
    ImageObject[] idObjects;

    public Collection<ImageObject> findObjects(BufferedImage image) {
//    public Collection<ImageObject> findObjects(BufferedImage image) {
        prepair(image);

        objects = new LinkedList<ImageObject>();
        idObjects = new ImageObject[iw * ih];
        for (int i = 0; i < ids.length; i++) {
            if (size[ids[i]] > MINIMUM_OBJECT_SIZE) {
                ImageObject obj = idObjects[ids[i]];
                if (obj == null) {
                    objects.add(obj = idObjects[ids[i]] = new ImageObject());
                }
                obj.area.add(new Point(i % iw, i / iw));
            }
        }

        for (ImageObject obj : objects) {
            obj.parse(obj.area);
        }

        return objects;
    }

    public BufferedImage showIncidence(BufferedImage image) {
        SparseIncidenceMatrix incidence = new SparseIncidenceMatrix();

        for (int i = iw; i < ids.length; i++) {
            if (i % iw != 0) {
                incidence.addIncidence(ids[i], ids[i - 1]);
            }
            incidence.addIncidence(ids[i], ids[i - iw]);
        }

        Graphics g = image.getGraphics();
        g.setColor(Color.GREEN);

        for (Point i : incidence.incidences.keySet()) {
            if (idObjects[i.x] == null || idObjects[i.y] == null) {
                continue;
            }
            Point c1 = idObjects[i.x].getMassCenter();
            Point c2 = idObjects[i.y].getMassCenter();
            g.drawLine(c1.x, c1.y, c2.x, c2.y);
        }

        return image;
    }

    public BufferedImage colorize(BufferedImage image) {

        Collection<ImageObject> findObjects = findObjects(image);

        for (int i = 0; i < ids.length; i++) {
            for (int k = 0; k < bands; k++) {
                colors[bands * i + k] = colors[bands * id(i) + k];
            }
        }

        image.getRaster().setPixels(0, 0, iw, ih, colors);
        Graphics g = image.getGraphics();

        g.setColor(Color.YELLOW);
        for (ImageObject obj : findObjects) {

            obj.drawBorder(g);

            if (obj.getEccentricity() < 1) {
                g.setColor(Color.RED);
                g.drawOval(obj.getMinX(), obj.getMinY(), obj.getWidth(), obj.getHeight());
                g.setColor(Color.YELLOW);
            }


//            imageObject.drawOutline(g);
        }

//        showIncidence(image);

        return image;
    }

    /**
     * Returns id - such parent that has no parent
     * this if this.parent == null
     * id.parent is always null - definition of id.
     * @return
     */
    public int id(int index) {
//            System.out.println(this);
        if (ids[index] == index) {
            return index;
        } else {
            // optimalization
//            if (ids[ids[index]] == NO_PARENT) {
//                return ids[index];
//            } else {
            // optimalization
            return ids[index] = id(ids[index]);
//            }

        }
    }

    /**
     * Joins this and ent sets together.
     * @param ent
     */
    public void merge(int index1, int index2) {
//            System.out.println("JOIN " + this + "(" + this.id() + ") + " +
//                    ent + "(" + ent.id() + ")");
//            System.out.println("JOIN " + this + " + " + ent);
        if (id(index1) == id(index2)) {
            // cannot merge
            // System.err.println("Joining Equivalent sets!!!");
            } else {
//            if (this.id().size < ent.id().size) {
//                ent.add(this);
            ids[id(index2)] = id(index1);
//                    System.out.println("VICE VERSA!");
//            } else {
//                this.add(ent);
//                    System.out.println("OK");
//        }
        }

    }

    /**
     * Returns true if this and ent could be in same set.
     * @param ent
     * @return
     */
    private boolean same(int index1, int index2) {
//        return false;
        int dist = 0;
        for (int k = 0; k < bands; k++) {
            dist += Math.abs(colors[index1 * bands + k] - colors[index2 * bands + k]);
        }
        return dist < 16;
//        return colors[index1] == colors[index2];
//            return this.color == ent.color;
//        return Colors.dist(colors[index1], colors[index2]) < 0.016; // 640x480
//            return Colors.dist(this.color, ent.color) < 0.014; // 640x480
//            return Colors.hsbDist(this.color, ent.color) < 0.029;
    }
}
