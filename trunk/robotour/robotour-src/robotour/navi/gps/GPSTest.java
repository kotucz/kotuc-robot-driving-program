package robotour.navi.gps;

import robotour.navi.gps.input.GPSInput;
import gnu.io.NoSuchPortException;
import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;

/**
 *
 * @author Kotuc
 */
public class GPSTest {

    /**
     * @param args
     */
    public static void main(String[] args) throws IllegalArgumentException, IOException, NoSuchPortException {

        System.err.println("Usage: java GPSInput <gpsport>");

//        GPSInput gpsi = GPSInput.opportenGPSInput(args[0]);
//        GPSInput gpsi = GPSInput.openPort("COM4");

        // linux like
//        GPSInput gpsi = GPSInput.openFile(new File("COM7"));

        // stored file
//        GPSInput gpsi = GPSInput.openFile(new File("C:/Users/Kotuc/Desktop/gps0001.TXT"));
//        GPSInput gpsi = GPSInput.openFile(new File("C:/Users/Kotuc/Desktop/gps0002.TXT"));
        GPSInput gpsi = GPSInput.openFile(new File("C:/Users/Kotuc/Desktop/putty.log"));

        JFrame frame = new JFrame("GPS Input Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new GPSPanel(gpsi));
        frame.pack();
        frame.setVisible(true);

    }
}
