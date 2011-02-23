package vision.pathfinding.knowledge;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 *  * @author Kotuc
 */
public class KnownRecog {

    private BufferedImage image;
    private Analyzer analyzer = new Analyzer();
    private Knowledge knowledge;

    public void createKnowledge(File dir) {
        this.knowledge = new Knowledge();
        knowledge.createKnowledge(analyzer, dir);
    }

    public BufferedImage getResult() {
        return image;
    }
    private Info sample;
    public Info[] infos;
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

        final int snapy = iw / 4; // random?
        final int snapn = 10;
        final int snapw = iw / snapn;
        final int snaph = ih / 10;

        Rectangle rectangle = new Rectangle(iw / 2 - iw / 10, ih - ih / 5, iw / 5, ih / 5);
        sample = analyzer.characteristics(image, rectangle);
        sample.name = "GOOD";
        sample.image = image;

        
        this.infos = new Info[snapn];
        for (int i = 0; i < snapn; i++) {
            Info info = analyzer.characteristics(image, new Rectangle(i * snapw, snapy, snapw, snaph));
//            max = Math.max((int) info.diff(sample), max);
            double bestVal = info.diff(sample);
            info.match = sample;
            for (Info known1 : knowledge.knowledge) {
                double diff = known1.diff(info);
                if (diff < bestVal) {
                    info.match = known1;
                    bestVal = diff;
                }
            }

            infos[i] = info;
        }

        paintDebug(g);

    }

    void paintDebug(Graphics g) {
        for (int i = 0; i < infos.length; i++) {
            Info info = infos[i];
            info.color = 0xFF000000 | ((int) Math.min(255 * info.diff(sample) / max, 255) << 16);

            g.setColor(new Color(info.color));

            g.fillRect(info.rect.x, 0, info.rect.width, info.rect.height);

//            g.setColor(Color.getHSBColor((float)info.hue.avg(), 1, 1));
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
