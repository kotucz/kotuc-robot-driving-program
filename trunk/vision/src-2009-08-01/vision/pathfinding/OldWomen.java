
package vision.pathfinding;


import java.awt.image.BufferedImage;
import vision.BasicOps;

/**
 * nonintantible
 * @author PC
 */
public final class OldWomen {

//    private static final int numberx = 30;
//    private static final int numbery = 30;

//    static BufferedImage MASK_10_11 = new PathMask3D(numberx, numbery, 1.0, 1.1).getMask();
//    static BufferedImage MASK_10_10 = new PathMask3D(numberx, numbery, 1.0, 1.0).getMask();
//    static BufferedImage MASK_11_10 = new PathMask(numberx, numbery, 1.1, 1.0).getMask();
//
//    static BufferedImage MASK_06_05 = new PathMask3D(numberx, numbery, 0.6, 0.5).getMask();
//    static BufferedImage MASK_05_06 = new PathMask3D(numberx, numbery, 0.5, 0.6).getMask();
//    static BufferedImage MASK_105_10 = new PathMask(numberx, numbery, 1.05, 1.0).getMask();
//
//
//    private static List<BufferedImage> masks = new ArrayList<BufferedImage>();
//    static {
//        masks.add(MASK_10_11);
//        masks.add(MASK_10_10);
//        masks.add(MASK_11_10);
//
////        masks.add(MASK_06_05);
////        masks.add(MASK_05_06);
//        masks.add(MASK_105_10);
//    }
    /** Creates a new instance of OldWoman */
    private OldWomen() {
    }

