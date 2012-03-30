package robotour.hardware;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.navi.basic.Azimuth;

/**
 *
 * @see http://www.robot-electronics.co.uk/htm/usb_i2c_tech.htm 
 * @author Kotuc
 */
public class I2CUSB {

    private InputStream is;
    private OutputStream os;
    private final I2CUSB bus;
    private static final int REVISION = 0x01;

    private I2CUSB() {
        this.bus = this;
//        super("I2CUSB");
    }
    private final Object lock = this;

    public static I2CUSB getI2C(String portName) throws PortInUseException, UnsupportedCommOperationException, IOException {
        CommPortIdentifier portId = Ports.getPortIdentifier(portName);
        if (portId == null) {
            throw new IllegalArgumentException("Port not found: " + portName);
        }
        SerialPort serial = (SerialPort) portId.open("Robotour I2C", 2000);

        serial.setSerialPortParams(19200, SerialPort.DATABITS_8,
                SerialPort.STOPBITS_2,
                SerialPort.PARITY_NONE);

        I2CUSB i2c = new I2CUSB();
        System.out.println("I2C Init");

        i2c.os = serial.getOutputStream();
        i2c.is = serial.getInputStream();

        int ver = i2c.readVersion();
        System.out.println("USB-I2C Version: " + ver);
        if (ver > 0) {
            System.out.println(i2c.is);
            System.out.println(i2c.os);

            return i2c;
        } else {
            throw new IOException(i2c+" does not respond!");
        }

    }

    public I2CDevice getDevice(int address) {
        return new I2CDevice(address);
    }

    public class I2CDevice {

        private final int address;

        private I2CDevice(int address) {
            this.address = address;
        }

        public int getAddress() {
            return address;
        }

        public I2CUSB getBus() {
            return bus;
        }

        /**
         *
         * @param register register to write
         * @param data data to write
         * @return 0 if fails, nonzero othwerwise
         * @throws IOException 
         */
        public int write(int register, int data) throws IOException {
            synchronized (lock) {
                return write(register, new byte[]{(byte) data});
            }
//            byte[] bytes = new byte[5];
//            bytes[0] = 0x55; // command byte  - 1 byte addres device
//            bytes[1] = (byte) address; // device address byte
//            bytes[2] = (byte) register; // internal address
//            bytes[3] = 1; // number of data bytes
//            bytes[4] = (byte) data; // data byte (s)
//            os.write(bytes);
//            os.flush();
//// debug info
//            int result = is.read();
////            System.out.println("write : " + Arrays.toString(bytes) + " result: " + result);
//            return result;// is.read(); // 0 - failure else OK
//
////                Logger.getLogger(I2CUSB.class.getName()).log(Level.SEVERE, null, ex);

        }

        /**
         *
         * @param register register to write
         * @param dataBytes
         * @return 0 if fails, nonzero othwerwise
         * @throws IOException 
         */
        public int write(int register, byte[] dataBytes) throws IOException {
            synchronized (lock) {
                byte[] bytes = new byte[4 + dataBytes.length];
                bytes[0] = 0x55; // command byte  - 1 byte addres device
                bytes[1] = (byte) address; // device address byte
                bytes[2] = (byte) register; // internal address
                bytes[3] = (byte) dataBytes.length; // number of data bytes

                for (int i = 0; i < dataBytes.length; i++) {
                    bytes[4 + i] = dataBytes[i]; // data byte (s)
//                bytes[4] = (byte) data; 
                }

                os.write(bytes);
                os.flush();
// debug info
                int result = is.read();
//            System.out.println("write : " + Arrays.toString(bytes) + " result: " + result);
                return result;// is.read(); // 0 - failure else OK

//                Logger.getLogger(I2CUSB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        /**
         *
         * @param register register to read
         * @return
         * @throws IOException 
         */
        public int read(int register) throws IOException {
            synchronized (lock) {
                byte[] bytes = new byte[4];
                bytes[0] = 0x55; // command byte - 1 byte addres device
                bytes[1] = (byte) (address + 1); // odd number to read
                bytes[2] = (byte) register; // register to read
                bytes[3] = 2; // number of data bytes to read
//                bytes = new byte[]{0x55, (byte)0xE1, 0x02, 0x02};
                os.write(bytes);
                os.flush();
                int hi = is.read();
                int lo = is.read();
                int hl = (hi << 8) + lo;
                if (hl == 65535) {
                    throw new IOException("Device connection error 65535");
                }
// debug info
//            System.out.println("read : " + Arrays.toString(bytes) + " result : " + hi + "," + lo + " = " + hl);
                return hl;
            }

        }

        @Override
        public String toString() {
            return bus + ":" + Integer.toHexString(address);
        }
    }

    synchronized int readVersion() throws IOException {
        byte[] bytes = new byte[4];
        bytes[0] = 0x5A; // mode
        bytes[1] = REVISION; // scan3
        bytes[2] = 0; // anything
        bytes[3] = 0; // anything
        os.write(bytes);
        os.flush();
        int version = is.read();
        return version;
    }

    synchronized void requestScan3() {
        try {
            byte[] bytes = new byte[4];
            bytes[0] = 0x5A; // mode
            bytes[1] = 0x06; // scan3
            bytes[2] = 0; // md22
            bytes[3] = 0; // md22
            os.write(bytes);
            os.flush();
            int battery = is.read();
        } catch (IOException ex) {
            Logger.getLogger(I2CUSB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    synchronized void scan3() {

        try {


            /*(reads 0x00)
            Compass bearing high byte
            Compass bearing low byte
             */
            int battery = is.read();
            int cmps = (is.read() << 8) + is.read();
            Azimuth azimuzh = Azimuth.valueOfDegrees(cmps / 10.0);
            System.out.println("Compass " + azimuzh);



            /*
            SRF08 at 0xE0, 0xE2, 0xE4:
             */
            for (int i = 0; i < 3; i++) {
//            try {
//                Thread.sleep(800);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(I2CUSB.class.getName()).log(Level.SEVERE, null, ex);
//            }
                /*
                Light sensor
                Range high byte
                Range low byte
                 */
                int light = is.read();
                int range = (is.read() << 8) + is.read();
                double distance = range / 5800.0; // us to cm to metres
                System.out.println("Sonar " + i + ": " + light + "\t" + distance + " m");
            }
        } catch (IOException ex) {
            Logger.getLogger(I2CUSB.class.getName()).log(Level.SEVERE, null, ex);
        }



    }

    public static void main(String[] args) throws PortInUseException, UnsupportedCommOperationException, IOException {
        System.out.println("USAGE: java I2CUSB i2cusbport");
        //I2CUSB i2c = I2CUSB.getI2C("COM4");
        I2CUSB i2c = I2CUSB.getI2C(args[0]);

        long lastNano = System.nanoTime();

        while (true) {
            System.out.println("SCAN3");
            i2c.requestScan3();
            i2c.scan3();
            long nowNano = System.nanoTime();
            System.out.println("Duration " + (nowNano - lastNano) + " ns");
            lastNano = nowNano;
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(I2CUSB.class.getName()).log(Level.SEVERE, null, ex);
//            }
        }

    }

    @Override
    public String toString() {
        return "i2c";
    }
}
