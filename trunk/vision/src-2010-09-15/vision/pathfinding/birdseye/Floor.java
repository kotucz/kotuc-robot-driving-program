package vision.pathfinding.birdseye;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public abstract class Floor {

    // floor image properties
    double sizexm = 40.0;
    double sizeym = 40.0;
    double offsetxm = -sizexm / 2.0;
    double offsetym = -sizeym / 2.0;
    Rectangle2D.Double rect = new Rectangle2D.Double(offsetxm, offsetym, sizexm, sizeym);
    final double pixelsperm = 10.0;
    final int width = (int) (sizexm * pixelsperm);
    final int height = (int) (sizeym * pixelsperm);

    public double getPixelsperm() {
        return pixelsperm;
    }

    public Rectangle2D.Double getRect() {
        return rect;
    }

    // possibly out of bounds
    public int getX(double xm) {
        xm -= offsetxm;
        return (int) (xm * pixelsperm);
    }

    public int getY(double ym) {
        ym -= offsetym;
        return (int) (ym * pixelsperm);
    }

    public boolean contains(double xm, double ym) {
        return rect.contains(xm, ym);
    }

    public abstract BufferedImage getImage();
}
