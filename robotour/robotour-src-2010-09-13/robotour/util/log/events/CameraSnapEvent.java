package robotour.util.log.events;

import java.awt.image.BufferedImage;
import robotour.util.log.SerialLog;

/**
 *
 * @author Kotuc
 */
public class CameraSnapEvent extends Event {

    private BufferedImage image;

    public CameraSnapEvent(BufferedImage image, long time) {
        super(EventType.camera, time);
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    String getFilename() {
        return SerialLog.createVideoSnapFile(getTime()).getName();
//        return "snap"+getTime()+".png";
    }

    
}
