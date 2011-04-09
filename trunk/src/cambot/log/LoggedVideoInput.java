package cambot.log;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import robotour.util.log.events.CameraSnapEvent;
import robotour.util.log.events.EventLog;
import vision.input.VideoInput;

/**
 *
 * @author Kotuc
 */
public class LoggedVideoInput implements VideoInput {

    final VideoInput vi;
    final EventLog eventLog;
    final ImageEventRecorder ier;

    public LoggedVideoInput(VideoInput vi, EventLog eventLog) {
        this.vi = vi;
        this.eventLog = eventLog;
        this.ier = new ImageEventRecorder(eventLog);
    }

    public BufferedImage snap() {
        long currentTimeMillis = System.currentTimeMillis();
        BufferedImage snap = vi.snap();
        ier.snap(snap, currentTimeMillis);
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
