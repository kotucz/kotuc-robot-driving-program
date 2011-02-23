package vision.pathfinding.knowledge;

import java.awt.image.BufferedImage;

/**
 *
 * @author Kotuc
 */
public class DoubleMatrix {

    final int width;
    final int height;
    final double[] values;

    public DoubleMatrix(int width, int height) {
        this.width = width;
        this.height = height;
        this.values = new double[width * height];
    }

    public DoubleMatrix(int width, int height, double init) {
        this(width, height);
        for (int i = 0; i < values.length; i++) {
            values[i] = init;
        }
    }

    public int indexi(int x, int y) {
        return x + y * width;
    }

    public double get(int x, int y) {
        return values[indexi(x, y)];
    }

    public void set(int x, int y, double value) {
        values[indexi(x, y)] = value;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public BufferedImage getImage() {
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

//        Graphics2D g = (Graphics2D) result.getGraphics();

//        double min = Double.POSITIVE_INFINITY;
//        double max = Double.NEGATIVE_INFINITY;
//
//        for (double d : values) {
//            if (d != Double.NaN) {
//                max = Math.max(d, max);
//                min = Math.min(d, min);
//            }
//        }

        double min = 0;
        double max = 1;

//        g.setStroke(new BasicStroke(3));

//        this.image = image;

        int[] rgbArray = new int[values.length];

        for (int i = 0; i < rgbArray.length; i++) {

            if (values[i] == Double.NaN) {
                rgbArray[i] = 0xFF888888;
            } else {
////                int red = 0xFF&((int)(values[i]*255));
////                rgbArray[i] = 0xFF000000 | (red << 16);
////                rgbArray[i] = (values[i]>0.5)?0xFFFF0000:0xFF000000;
//            }

                // stretched
                rgbArray[i] = 0xFF000000 | ((int) Math.min(255.0 * (values[i] - min) / (max - min), 255) << 16);
            }
        }

        result.setRGB(0, 0, width, height, rgbArray, 0, width);

        return result;

    }
}
