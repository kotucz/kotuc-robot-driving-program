package vision.pathfinding.knowledge;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 *  * @author Kotuc
 */
public class KnownRecog2 {

    private BufferedImage image;
    private Analyzer analyzer = new Analyzer();
    private Knowledge knowledge;
    private static final int WN = 32;
    private static final int HN = 30;

    public void createKnowledge(File dir) {
        this.knowledge = new Knowledge();
        knowledge.createKnowledge(analyzer, dir);
    }

    public BufferedImage getResult() {
        return image;
    }
    List<Info> infos;
    int max = 0;

    public void process(BufferedImage image) {

        if (knowledge == null) {
            throw new IllegalStateException("knowledge not initialized");
        }

        final int iw = image.getWidth();
        final int ih = image.getHeight();

//        BufferedImage conv = BasicOps.convolve(image, 3);

        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setStroke(new BasicStroke(3));

        this.image = image;

        int w = iw / WN;
        int h = ih / HN;

        Rectangle rectangle = new Rectangle(iw / 2 - iw / 10, ih - ih / 5, iw / 5, ih / 5);

        Info sample = analyzer.characteristics(image, rectangle);
        sample.name = "GOOD";
        sample.image = image;

        this.infos = new LinkedList<Info>();
        for (int i = 0; i < WN; i++) {
            for (int j = 0; j < HN; j++) {
                Info info = analyzer.characteristics(image, new Rectangle(w * i + 1, h * j + 1, w - 3, h - 3));
                max = Math.max((int) info.diff(sample), max);
                double bestVal = info.diff(sample);
                for (Info known1 : knowledge.knowledge) {
                    double diff = known1.diff(info);
                    if (diff < bestVal) {
                        info.match = known1;
                        bestVal = diff;
                    }
                }
                infos.add(info);
            }
        }

        paintDebug(g);

    }

    void paintDebug(Graphics g) {

        for (Info info : infos) {

            info.color = 0xFF000000 | ((int) Math.min(255.0 * info.diff(info.match) / max, 255) << 16);

            g.setColor(new Color(info.color));

            g.fillRect(info.rect.x, info.rect.y, info.rect.width, info.rect.height);

            g.setColor(Color.getHSBColor((float) info.hue.avg(), 1, 1));
            g.setColor(Color.getHSBColor(0, 0, (float) info.sat.avg()));
            g.drawRect(info.rect.x, info.rect.y, info.rect.width, info.rect.height);

            if (info.match != null) {
                g.drawImage(info.match.image, info.rect.x, info.rect.y - info.rect.height, info.rect.width, info.rect.height, null);
                info.good = !info.match.name.startsWith("B");

                if (info.isGood()) {
                    g.setColor(Color.GREEN);
                } else {
                    g.setColor(Color.ORANGE);
                }
            } else {
                g.setColor(Color.getHSBColor(0, 1, 1));
            }
            g.drawRect(info.rect.x, info.rect.y - info.rect.height, info.rect.width, info.rect.height);

        }
    }
}
