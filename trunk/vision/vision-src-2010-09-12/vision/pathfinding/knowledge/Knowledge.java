package vision.pathfinding.knowledge;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import vision.BasicOps;

/**
 *
 * @author Kotuc
 */
public class Knowledge {

    List<Info> knowledge;

    public void createKnowledge(Analyzer analyzer, File dir) {
        knowledge = new LinkedList<Info>();
        if (!dir.isDirectory()) {
            dir = dir.getParentFile();
        }
        final File[] listFiles = dir.listFiles();
        for (File file1 : listFiles) {
            try {
                BufferedImage img = ImageIO.read(file1);
                Info info = analyzer.characteristics(img, new Rectangle(0, 0, img.getWidth(), img.getHeight()));
                info.name = file1.getName();
                info.image = BasicOps.getResizedImage(img, 64, 48);
                System.out.println("Knowlege " + info.name);
                knowledge.add(info);
            } catch (IOException ex) {
                Logger.getLogger(KnownRecog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Loaded knowledge: " + knowledge.size());
        if (knowledge.isEmpty()) {
            throw new IllegalStateException("knowledge is empty!");
        }
    }

//    public double classify(Info info) {
//
//    }

}
