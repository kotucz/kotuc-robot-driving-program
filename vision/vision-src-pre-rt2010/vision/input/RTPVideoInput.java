package vision.input;

import vision.*;
import vision.input.VideoInput;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.Buffer;
import javax.media.CannotRealizeException;
import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;
import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.control.FrameGrabbingControl;
import javax.media.format.VideoFormat;
import javax.media.util.BufferToImage;
import vision.ShutdownManager;
import vision.Shutdownable;

/**
 *
 * @author Tomas
 */
public class RTPVideoInput implements VideoInput, Shutdownable {

    private final Player player;
//    private final Processor processor;
//    private final CaptureDeviceInfo deviceInfo;
    private final FrameGrabbingControl fgc;

//    private VideoFormat format;

//    private Component controlPanel;
//    private Component visualComponent;
    private RTPVideoInput(CaptureDeviceInfo deviceInfo) throws IOException, NoPlayerException, CannotRealizeException {
        // Create a Player for the capture device
//        player = Manager.createRealizedPlayer(new MediaLocator("vfw://0"));
        player = Manager.createRealizedPlayer(deviceInfo.getLocator());
        player.prefetch();
        player.start();
        getLogger().log(Level.SEVERE, "player created");
        fgc = (FrameGrabbingControl) player.getControl("javax.media.control.FrameGrabbingControl");

//           proc = Manager.createRealizedProcessor(new ProcessorModel(deviceInfo.getLocator(), new Format[] {format}, new FileTypeDescriptor(FileTypeDescriptor.MSVIDEO)));
//             if (fgc==null) fgc = (FrameGrabbingControl)proc.getControl("javax.media.control.FrameGrabbingControl");;
//            visualComponent = player.getVisualComponent();
//            controlPanel = player.getControlPanelComponent();

        System.out.println(fgc);
        ShutdownManager.registerStutdown(this);
//        devicePanel = cameraPanel;
    }
    private BufferToImage bti;

    public BufferedImage snap() {
        Buffer buf = fgc.grabFrame();

//        if (bti == null) {
        bti = new BufferToImage((VideoFormat) buf.getFormat());
//        }
        return (BufferedImage) bti.createImage(buf);
    }

    /**
     * starts the video input, so it can be snapped
     */
    static VideoInput startVideo(String deviceName) throws IOException, NoPlayerException, CannotRealizeException {

        CaptureDeviceInfo deviceInfo = CaptureDeviceManager.getDevice(deviceName);

        if (deviceInfo == null) {
            throw new IllegalArgumentException("Not such device: " + deviceName);
        }

//        System.out.println("LOCATOR: "+deviceInfo.getLocator().toExternalForm());

        return new RTPVideoInput(deviceInfo);

    }

    static void listCaptureDevices() {
        System.out.println("Listing capture devices:");
        for (Object object : CaptureDeviceManager.getDeviceList(null)) {
            System.out.println(object);
        }

    }

    @Override
    public void shutdown() {
        close();
    }

    public void close() {
        getLogger().log(Level.SEVERE, "closing player");
        try {
            player.stop();
            player.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
//        getLogger().log(Level.SEVERE, "closing processor");
//        try {
//            processor.stop();
//            processor.close();
//        } catch (Exception ex) {
//        }
        getLogger().log(Level.SEVERE, "closed");
    }

    private static Logger getLogger() {
        return Logger.getLogger(RTPVideoInput.class.getName());
    }
}
