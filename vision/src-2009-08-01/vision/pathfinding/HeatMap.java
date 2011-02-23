package vision.pathfinding;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;
import vision.Histogram;
import vision.Spectrograph;

/**
 *
 * @author Tomas Kotula
 */
public class HeatMap {

    static HeatFunction FUNC_HM10 = new HeatMap3(Color.GRAY);

//    private BufferedImage src;
    private final float[][] heatMap;
//    private BufferedImage heatImg;
    private final int iw,  ih;
    private FloorProjection fp = new FloorProjection();

    /**
     * 
     */
    private HeatMap(BufferedImage src, HeatFunction func) {
//        this.src = src;
        this.iw = src.getWidth();
        this.ih = src.getHeight();

        this.heatMap = new float[iw][ih];


        for (int y = 0; y < ih; y++) {
            for (int x = 0; x < iw; x++) {

                int rgb = src.getRGB(x, y);

                heatMap[x][y] = func.heat(rgb);

            }
        }
    }

    /**
     * Returns HeatMap representation of the image processed by func
     * @param img
     * @param func
     * @return
     */
    public static HeatMap getHeatMap(BufferedImage img, HeatFunction func) {
        return new HeatMap(img, func);
    }

    BufferedImage createRoadBnWImage() {
        return createHeatImage(ROAD_WHITE_OTHER_BLACK);
    }

    /**
     * @return heat value on floor coordinates
     */
    float getHeatFloor(float floorx, float floory) {
        Point p = fp.getPoint(floorx, floory, iw, ih);

        return heatMap[p.x][p.y];

    }

    /**
     *
     * @param x
     * @param y
     * @return heat on image coorinates x, y
     */
    float getHeat(int x, int y) {
        return heatMap[x][y];
    }

    /**
     * 
     * creates new Heat Image of previously parsed source Image
     * 
     * @see parse, HeatVizualizer
     * 
     * @param vizualizer
     * @return BufferedImage colors according to heat values interpreted by visualizer
     * 
     */
    public BufferedImage createHeatImage(HeatVizualizer vizualizer) {

        BufferedImage heatImg = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_RGB);
        heatImg.createGraphics();

        for (int y = 0; y < ih; y++) {
            for (int x = 0; x < iw; x++) {

                float prob = heatMap[x][y];

                heatImg.setRGB(x, y, vizualizer.color(prob));

            }
        }
        return heatImg;
    }
    static final HeatVizualizer VIZUAL_1 = new HeatVizualizer() {

        public int color(double prob) {
            if (prob > 500) {
                return 0xFf0000FF;
            } else if (prob > 200) {
                return 0xFF1000F0;
            } else if (prob > 100) {
                return 0xFF3000C0;
            } else if (prob > 50) {
                return 0xFF600090;
            } else if (prob > 10) {
                return 0xFFA00040;
            } else {
                return 0xFF000000;
            }
        }
    };
    static final HeatVizualizer VIZUAL_2 = new HeatVizualizer() {

        public int color(double prob) {
            if (prob > 0.9) {
                return 0xFFFF0000;
            } else if (prob > 0.8) {
                return 0xFFFFFF00;
            } else if (prob > 0.7) {
                return 0xFF00FF00;
            } else if (prob > 0.6) {
                return 0xFF00FFFF;
            } else if (prob > 0.5) {
                return 0xFF0000FF;
            } else if (prob > 0.4) {
                return 0xFFFF00FF;
            } else {
                return 0xFF000000;
            }
        }
    };

    static class HeaFunc2_2 implements HeatFunction {

        /**
         * road color
         */
        private final Color colorr;
        /**
         * road color vector
         */
        private final Vector3f colorrvector;

        public HeaFunc2_2() {

            colorr = Color.DARK_GRAY;
            colorrvector = new Vector3f(new Color3f(colorr));
            colorrvector.normalize();

//        parse(heatImg, this);

        }

        public HeaFunc2_2(Color color) {

            colorr = color;
            colorrvector = new Vector3f(new Color3f(colorr));
            colorrvector.normalize();

//        parse(heatImg, this);

        }

        public float heat(int rgb) {
            Vector3f pix = new Vector3f(new Color3f((new Color(rgb))));
//        System.out.println("pix "+pix+" dot "+pix.dot(colorrvector)+" length "+pix.length());
            return (pix.dot(colorrvector) / pix.length());
        }
    }

    static HeatMap getHeatMap2_2(BufferedImage img) {
        return new HeatMap(img, new HeaFunc2_2());
    }

    public static HeatMap getHeatMapCM(BufferedImage img) {
        return new HeatMap(img, new HeatMapCM());
    }

    static class HeatMapCM implements HeatFunction {

        private Histogram histogram = new Histogram();
//    static Histogram histogram = new Histogram(new File("C:/Users/Kotuc/Desktop/snap000740.png"));

        HeatMapCM() {
            File dir = new File("./roadimgs/");
            if (dir.isDirectory()) {
                File[] files = dir.listFiles(new FilenameFilter() {

                    public boolean accept(File dir, String name) {
                        return name.contains("png");
                    }
                });
                if (files.length > 0) {
                    for (File f : files) {
                        histogram.load(f);
                    }
                } else {
                    System.err.println("HeatMapCM histogram 0 files in " + dir + "!");
                }

            }

        }

        public float heat(int color) {
            return histogram.getValue(color);
        }
    }
    public static final HeatVizualizer ROAD_WHITE_OTHER_BLACK = new HeatVizualizer() {

        public int color(double val) {
            return ((val > 0.5) ? 0xFFFFFFFF : 0xFF000000);
        }
    };

    boolean isRoad(int x, int y) {
        return getHeat(x, y) > 0.5;
    }

    /**
     * somehow not finished
     */
    private static class HeatMap3 implements HeatFunction {

        private Vector3f colorr;

        public HeatMap3(Color color) {
            colorr = new Vector3f(new Color3f(Spectrograph.toSpectralColor(color)));
        }

        public float heat(int color) {
            Vector3f median = new Vector3f();
            int count = 0;

            Vector3f pix = null;
            int rgb = 0;

            rgb = color;
            pix = new Vector3f(new Color3f((new Color(rgb))));

            //                Vector3f fin;

            //                float heat = Math.max(1-pix.distance(colorr), 0);
            ///  heat>0.82
            if ((pix.dot(colorr) / pix.length()) > 0.99) {
                //                    heatMap.setRGB(x, y, rgb);
                median.add(pix);
                count++;
            //                    heatMap.setRGB(x, y, 0xFF0000);
            } else {
                //                    heatMap.setRGB(x, y, 0x000000);
            }

            median.scale(1 / median.length());

            pix = new Vector3f(new Color3f((new Color(color))));

            //                Vector3f fin;

            //                float heat = Math.max(1-pix.distance(colorr), 0);
            ///  heat>0.82
            if ((pix.dot(median) / pix.length()) > 0.99) {
                //                    heatMap.setRGB(x, y, rgb);
//                    median.add(pix);
//                    count++;
//                    heatMap.setRGB(x, y, 0xFF0000);
            }
            return 0;
        }
    }
}


