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
import vision.BasicOps;

/**
 *  * @author Kotuc
 */
public class KnownRecog2 {

    private BufferedImage image;
    private Knowledge knowledge;
    private static final int WN = 32;
    private static final int HN = 30;
    private static final double sampleScaleFactor = 5;

    public void createKnowledge(File dir) {
        this.knowledge = Knowledge.createKnowledge(dir);
        for (Info info : knowledge.knowledge) {
            info.hist.scale(sampleScaleFactor);
        }
    }

    public BufferedImage getResult() {
        return image;
    }
    List<Info> infos;
    double max = 0;

    public void process(BufferedImage image) {

        if (knowledge == null) {
            throw new IllegalStateException("knowledge not initialized");
        }

        final int iw = image.getWidth();
        final int ih = image.getHeight();

//        BufferedImage conv = BasicOps.convolve(image, 3);       

        int w = iw / WN;
        int h = ih / HN;

        Rectangle rectangle = new Rectangle(iw / 2 - iw / 10, ih - ih / 5, iw / 5, ih / 5);

        Info sample = Info.createFrom(image, rectangle);
        sample.name = "GOOD";
        sample.image = image;
        sample.hist.scale(sampleScaleFactor);

        this.infos = new LinkedList<Info>();
        for (int i = 0; i < WN; i++) {
            for (int j = 0; j < HN; j++) {

                Info info = Info.createFrom(image, new Rectangle(w * i, h * j, w, h));

//                max = info.diff(sample);
//                double bestVal = info.diff(sample);
//                info.match = sample;

                double bestVal = max = 1;

                for (Info known1 : knowledge.knowledge) {
                    double diff = known1.diff(info);
                    max = Math.max(diff, max);
                    if (diff < bestVal) {
                        info.match = known1;
                        bestVal = diff;
                    }
                }

                infos.add(info);
            }
        }


        this.image = image = BasicOps.copy(image);

        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setStroke(new BasicStroke(3));

        paintDebug(g);



    }

    void paintDebug(Graphics g) {

        System.out.println("max " + max);

        for (Info info : infos) {

            if (info.match == null) {
                info.color = 0xFF0000FF;
            } else {
                info.color = 0xFF000000 | ((int) Math.min(255.0 * info.diff(info.match) / max, 255) << 16);
            }
            g.setColor(new Color(info.color));

            g.fillRect(info.rect.x, info.rect.y, info.rect.width, info.rect.height);

//            g.setColor(Color.getHSBColor((float) info.hue.avg(), 1, 1));
//            g.setColor(Color.getHSBColor(0, 0, (float) info.sat.avg()));
//            g.drawRect(info.rect.x, info.rect.y, info.rect.width, info.rect.height);
//
//            if (info.match != null) {
////                g.drawImage(info.match.image, info.rect.x, info.rect.y - info.rect.height, info.rect.width, info.rect.height, null);
//                g.drawImage(info.match.image, info.rect.x, info.rect.y, info.rect.width, info.rect.height, null);
//                info.good = !info.match.name.startsWith("B");
//
//                if (info.isGood()) {
//                    g.setColor(Color.GREEN);
//                } else {
//                    g.setColor(Color.ORANGE);
//                }
//            } else {
//                g.setColor(Color.getHSBColor(0, 1, 1));
//            }
//            g.drawRect(info.rect.x, info.rect.y - info.rect.height, info.rect.width, info.rect.height);

        }
    }
}
