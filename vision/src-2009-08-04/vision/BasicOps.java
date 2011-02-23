/*
 * BasicOps.java
 *
 */
package vision;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

/**
 * noninstantible tool class
 * @author Kotuc
 */
public final class BasicOps {

    private BasicOps() {
    }

    public static BufferedImage getResizedImage(BufferedImage src, int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(src, 0, 0, width, height, null);
        return img;
    }

    /**
     *  creates a copy of image
     * @param sourceImage
     * @return 
     */
    public static BufferedImage copy(BufferedImage sourceImage) {
        BufferedImage image = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        image.getGraphics().drawImage(sourceImage, 0, 0, null);
        return image;
    }

    /**
     *  creates a copy of image
     * @param sourceImage
     * @param treshold
     * @return
     */
    public static BufferedImage treshold(BufferedImage sourceImage, final int treshold) {
        return new ColorTransform() {

            @Override
            public int getColor(int... colors) {
                int intensity = (colors[0] & 0xFF) + ((colors[0] >> 8) & 0xFF) + ((colors[0] >> 16) & 0xFF);
                return (3 * treshold < intensity) ? 0xFFFFFFFF : 0xFF000000;
            }
        }.apply(sourceImage);

    }

    /**
     * flips image left-right
     * @param sourceImage
     * @return
     */
    public static BufferedImage horizontalFlip(BufferedImage sourceImage) {
//        BufferedImage image = new BufferedImage(320, 240, sourceImage.getType());
//        image.getGraphics().drawImage(sourceImage, 
//                320, 0, 0, 240, // dest coords
//                0, 0, sourceImage.getWidth(), sourceImage.getHeight(), // source coords
//                null);
//        return image;
        BufferedImage image = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), sourceImage.getType());
        image.getGraphics().drawImage(sourceImage,
                sourceImage.getWidth(), 0, 0, sourceImage.getHeight(), // dest coords
                0, 0, sourceImage.getWidth(), sourceImage.getHeight(), // source coords
                null);
        return image;
    }

    public static BufferedImage convolve(BufferedImage image, int op) {
        BufferedImageOp biop;

        int dim[][] = {{3, 3}, {3, 3}, {3, 3}, {5, 5}};
        float data[][] = {{0.1f, 0.1f, 0.1f, // 3x3 blur
                0.1f, 0.2f, 0.1f,
                0.1f, 0.1f, 0.1f},
            {-1.0f, -1.0f, -1.0f, // 3x3 sharpen
                -1.0f, 9.0f, -1.0f,
                -1.0f, -1.0f, -1.0f},
            {0.f, -1.f, 0.f, // 3x3 edge
                -1.f, 5.f, -1.f,
                0.f, -1.f, 0.f},
            {-1.0f, -1.0f, -1.0f, -1.0f, -1.0f, // 5x5 edge
                -1.0f, -1.0f, -1.0f, -1.0f, -1.0f,
                -1.0f, -1.0f, 24.0f, -1.0f, -1.0f,
                -1.0f, -1.0f, -1.0f, -1.0f, -1.0f,
                -1.0f, -1.0f, -1.0f, -1.0f, -1.0f}};

        biop = new ConvolveOp(new Kernel(dim[op][0], dim[op][1], data[op]));

        int iw = image.getWidth();
        int ih = image.getHeight();

        BufferedImage bi2 = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_RGB);

        return biop.filter(image, bi2);

    }

     static BufferedImage enlightGrass(BufferedImage image) {
//        BufferedImageOp biop;

        //        thresholdOp(low, high);
//        int i = 1;
//        biop = new RescaleOp(1.0f, 0, null);

        //        byte invert[] = new byte[256];
//        byte ordered[] = new byte[256];
//        byte contrast[] = new byte[256];
//        byte zero[] = new byte[256];
//        for (int j = 0; j < 256 ; j++) {
//            invert[j] = (byte) (256-j);
//            ordered[j] = (byte) j;
//            contrast[j] = (byte) ((j<150)?0:255);
//        }

        //        biop = new LookupOp(new ByteLookupTable(0,invert), null);

        //        byte[][] yellowInvert = new byte[][] { invert, invert, ordered };
//        biop = new LookupOp(new ByteLookupTable(0,yellowInvert), null);

        //        byte[][] showGrass = new byte[][] { contrast, zero, contrast };
//        biop = new LookupOp(new ByteLookupTable(0,showGrass), null);


        // best try

        //        byte red[] = new byte[256];
//        byte green[] = new byte[256];
//        byte blue[] = new byte[256];
//
//        for (int j = 0; j < 256 ; j++) {
//            red[j] = (byte) ((j<200)?0:255);
//            green[j] = (byte) 0;
//            blue[j] = (byte) ((j<150)?0:255);
//        }
//
//        byte[][] showWay = new byte[][] {red, green, blue};
//        biop = new LookupOp(new ByteLookupTable(0, showWay), null);
//
//
//        int iw = image.getWidth(this);
//        int ih = image.getHeight(this);
//
//        BufferedImage bi1 = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_RGB);
//        bi1.createGraphics().drawImage(image,0,0,null);
//
//        iw = image.getWidth(this);
//        ih = image.getHeight(this);
//        BufferedImage bi2 = new BufferedImage(iw,ih,BufferedImage.TYPE_INT_RGB);
//        biop.filter(bi1, bi2);
//
//        image = bi2;
//




        //      my try

        int iw = image.getWidth();
        int ih = image.getHeight();


        for (int x = 0; x < iw; x++) {
            for (int y = 0; y < ih; y++) {
                int i = image.getRGB(x, y);

                int r = (i & 0xFF0000) >> 16;
                int g = (i & 0xFF00) >> 8;
                int b = i & 0xFF;



                i = (int) Math.min(255, (Math.sqrt(Math.pow(r - g, 2) + Math.pow(r - b, 2) + Math.pow(b - g, 2))));
                i = (255 - i);
                // higher i -> more red -> more way
                i = (0xFF - i) + i * 0x10000;
                image.setRGB(x, y, i);
            }
        }

        return image;

    }

    static BufferedImage putDownOld(BufferedImage image) {
        int iw = image.getWidth();
        int ih = image.getHeight();

        //        BufferedImage bi = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_RGB);
//        bi.createGraphics();

        //        Graphics2D g2 = (Graphics2D)bi.getGraphics();

        //        g2.shear(0.5, 0.0);

        //        AffineTransform at = new AffineTransform();

        //        at.setTransform(0.5, 0 ,
//                        0, 1.0,
//                        iw/4, 0);
//
//        at.transform(new double[] {0, ih,  iw, ih}, 0,
//                new double[] {iw/2, ih, iw/2, ih}, 0, 2);

        //        at.setToRotation(0.5, iw/2, ih/2);
//
//        System.out.println(at);
//
//        g2.setTransform(at);
//
//        g2.drawImage(image, 0, 0, iw, ih, Color.BLACK, null);
//

        BufferedImage floor = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_RGB);
        floor.createGraphics();


        double sang = Math.toRadians(20), // 30 deg down from horizont
                //               cang = Math.toRadians(30), // angle the cam is facing from horizon
                vang = Math.toRadians(50), // 60 deg view angle

                heig = 1.0, // cam height from ground
                mperpx = 0.01, // 100px == 1m
                focus = 0.0;


        for (int y = 0; y < ih; y++) {
            double alfa = Math.atan((double) heig / (double) (ih - y) / mperpx);

            double py = (double) ih * (Math.sin(alfa - sang - vang / 2.0) / Math.sin(vang / 2.0) + 0.5);

            double k = heig / Math.sin(alfa);

            for (int x = 0; x < iw; x++) {
                try {
                    //                  floor.setRGB(iw/2 + (x-iw/2)*((ih+150-y))/iw, y, image.getRGB(x, y));
                    floor.setRGB(x, y, image.getRGB((int) ((x - iw * 0.5) * k + iw * 0.5),
                            (int) py));
                } catch (ArrayIndexOutOfBoundsException ex) {
                }
            }
        }

        Graphics2D g2 = (Graphics2D) floor.getGraphics();
        g2.setColor(Color.CYAN);

        g2.drawLine(iw / 2 - 5, ih - (int) (0.5 / mperpx), iw / 2 + 5, ih - (int) (0.5 / mperpx));

        g2.drawLine(iw / 2 - 5, ih - (int) (1.0 / mperpx), iw / 2 + 5, ih - (int) (1.0 / mperpx));

        g2.drawLine(iw / 2 - 5, ih - (int) (1.5 / mperpx), iw / 2 + 5, ih - (int) (1.5 / mperpx));

        return floor;

    }

    //        LinkedList<Point> oldpoints = new LinkedList();
