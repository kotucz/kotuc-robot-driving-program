package vision.ar;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.vecmath.Vector2d;

/**
 *
 * @author Kotuc
 */
public class PatternMatcher {

    private List<Pattern> patterns = new ArrayList<Pattern>();

    public PatternMatcher() {
        loadPattern("stairs");
        loadPattern("face");
    }

    private void loadPattern(File file, String name, double angle) {
        try {
            patterns.add(new Pattern(ImageIO.read(file), name, angle));
        } catch (IOException ex) {
            Logger.getLogger(PatternMatcher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadPattern(String name) {
        loadPattern(new File("./patterns/pattern-" + name + "-0.png"), name, 0.0);
        loadPattern(new File("./patterns/pattern-" + name + "-90.png"), name, -Math.PI / 2);
        loadPattern(new File("./patterns/pattern-" + name + "-270.png"), name, Math.PI / 2);
        loadPattern(new File("./patterns/pattern-" + name + "-180.png"), name, Math.PI);
    }

    /**
     * return the most fitting pattern if exists
     */
    Pattern recognize(Pattern sample) {
        Pattern best = null;
        double bestMatch = 0.9; // minimum available
        for (Pattern patt : patterns) {
            double match = matchPatterns(patt, sample);
            if (match > bestMatch) {
                bestMatch = match;
                best = patt;
            }
        }
        return best;
    }

    /**
     *
     */
    private double matchPatterns(Pattern pattern, Pattern sample) {

        BufferedImage im1 = pattern.getImage();
        BufferedImage im2 = sample.getImage();


        int diff = 0;
        final int iw = im1.getWidth();
        final int ih = im1.getHeight();
        for (int x = 0; x < iw; x++) {
            for (int y = 0; y < ih; y++) {
                // tresholded image
//                if (pattern.getRGB(x, y) != sample.getRGB(x, y)) {
//                    diff++;
//                }
                if (im1.getRGB(x, y) != im2.getRGB(x, y)) {
                    diff++;
                }
            //diff += Colors.dist(pattern.getRGB(x, y), sample.getRGB(x, y));
            }
        }

        return 1 - ((double) diff / (iw * ih));

    }

    /**
     *
     * @param original
     * @return normalized pattern of sqare object to be compared via PatternMatcher
     */
    public static Pattern snapPattern(BufferedImage original, Point ncorn, Point ecorn, Point scorn, Point wcorn) {
        final int pw = 90;
        final int ph = 90;

        BufferedImage pattern = new BufferedImage(pw, ph, BufferedImage.TYPE_INT_RGB);
        Graphics g = pattern.createGraphics();
        Vector2d a = new Vector2d(ncorn.x, ncorn.y);
        Vector2d b = new Vector2d(ecorn.x, ecorn.y);
        Vector2d c = new Vector2d(scorn.x, scorn.y);
        Vector2d d = new Vector2d(wcorn.x, wcorn.y);

        Vector2d m = new Vector2d();
        Vector2d n = new Vector2d();
        Vector2d f = new Vector2d();
        for (int x = 0; x < pw; x++) {
            for (int y = 0; y < ph; y++) {
                m.interpolate(a, b, (double) x / pw);
                n.interpolate(d, c, (double) x / pw);
                f.interpolate(m, n, (double) y / ph);
                pattern.setRGB(x, y, original.getRGB((int) f.x, (int) f.y));
            }
        }
        return new Pattern(pattern, "snapped", 0);
    }
}