    public static BufferedImage groundWoman4(BufferedImage image) {
//        int iw = image.getWidth();
//        int ih = image.getHeight();

//        int maskid = 1;

        BufferedImage best = null;
        double bestdist = 0;

        float[] steers = new float[]{-0.1f, -0.08f, -0.06f, -0.04f, -0.02f, 0.002f, 0.02f, 0.04f, 0.06f, 0.08f, 0.1f};

        image = groundWoman1(image);

        for (float steer : steers) //        BufferedImage mask = masks.get(maskid);
        {

            BufferedImage bi = BasicOps.copy(image);

            double dist = PathFinding.paintTrails4(bi, steer);

//            g2.drawString(""+dist+" m",numberx/2, numbery/2);
            System.out.println(" DIST " + dist);

            if (dist > bestdist) {
                best = bi;
                bestdist = dist;
            }

        }

        return best;

    }

//    public static BufferedImage groundWoman3(BufferedImage image) {
//        int iw = image.getWidth();
//        int ih = image.getHeight();
//
////        int maskid = 1;
//
//        BufferedImage best = null;
//        double bestprob = 0;
//
//        for (BufferedImage mask : masks)
////        BufferedImage mask = masks.get(maskid);
//        {
//
//            BufferedImage bi = new BufferedImage(numberx, numbery, BufferedImage.TYPE_INT_RGB);
//            bi.createGraphics();
//            Graphics2D g2 = (Graphics2D)bi.getGraphics();
//
//            long allpxs = 0, allred = 0, allgreen = 0, allblue = 0 ;
//
//            double prob = 0;
//            double prob100 = 0;
//
//            for (int y = 0; y < numbery; y++) {
//                for (int x = 0; x < numberx; x++) {
//
//                    int weight = (mask.getRGB(x, y)&0xFF);
//
//                    if (weight>10) {
//
//                        int color = 0;
//                        long pxs = 0, pixel = 0 , red = 0, green = 0, blue = 0 ;
//
//
//                        for (int j = y*ih/numbery; j < (y+1)*ih/numbery-1; j++) {
//                            for (int i = x*iw/numberx; i < (x+1)*iw/numberx-1; i++) {
//
//                                pxs++;
//                                pixel = image.getRGB(i, j);
//                                red   += ((pixel >> 16) & 0xff);
//                                green += ((pixel >>  8) & 0xff);
//                                blue  += ((pixel      ) & 0xff);
//
//                            }
//                        }
//
//                        color = (((int)((double)red/pxs)) << 16) + (((int)((double)green/pxs)) << 8) + ((int)((double)blue/pxs));
//
//                        bi.setRGB(x, y, color);
//
//                        allred += weight*red;
//                        allblue += weight*blue;
//                        allgreen += weight*green;
//
//                        allpxs += weight*pxs;
//
//                        prob += weight*new HeatMap2_2().heat(color);
//                        prob100 += weight;
//
//                    }
//                }
//            }
//
//            prob /= prob100;
//
//            int color = (((int)((double)allred/allpxs)) << 16) + (((int)((double)allgreen/allpxs)) << 8) + ((int)((double)allblue/allpxs));
//
//            g2.setColor(Color.RED);
//
//            g2.drawString(""+prob+" %",numberx/2, numbery/2);
//            System.out.println(" PROB "+prob+" "+Integer.toHexString(color));
//
//            if (prob>bestprob) {
//                best = bi;
//                bestprob = prob;
//            }
//
//        }
//
//        return best;
//
//    }
//    public static BufferedImage groundWoman3_1(BufferedImage image) {
//        int iw = image.getWidth();
//        int ih = image.getHeight();
//
//        BufferedImage best = null;
//        double bestprob = 0;
//
//        for (BufferedImage mask : masks) {
//
//            BufferedImage bi = new BufferedImage(numberx, numbery, BufferedImage.TYPE_INT_RGB);
//            bi.createGraphics();
//            Graphics2D g2 = (Graphics2D)bi.getGraphics();
//
//            long allpxs = 0, allred = 0, allgreen = 0, allblue = 0 ;
//
//            for (int y = 0; y < numbery; y++) {
//                for (int x = 0; x < numberx; x++) {
//
//                    int weight = (mask.getRGB(x, y)&0xFF);
//
//                    if (weight>0) {
//
//                        int color = 0;
//                        long pxs = 0, pixel = 0 , red = 0, green = 0, blue = 0 ;
//
//
//                        for (int j = y*ih/numbery; j < (y+1)*ih/numbery-1; j++) {
//                            for (int i = x*iw/numberx; i < (x+1)*iw/numberx-1; i++) {
//
//                                pxs++;
//                                pixel = image.getRGB(i, j);
//                                red   += ((pixel >> 16) & 0xff);
//                                green += ((pixel >>  8) & 0xff);
//                                blue  += ((pixel      ) & 0xff);
//
//                            }
//                        }
//
//                        color = (((int)((double)red/pxs)) << 16) + (((int)((double)green/pxs)) << 8) + ((int)((double)blue/pxs));
//
//                        bi.setRGB(x, y, color);
//
//                        allred += weight*red;
//                        allblue += weight*blue;
//                        allgreen += weight*green;
//
//                        allpxs += weight*pxs;
//
//                    }
//                }
//
//
//            }
//
//            int color = (((int)((double)allred/allpxs)) << 16) + (((int)((double)allgreen/allpxs)) << 8) + ((int)((double)allblue/allpxs));
//            double prob = 100*(new HeatMap2_2().heat(color));
//            g2.setColor(Color.RED);
//
//            g2.drawString(""+prob+" %",numberx/2, numbery/2);
//            System.out.println(" PROB "+prob);
//
//            if (prob>bestprob) {
//                best = bi;
//                bestprob = prob;
//            }
//
//        }
//
//        return best;
//
//    }
//    public static BufferedImage groundWoman2(BufferedImage image) {
//        int iw = image.getWidth();
//        int ih = image.getHeight();
//
//
//
//        BufferedImage bi = new BufferedImage(numberx, numbery, BufferedImage.TYPE_INT_RGB);
//        bi.createGraphics();
//        Graphics2D g2 = (Graphics2D) bi.getGraphics();
//
//        BufferedImage mask = MASK_10_11;
//
//        long allpxs = 0, allred = 0, allgreen = 0, allblue = 0;
//
//        for (int y = 0; y < numbery; y++) {
//            for (int x = 0; x < numberx; x++) {
//
//                int weight = (mask.getRGB(x, y) & 0xFF);
//
//                if (weight > 0) {
//
//                    int color = 0;
//                    long pxs = 0, pixel = 0, red = 0, green = 0, blue = 0;
//
//
//                    for (int j = y * ih / numbery; j < (y + 1) * ih / numbery - 1; j++) {
//                        for (int i = x * iw / numberx; i < (x + 1) * iw / numberx - 1; i++) {
//
//                            pxs++;
//                            pixel = image.getRGB(i, j);
//                            red += ((pixel >> 16) & 0xff);
//                            green += ((pixel >> 8) & 0xff);
//                            blue += ((pixel) & 0xff);
//
//                        }
//                    }
//
//                    color = (((int) ((double) red / pxs)) << 16) + (((int) ((double) green / pxs)) << 8) + ((int) ((double) blue / pxs));
//
//                    bi.setRGB(x, y, color);
//
//                    allred += weight * red;
//                    allblue += weight * blue;
//                    allgreen += weight * green;
//
//                    allpxs += weight * pxs;
//
//                }
//            }
//
//
//        }
//
//        int color = (((int) ((double) allred / allpxs)) << 16) + (((int) ((double) allgreen / allpxs)) << 8) + ((int) ((double) allblue / allpxs));
//        double prob = 100 * (new HeatMap2_2().heat(color));
//        g2.setColor(Color.RED);
//
//        g2.drawString("" + prob + " %", numberx / 2, numbery / 2);
//        //System.out.println(" PROB "+prob);
//
//
//
//
//
//        return bi;
//
//    }
    public static BufferedImage groundWoman1(BufferedImage image) {
        int iw = image.getWidth();
        int ih = image.getHeight();

        int pxinsqr = 8;

        final int numberx = iw / pxinsqr;
        final int numbery = ih / pxinsqr;

        BufferedImage bi = new BufferedImage(numberx, numbery, BufferedImage.TYPE_INT_RGB);
        bi.createGraphics();




        for (int y = 0; y < numbery; y++) {
            for (int x = 0; x < numberx; x++) {

                int color = 0, pxs = 0, pixel = 0, red = 0, green = 0, blue = 0;



                for (int j = y * ih / numbery; j < (y + 1) * ih / numbery - 1; j++) {
                    for (int i = x * iw / numberx; i < (x + 1) * iw / numberx - 1; i++) {

                        pxs++;
                        pixel = image.getRGB(i, j);
                        red += (pixel >> 16) & 0xff;
                        green += (pixel >> 8) & 0xff;
                        blue += (pixel) & 0xff;



                    }
                }

                color = (((int) ((double) red / pxs)) << 16) + (((int) ((double) green / pxs)) << 8) + ((int) ((double) blue / pxs));

                bi.setRGB(x, y, color);
            }
        }


        return bi;

    }
}
