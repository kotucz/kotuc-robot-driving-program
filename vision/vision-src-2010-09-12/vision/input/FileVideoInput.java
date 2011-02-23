package vision.input;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Kotuc
 */
public class FileVideoInput implements VideoInput {

    private final File file;

    FileVideoInput(File file) {
        if (!file.isDirectory()) {
            throw new IllegalArgumentException(file + " is not a directory.");
        }
        this.file = file;
    }
    private static final FilenameFilter filter = new FilenameFilter() {

        public boolean accept(File dir, String name) {
            return name.endsWith("png");
        }
    };

    public BufferedImage snap() {
        try {
            File[] files = file.listFiles(filter);
//            File f = files[(int) (Math.random() * (files.length - 1))];
            File f = files[files.length];
            System.out.println("Snapping: " + f);
            return ImageIO.read(f);
//            } else {
//                return ImageIO.read(file);
//            }
        } catch (IOException ex) {
            Logger.getLogger(FileVideoInput.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
