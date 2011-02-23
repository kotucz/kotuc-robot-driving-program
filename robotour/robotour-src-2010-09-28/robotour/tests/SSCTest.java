package robotour.tests;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.hardware.SSC32;

/**
 *
 * @author Tomas
 */
public class SSCTest {

    /**
     *  tests controling.. changes few times servost 30 and 31 and banks 0 and 1
     * @param args
     * @throws PortInUseException
     * @throws IOException
     * @throws UnsupportedCommOperationException
     */
    public static void main(String[] args) throws PortInUseException, IOException, UnsupportedCommOperationException, NoSuchPortException {
        System.err.println("Usage: java " + SSCTest.class.getCanonicalName() + " <serialport>");
//        SSC32 ssc = SSC32.getSSC32(args[0]);
//        Ports.getPortIdentifier("COM12");
        SSC32 ssc = SSC32.getSSC32("COM12");
        try {
            for (int i = 0; i < 10; i++) {
                System.out.println("Version: " + ssc.readVersion());
                System.out.println("Digital input A :" + ssc.readDigitalInput(SSC32.InputPin.A));
                System.out.println("Digital input B :" + ssc.readDigitalInput(SSC32.InputPin.B));
                System.out.println("Digital input C :" + ssc.readDigitalInput(SSC32.InputPin.C));
                System.out.println("Digital input D :" + ssc.readDigitalInput(SSC32.InputPin.D));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(SSC32.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(SSC32.class.getName()).log(Level.SEVERE, null, ex);
        }



//        ssc.sendCommand("#0:0 #1:0");
//        ssc.sendCommand("#30 P750 T3000 #31 P2250 T3000");
//
////        ssc.sendCommand("#15H");
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException ex) {
//            ex.printStackTrace();
//        }
//
//        ssc.sendCommand("#0:1 #1:1");
//        ssc.sendCommand("#30 P2250 T3000 #31 P750 T3000");
//
//
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException ex) {
//            ex.printStackTrace();
//        }
//
//        ssc.sendCommand("#0:255 #1:255");
//        ssc.sendCommand("#30 P1500 T3000 #31 P750 T3000");
//
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException ex) {
//            ex.printStackTrace();
//        }


//        ssc.sendCommand("#0:0 #1:0");
//        ssc.sendCommand("#30 P750 T3000 #31 P2250 T3000");
//
////        ssc.sendCommand("#15H");
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException ex) {
//            ex.printStackTrace();
//        }
//
//        ssc.sendCommand("#0:1 #1:1");
//        ssc.sendCommand("#30 P2250 T3000 #31 P750 T3000");
//
//
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException ex) {
//            ex.printStackTrace();
//        }
//
//        ssc.sendCommand("#0:255 #1:255");
//        ssc.sendCommand("#30 P1500 T3000 #31 P750 T3000");
//
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException ex) {
//            ex.printStackTrace();
//        }

//        ssc.sendCommand("#15L");

        ssc.shutdown();

        System.exit(0);


    }
}