//
//    public BufferedImage keyPoint(BufferedImage image) {
//
//        BufferedImageOp biop;
//
//        int iw = image.getWidth();
//        int ih = image.getHeight();
//
//       LinkedList<Point> points = new LinkedList();
//
//       int o = 3;
//
//        int dim[][] = {{3,3}, {3,3}, {3,3}, {4,4}};
//        float data[][] =  {
//                           {0.1f, 0.1f, 0.1f,              // 3x3 blur
//                            0.1f, 0.2f, 0.1f,
//                            0.1f, 0.1f, 0.1f},
//                           {-1.0f, -1.0f, -1.0f,           // 3x3 sharpen
//                            -1.0f, 9.0f, -1.0f,
//                            -1.0f, -1.0f, -1.0f},
//                           { 0.f, -1.f,  0.f,                  // 3x3 edge
//                            -1.f,  5.f, -1.f,
//                             0.f, -1.f,  0.f},
//                           {+1.0f, +1.0f, +1.0f, +1.0f,  // 5x5 edge
//                            -1.0f, -1.0f, -1.0f, +1.0f,
//                            -1.0f, -1.0f, -1.0f, +1.0f,
//                            -1.0f, -1.0f, -1.0f, +1.0f}};
//
//        biop = new ConvolveOp(new Kernel(dim[o][0], dim[o][1], data[o]));
//
//        BufferedImage bi2 = new BufferedImage(iw,ih,BufferedImage.TYPE_INT_RGB);
//
//        bi2 = biop.filter(image, bi2);
//
////        if (o>2) return bi2;
//
//        Graphics2D g22 = (Graphics2D)bi2.getGraphics();
//        g22.setColor(Color.BLACK);
//
//        for (int x = 0; x<iw; x++) {
//            for (int y = 0; y<ih; y++) {
//                int i = getGray(bi2, x, y);
//
//                if (i>200) {
//// found
//                   g22.fillOval(x-20, y-20, 40, 40);
//                   points.add(new Point(x, y));
//                }
//
//            }
//        }
//
//        Graphics2D g2 = (Graphics2D)image.getGraphics();
//        g2.setColor(Color.RED);
//
//        if (oldpoints.size()<4) {
//            oldpoints.addAll(points);
//        } else {
//            LinkedList<Point> newpoints = new LinkedList();
//            for (Point point:points) {
//                for (Point oldpoint:oldpoints) {
//                    if (point.distance(oldpoint)<20) newpoints.add(point);
//                }
//            }
//            oldpoints = newpoints;
//        }
//
//        for (Point point:oldpoints) {
//            g2.fillOval(point.x-4, point.y-4, 8, 8);
//        }
//
//        g2.setColor(Color.BLUE);
//        for (Point point:points) {
//            g2.fillOval(point.x-4, point.y-4, 8, 8);
//
//        }
//
//        return image;
//
//    }
    /**
     * finds the most and min intensity pixel
     * @param image
     * @return 
     */
    static BufferedImage minMax(BufferedImage image) {


        //      my try

        int iw = image.getWidth();
        int ih = image.getHeight();

        Graphics2D g2 = (Graphics2D) image.getGraphics();
        g2.setColor(Color.RED);

        int mxi = 0,
                mxx = 0,
                mxy = 0;
        int mni = 1000,
                mnx = 0,
                mny = 0;

        for (int x = 0; x < iw; x++) {
            for (int y = 0; y < ih; y++) {
                int i = image.getRGB(x, y);

                int r = (i & 0xFF0000) >> 16;
                int g = (i & 0xFF00) >> 8;
                int b = i & 0xFF;

                if (r + g + b > mxi) {
                    mxx = x;
                    mxy = y;
                    mxi = r + g + b;
                }

                if (r + g + b < mni) {
                    mnx = x;
                    mny = y;
                    mni = r + g + b;
                }

            }
        }

        g2.fillOval(mxx - 4, mxy - 4, 8, 8);
        g2.setColor(Color.BLUE);
        g2.fillOval(mnx - 4, mny - 4, 8, 8);

        return image;

    }
}
