package robotour.tests;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import robotour.hardware.SSC32;

/**
 *
 * @author Tomas
 */
public class AccelerationTest {
    public static void main(String[] args) throws UnsupportedCommOperationException, PortInUseException, IOException, InterruptedException, NoSuchPortException {
        SSC32 ssc = SSC32.getSSC32(args[0]);
        System.out.println("SET");
        ssc.sendCommand("#1 P1500");
        Thread.sleep(1000);
        System.out.println("GO");
        ssc.sendCommand("#1 P2000 S100");
        Thread.sleep(5000);
        System.out.println("MAX");
        ssc.sendCommand("#1 P1500 S100");
        Thread.sleep(5000);
        System.out.println("STOP");
        System.exit(0);
    }
}
