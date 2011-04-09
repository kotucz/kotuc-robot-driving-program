package cambot.log;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import robotour.util.log.SerialLog;
import robotour.util.log.events.CameraSnapEvent;
import robotour.util.log.events.EventLog;

/**
 *
 * @author Kotuc
 */
public class ImageEventRecorder {
    
    final EventLog eventLog;

    public ImageEventRecorder(EventLog eventLog) {        
        this.eventLog = eventLog;
    }

    public void snap(BufferedImage snap, long time) {
        File file = VideoLog.createVideoSnapFile(time);
        eventLog.eventRecieved(new CameraSnapEvent(file.getName(), time));
        try {
            ImageIO.write(snap, "png", file);
        } catch (IOException ex) {
            Logger.getLogger(SerialLog.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }


}
