package cambot.log;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import vision.input.VideoInput;

/**
 *
 * @author Kotuc
 */
public class VideoLog {

    public static VideoInput getLoggedVideoInput(final VideoInput vi) {
        return getLoggedVideoInput(vi, getVideoLogDir());
    }
    private static File vidLogDir;

    public static File getVideoLogDir() {
        if (vidLogDir == null) {
            throw new NotImplementedException();
//            vidLogDir = new File(SerialLog.getRunLogDirectory(), "video/");
//            vidLogDir.mkdir();
        }
        return vidLogDir;
    }

    public static File createVideoSnapFile(long snaptime) {
        return new File(getVideoLogDir(), "snap" + snaptime + ".png");
    }

    public static VideoInput getLoggedVideoInput(final VideoInput vi, final File viLogDir) {

        return new VideoInput() {

            public BufferedImage snap() {
                BufferedImage image = vi.snap();
                try {
                    ImageIO.write(image, "png", createVideoSnapFile(System.currentTimeMillis()));
                } catch (IOException ex) {
                    Logger.getLogger(VideoLog.class.getName()).log(Level.SEVERE, null, ex);
                }
                return image;
            }
        };

    }
}
