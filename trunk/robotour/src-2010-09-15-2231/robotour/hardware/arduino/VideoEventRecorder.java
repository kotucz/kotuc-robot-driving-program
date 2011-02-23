package robotour.hardware.arduino;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import robotour.util.log.SerialLog;
import robotour.util.log.events.CameraSnapEvent;
import robotour.util.log.events.EventLog;
import vision.input.VideoInput;

/**
 *
 * @author Kotuc
 */
public class VideoEventRecorder implements VideoInput {

    final VideoInput vi;
    final EventLog eventLog;

    public VideoEventRecorder(VideoInput vi, EventLog eventLog) {
        this.vi = vi;
        this.eventLog = eventLog;
    }

    public BufferedImage snap() {
        long currentTimeMillis = System.currentTimeMillis();
        BufferedImage snap = vi.snap();
        File file = SerialLog.createVideoSnapFile(currentTimeMillis);
        eventLog.eventRecieved(new CameraSnapEvent(file.getName(), currentTimeMillis));
        try {
            ImageIO.write(snap, "png", file);
        } catch (IOException ex) {
            Logger.getLogger(SerialLog.class.getName()).log(Level.SEVERE, null, ex);
        }
        return snap;
    }

    public void startRecording() {
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {                
                snap();                
            }
        }, 1000, 250);
        System.out.println("Recording started");
    }

}
