package vision.objects;

import vision.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tomas
 */
public class SuperObjectAnalysis {

    public BufferedImage findObjects(BufferedImage image) {
//    public Collection<ImageObject> findObjects(BufferedImage image) {

        image = BasicOps.getResizedImage(image, 320, 240);

        List<ImageObject> objs = new ArrayList<ImageObject>();

        final int iw = image.getWidth();
        final int ih = image.getHeight();

        // [x, y]
        final Ent[][] ents = new Ent[iw][ih];

        try {
            for (int y = 0; y < ih; y++) {
                for (int x = 0; x < iw; x++) {
                    int color = image.getRGB(x, y);
                    Ent ent = ents[x][y] = new Ent(x, y, color);


                    if (0 < y && ent.same(ents[x][y - 1])) {
                        ent.join(ents[x][y - 1]);
                    }

                    if (0 < x && ent.same(ents[x - 1][y])) {
                        ent.join(ents[x - 1][y]);
                    }

                }

            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }


        for (int y = 0; y < ih; y++) {
            for (int x = 0; x < iw; x++) {
                if (ents[x][y].id().size > 30) {
//                if (ents[x][y].id().childs > 3000) {
//                    image.setRGB(x, y, (int) (ents[x][y].id().colorsum / ents[x][y].id().childs));
//                    image.setRGB(x, y, ents[x][y].color);
//                   image.setRGB(x, y, ents[x][y].id().color);
                    image.setRGB(x, y, Colors.getColor(
                            ents[x][y].id().redsum / ents[x][y].id().size,
                            ents[x][y].id().greensum / ents[x][y].id().size,
                            ents[x][y].id().bluesum / ents[x][y].id().size));
                } else {
                    image.setRGB(x, y, 0xFF000000);
                }

            }
        }

        return image;
    //return objs;

    }

    /**
     * Representation of element of set with an identifier.
     */
    class Ent {

        final int x,  y;
        final int color;
        Ent parent = null;
        int size = 1;
        long colorsum;
        int redsum;
        int greensum;
        int bluesum;

        public Ent(int x, int y, int color) {
            this.x = x;
            this.y = y;
            this.color = color;
            this.colorsum = color;
            this.redsum = Colors.getRed(color);
            this.greensum = Colors.getGreen(color);
            this.bluesum = Colors.getBlue(color);
        }

        /**
         * Returns id - such parent that has no parent
         * this if this.parent == null
         * id.parent is always null - definition of id.
         * @return
         */
        public Ent id() {
//            System.out.println(this);
            if (this.parent == null) {
                return this;
            } else {
                // optimalization
                if (this.parent.parent == null) {
                    return this.parent;
                } else {
                    // optimalization
                    return this.parent = this.parent.id();
                }
            }
        }

        /**
         * Makes this parent of ent.
         * @param ent
         */
        public void add(Ent ent) {
            final Ent newId = this.id();
            final Ent addId = ent.id();
//            System.out.println("ADD");

            newId.size += addId.size;

            newId.colorsum += addId.colorsum;

            newId.redsum += addId.redsum;
            newId.greensum += addId.greensum;
            newId.bluesum += addId.bluesum;

            addId.setParent(newId);
        }

        private void setParent(Ent parent) {
            this.parent = parent;
        }

        /**
         * Joins this and ent sets together.
         * @param ent
         */
        public void join(Ent ent) {
//            System.out.println("JOIN " + this + "(" + this.id() + ") + " +
//                    ent + "(" + ent.id() + ")");
//            System.out.println("JOIN " + this + " + " + ent);
            if (this.id().equals(ent.id())) {
                // System.err.println("Joining Equivalent sets!!!");
            } else {
                if (this.id().size < ent.id().size) {
                    ent.add(this);
//                    System.out.println("VICE VERSA!");
                } else {
                    this.add(ent);
//                    System.out.println("OK");
                }
            }
        }

        /**
         * Returns true if this and ent could be in same set.
         * @param ent
         * @return
         */
        private boolean same(Ent ent) {
//            return this.color == ent.color;
            return Colors.dist(this.color, ent.color) < 0.016; // 640x480
//            return Colors.dist(this.color, ent.color) < 0.014; // 640x480
//            return Colors.hsbDist(this.color, ent.color) < 0.029;
        }

        @Override
        public String toString() {
            return "Ent[" + x + ", " + y + "]" + Integer.toHexString(color) + "(" + size + ")";
        }
    }
}
