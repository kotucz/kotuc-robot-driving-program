package vision.input;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.CannotRealizeException;
import javax.media.NoPlayerException;

/**
 *
 * @author kotuc
 */
public class VideoInputs {

    private static VideoInput video;

    public static VideoInput getVideo() {
        if (null == video) {
            if (true) {
                try {
                    // JMF (windows with JMF)
                    video = JMFVideoInput.startVideo("vfw:Microsoft WDM Image Capture (Win32):0");
                    Thread.sleep(4321);
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(VideoInputs.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(VideoInputs.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(VideoInputs.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoPlayerException ex) {
                    Logger.getLogger(VideoInputs.class.getName()).log(Level.SEVERE, null, ex);
                } catch (CannotRealizeException ex) {
                    Logger.getLogger(VideoInputs.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                // image module
                video = new FileVideoInput(new File("D:/kotuc/robot-kamera/1/"));

            }
        }
        return video;
    }
}
