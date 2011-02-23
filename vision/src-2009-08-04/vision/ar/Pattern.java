package vision.ar;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

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

    public Pattern(int pw, int ph) {
        image = new BufferedImage(pw, ph, BufferedImage.TYPE_BYTE_BINARY);
    }

    public void set(int x, int y, boolean binary) {
        image.setRGB(x, y, (binary) ? BLACK : WHITE);
    }

    public BufferedImage getImage() {
        return image;
    }

    public Pattern(BufferedImage image, String name, double angle) {
        this.image = image;
        this.name = name;
        this.angle = angle;
        corners.add(new Point(20, 20));
        corners.add(new Point(20, 70));
        corners.add(new Point(70, 20));
        corners.add(new Point(70, 70));
    }
    Set<Point> corners = new HashSet<Point>();

    public String getName() {
        return name;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
}
