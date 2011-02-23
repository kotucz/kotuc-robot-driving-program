package vision.ar;

import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;

/**
 *
 * @author Kotuc
 */
public class Pattern {

    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;
    BufferedImage image;
    private String name;
    private double angle;
    // average amount of black/white
    private int gray = -1;

    public Pattern(int pw, int ph) {
        image = new BufferedImage(pw, ph, BufferedImage.TYPE_BYTE_GRAY);
    }

    public void set(int x, int y, boolean binary) {
        image.setRGB(x, y, (binary) ? BLACK : WHITE);
    }

    public BufferedImage getImage() {
        return image;
    }

    int gray() {
        if (gray < 0) {
            gray = 0;
            int[] colors = null;
            colors = image.getRaster().getPixels(0, 0, image.getWidth(), image.getHeight(), colors);
            for (int i : colors) {
                gray += i & 255;
            }
            gray /= colors.length;
        }
        return gray;
    }

    public Pattern(BufferedImage image, String name, double angle) {
        this.image = image;
        this.name = name;
        this.angle = angle;
//        corners.add(new Point(20, 20));
//        corners.add(new Point(20, 70));
//        corners.add(new Point(70, 20));
//        corners.add(new Point(70, 70));
    }
//    Set<Point> corners = new HashSet<Point>();

    public String getName() {
        return name;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
}
