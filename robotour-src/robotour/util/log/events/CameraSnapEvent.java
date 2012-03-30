package robotour.util.log.events;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Kotuc
 */
public class CameraSnapEvent extends Event {

//    private BufferedImage image;
    private final String filename;

    public CameraSnapEvent(String filename, long time) {
        super(EventType.camera, time);
        this.filename = filename;
    }

//    public CameraSnapEvent(BufferedImage image, long time) {
//        super(EventType.camera, time);
//        this.image = image;
//    }
    public BufferedImage getImage(File dir) throws IOException {
        return ImageIO.read(new File(dir, filename));
    }

    String getFilename() {
        return filename;
//        return SerialLog.createVideoSnapFile(getTime()).getName();
//        return "snap"+getTime()+".png";
    }
}
