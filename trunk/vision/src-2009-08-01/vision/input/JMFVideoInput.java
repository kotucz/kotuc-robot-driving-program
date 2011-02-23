package vision.input;

import vision.Shutdownable;
import vision.ShutdownManager;
import javax.media.CannotRealizeException;
import javax.media.NoPlayerException;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.Buffer;
import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;
import javax.media.Format;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.media.control.FrameGrabbingControl;
import javax.media.format.VideoFormat;
import javax.media.util.BufferToImage;
import javax.swing.JOptionPane;
import vision.Setup;

/**
 *
 * WARNING: Do not use this class without adding JMF libraries into CLASSPATH!
 * No devices will be found!
 *
 * @author Kotuc
 */
public class JMFVideoInput implements VideoInput, Shutdownable {

    private static final String DEFAULT_DEVICE_KEY = "device.camera.device";
    private static final String DEFAULT_FORMAT_KEY = "device.camera.format";
    private final Player player;
//    private final Processor processor;
//    private final CaptureDeviceInfo deviceInfo;
    private final FrameGrabbingControl fgc;

//    private VideoFormat format;

//    private Component controlPanel;
//    private Component visualComponent;
    private JMFVideoInput(CaptureDeviceInfo deviceInfo) throws IOException, NoPlayerException, CannotRealizeException {
        // Create a Player for the capture device
        player = Manager.createRealizedPlayer(new MediaLocator("vfw://0"));
//        player = Manager.createRealizedPlayer(deviceInfo.getLocator());
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

        return new JMFVideoInput(deviceInfo);

    }

    static void listCaptureDevices() {
        System.out.println("Listing capture devices: ");
        int num = 0;
        for (Object object : CaptureDeviceManager.getDeviceList(null)) {

            System.out.println((num++) + " " +object);
        }
        System.out.println("Found "+num+" devices.");
        if (num<1) {
            System.out.println("Bad thing happend! Make sure JMF libraries are in classpath!");
        }
    }

    /**
     * starts the video input, so it can be snapped
     */
    private static VideoInput startVideoOld() throws IOException, NoPlayerException, CannotRealizeException {

        //        CaptureDeviceInfo deviceInfo = CaptureDeviceManager.getDevice("deviceName");
        VideoFormat format = null;
//        format = new javax.media.format.YUVFormat(YUVFormat.YUV_YUYV);
//        format = new javax.media.format.RGBFormat();
//        format = new javax.media.format.JPEGFormat();
//        format = null;
// Get the CaptureDeviceInfo for the live audio capture device
        @SuppressWarnings("unchecked")
        Vector<CaptureDeviceInfo> deviceList = CaptureDeviceManager.getDeviceList(format);
//        Vector<CaptureDeviceInfo> deviceList = CaptureDeviceManager.getDeviceList(format);
        if (deviceList.isEmpty()) {
            getLogger().log(Level.SEVERE, "Threre are not devices of " + format + " format");
        } else {
//                getLogger().log(Level.SEVERE, "Threre are no devices of " + format + " format");
            getLogger().log(Level.INFO, "Input devices:");
            int i = 0;
            for (CaptureDeviceInfo di : deviceList) {
                getLogger().log(Level.INFO, "" + (i++) + ": " + di);
            }
        }
//        deviceInfo = deviceList.get(Setup.getInt(DEFAULT_DEVICE_KEY, 2)); // 2
//        format = (VideoFormat) deviceInfo.getFormats()[Setup.getInt(DEFAULT_FORMAT_KEY, 7)]; // 7

        return new JMFVideoInput(deviceList.get(Setup.getInt(DEFAULT_DEVICE_KEY, 2)));
    }

    /**
     * performs the sequence of selecting capture device and format
     * and stores it into Setup, so it is used in future calling of startVideo()
     */
    public static void selectDeviceDialog() {
        @SuppressWarnings("unchecked")
        Vector<CaptureDeviceInfo> deviceList = CaptureDeviceManager.getDeviceList(null);
//        Vector<CaptureDeviceInfo> deviceList = CaptureDeviceManager.getDeviceList(format);
        String[] cdnames = new String[deviceList.size()];
        for (int i = 0; i <
                cdnames.length; i++) {
            cdnames[i] = deviceList.get(i).getName();
        }

        String cdname = (String) JOptionPane.showInputDialog(
                null,
                "Select Input Device:",
                "Capture Device Select",
                JOptionPane.PLAIN_MESSAGE,
                null,
                cdnames,
                null);

        if (cdname != null) {
            for (int i = 0; i <
                    cdnames.length; i++) {
                if (cdname.equals(cdnames[i])) {
                    Setup.set(DEFAULT_DEVICE_KEY, "" + i);

                    // select format

                    Format[] formats = deviceList.get(i).getFormats();
                    Format fmt = (Format) JOptionPane.showInputDialog(
                            null,
                            "Select Input Device Format:",
                            "Capture Device Format Select",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            formats,
                            formats[Setup.getInt(DEFAULT_FORMAT_KEY, 0)]);

                    if (fmt != null) {
                        for (int j = 0; j <
                                formats.length; j++) {
                            if (fmt.equals(formats[j])) {
                                Setup.set(DEFAULT_FORMAT_KEY, "" + j);
                            }

                        }
                    }
                }
            }
        }
        Setup.store();
    }

    public Component getVisualComponent() {
        return player.getVisualComponent();
    }

    public Dimension getSize() {
        throw new UnsupportedOperationException();
//        return new Dimension();
//        return format.getSize();
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

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                listCaptureDevices();
//                try {
//                    JMFVideoInput vi = (JMFVideoInput) JMFVideoInput.startVideo("vfw:Microsoft WDM Image Capture (Win32):0");
//                    JFrame vf = new JFrame();
//                    vf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                    vf.add(vi.getVisualComponent());
////                vf.add(vi.controlPanel);
//                    vf.pack();
//                    vf.setVisible(true);
//                } catch (IOException ex) {
//                    Logger.getLogger(JMFVideoInput.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (NoPlayerException ex) {
//                    Logger.getLogger(JMFVideoInput.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (CannotRealizeException ex) {
//                    Logger.getLogger(JMFVideoInput.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                listCaptureDevices();
                // second cam
//                try {
//                    JMFVideoInput vi = (JMFVideoInput) JMFVideoInput.startVideo("vfw:Microsoft WDM Image Capture (Win32):0");
//                    JFrame vf = new JFrame();
//                    vf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                    vf.add(vi.getVisualComponent());
////                vf.add(vi.controlPanel);
//                    vf.pack();
//                    vf.setVisible(true);
//                } catch (IOException ex) {
//                    Logger.getLogger(JMFVideoInput.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (NoPlayerException ex) {
//                    Logger.getLogger(JMFVideoInput.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (CannotRealizeException ex) {
//                    Logger.getLogger(JMFVideoInput.class.getName()).log(Level.SEVERE, null, ex);
//                }
            }
        });
    }

    private static Logger getLogger() {
        return Logger.getLogger("JMFVideoInput");
    }

//    public void println(String s) {
//        System.out.println(s);
//    }
}
