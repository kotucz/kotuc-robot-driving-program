package robotour.hardware;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.iface.MeasureException;
import robotour.iface.Sonar;
import robotour.rt.RealTime;

/**
 *
 * @author Kotuc
 * @see http://www.robot-electronics.co.uk/htm/srf08tech.shtml
 */
public class SRF08 implements Sonar {

    private static final int COMMAND_REGISTER = 0x00;
    private static final int GAIN_REGISTER = 0x01;
    private static final int PING_CM = 0x51;
    private static final int RANGE_REGISTER = 0x02;

//    private I2CUSB i2c = DeviceManager.getI2C();
    private final I2CUSB.I2CDevice dev;
    private int measureTime = 65;

    public SRF08(I2CUSB i2C, int address) {
        if (null == i2C) {
            throw new NullPointerException("i2C");
        }
        if (address != 0xE0 && address != 0xE2 && address != 0xE4) {
            new IllegalArgumentException("address " + Integer.toHexString(address)).printStackTrace();
        }
        dev = i2C.getDevice(address);
//        try {
//            changeRange(26);
//        } catch (IOException ex) {
//            Logger.getLogger(SRF08.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public synchronized double getDistance() throws MeasureException {
        synchronized (dev.getBus()) {
            try {
                changeRange(26);

                // start ranging in cm
                dev.write(COMMAND_REGISTER, PING_CM);
                try {
                    RealTime.sleep(measureTime, 0); //
                } catch (InterruptedException ex) {
                    Logger.getLogger(SRF08.class.getName()).log(Level.SEVERE, null, ex);
                }
                return 0.01 * dev.read(2); // read register 2 - 1st echo
            } catch (IOException ex) {
                throw new MeasureException(ex);
            }
        }
    }

//    public static void main(String[] args) {
//        SRF08 srfc = new SRF08(DeviceManager.getI2C(), 0xE0);
//        SRF08 srfl = new SRF08(DeviceManager.getI2C(), 0xE2);
//        SRF08 srfr = new SRF08(DeviceManager.getI2C(), 0xE4);
//        while (true) {
//            try {
//                System.out.println("Distances:\t" + srfl.getDistance() + "m\t" + srfc.getDistance() + "m\t" + srfr.getDistance() + "m");
////            System.out.println("Distance LEFT : "+srfl.getDistance()+ " m");
////            try {
////                Thread.sleep(100);
////            } catch (InterruptedException ex) {
////                Logger.getLogger(SRF08.class.getName()).log(Level.SEVERE, null, ex);
////            }
////            System.out.println("Distance CENTER : "+srfc.getDistance()+ " m");
////            try {
////                Thread.sleep(100);
////            } catch (InterruptedException ex) {
////                Logger.getLogger(SRF08.class.getName()).log(Level.SEVERE, null, ex);
////            }
////            System.out.println("Distance RIGHT : "+srfr.getDistance()+ " m");
////            try {
////                Thread.sleep(100);
////            } catch (InterruptedException ex) {
////                Logger.getLogger(SRF08.class.getName()).log(Level.SEVERE, null, ex);
////            }
//            } catch (Exception ex) {
//                Logger.getLogger(SRF08.class.getName()).log(Level.SEVERE, null, ex);
//            }
////            System.out.println("Distance LEFT : "+srfl.getDistance()+ " m");
////            try {
////                Thread.sleep(100);
////            } catch (InterruptedException ex) {
////                Logger.getLogger(SRF08.class.getName()).log(Level.SEVERE, null, ex);
////            }
////            System.out.println("Distance CENTER : "+srfc.getDistance()+ " m");
////            try {
////                Thread.sleep(100);
////            } catch (InterruptedException ex) {
////                Logger.getLogger(SRF08.class.getName()).log(Level.SEVERE, null, ex);
////            }
////            System.out.println("Distance RIGHT : "+srfr.getDistance()+ " m");
////            try {
////                Thread.sleep(100);
////            } catch (InterruptedException ex) {
////                Logger.getLogger(SRF08.class.getName()).log(Level.SEVERE, null, ex);
////            }
//        }

    // changing address
//        int oldAddress = 0xE0;
//        int newAddress = 0xE4; // only E0, E2, E4, E6, E8, EA, EC, EE, F0, F2, F4, F6, F8, FA, FC or FE
//
//        SRF08 srf = new SRF08(DeviceManager.getI2C(), oldAddress);
//        try {
//            if (srf.changeAddress(newAddress)!=0) {
//                System.out.println("OK "+Integer.toHexString(oldAddress)+" address changed to "+Integer.toHexString(newAddress));
//            } else {
//                System.out.println("Failed");
//            }
//        } catch (IOException ex) {
//            Logger.getLogger(SRF08.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        System.exit(0);
    // }
    /**
     * changes address of current device
     * @param newAddress
     * @return 0 i2c write feedback
     */
    synchronized int changeAddress(int newAddress) throws IOException {
//        return dev.write(0x00, new byte[] {(byte)0xA0, (byte)0xAA, (byte)0xA5, (byte)newAddress});
        synchronized (dev.getBus()) {
            return dev.write(0x00, 0xA0) |
                    dev.write(0x00, 0xAA) |
                    dev.write(0x00, 0xA5) |
                    dev.write(0x00, newAddress);
        }

    }

    /**
     *
     * time 0 ms .. 65 ms
     * range 43mm .. 11 m
     *
     * @param range 0 .. 255
     * @return
     */
    synchronized int changeRange(int range) throws IOException {
        synchronized (dev.getBus()) {
            if (range < 0 || range > 255) {
                throw new IllegalArgumentException("range: " + range);
            }

            this.measureTime = 1 + (int) (65.0 * range / 255);
//            System.out.println("measure time:" + measureTime);
            return dev.write(RANGE_REGISTER, range);
        }
    }

    /**
     *
     * Maximum possible gain is reached after about 390mm of range. 
     * 
     * @param gain 0 .. 31 nonlinear
     * @return
     */
    synchronized int changeGain(int gain) throws IOException {
        synchronized (dev.getBus()) {
            if (gain < 0 || gain > 31) {
                throw new IllegalArgumentException("gain: " + gain);
            }
            return dev.write(GAIN_REGISTER, gain);
        }
    }

    @Override
    public String toString() {
        return "SRF08(" + dev + ")";
    }
}
