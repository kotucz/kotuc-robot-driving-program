package vision.pathfinding.birdseye;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class FloorImage extends Floor {

    private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    private Graphics2D g = image.createGraphics();

    public Graphics2D getGraphics() {
        return g;
    }



    public FloorImage() {
        g.setColor(Color.GREEN);
        g.drawRect(0, 0, image.getWidth() - 1, image.getHeight() - 1);
    }

    public void setRGB(double xm, double ym, int rgb) {
        if (rect.contains(xm, ym)) {
            try {
//                image.setRGB(getX(xm), image.getHeight() - getY(ym), val);

                image.setRGB(getX(xm), getY(ym), rgb);
            } catch (Exception e) {
            }
        }
    }

    public int getRGB(double xm, double ym) {
        if (rect.contains(xm, ym)) {
            try {
//                image.setRGB(getX(xm), image.getHeight() - getY(ym), val);

                return image.getRGB(getX(xm), getY(ym));
            } catch (Exception e) {
            }

        }
        return 0;
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }

    
}
