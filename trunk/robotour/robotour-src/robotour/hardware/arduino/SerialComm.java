package robotour.hardware.arduino;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.hardware.Ports;
import robotour.util.log.SerialLog;
import robotour.util.ShutdownManager;
import robotour.util.Shutdownable;

/**
 *
 * @author Kotuc
 */
public class SerialComm implements SerialPortEventListener, Shutdownable {

    private final InputStream is;
    private final OutputStream os;
//    private BufferedReader reader;    
    private SerialPort serialPort;
    private final DataInputStream dataInputStream;
//    public SharpScanReader sharpReader;

    public SerialComm(InputStream inputStream, OutputStream outputStream) {
        this.is = inputStream;
        this.os = outputStream;
        dataInputStream = new DataInputStream(inputStream);
        ShutdownManager.registerStutdown(this);
    }

    /** Creates a new instance of GPSInput */
    public SerialComm(CommPortIdentifier portId) throws IOException {
        try {

// listening
            this.serialPort = (SerialPort) portId.open("SerialComm", 2000);

            Thread.sleep(5000);

//            serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);

//            is = serialPort.getInputStream();
            is = SerialLog.getLoggedInputStream(serialPort.getInputStream(), SerialLog.createLogFile("arduino-in"));
            dataInputStream = new DataInputStream(is);


//            os = serialPort.getOutputStream();
            os = SerialLog.getLoggedOutputStream(serialPort.getOutputStream(), SerialLog.createLogFile("arduino-out"));

            System.out.println(this + ": " + portId.getName() + " opened");

// end listening part

//        generateSignal();
            ShutdownManager.registerStutdown(this);
//            getLogger().log(Level.INFO, "listening on " + serialPort.getName());
        } catch (Exception ex) {
            throw new IOException("Opening error", ex);
        }
    }

    public OutputStream getOutputStream() {
        return os;
    }

//    private ArduinoSerial(InputStream is, OutputStream os) {
//        this.is = is;
//        this.os = os;
//serial.getInputStream(), serial.getOutputStream()
////        this.reader = new BufferedReader(new InputStreamReader(is));
//    }
//    String readLine() throws IOException {
//        String readLine = reader.readLine();
//        System.out.println("Arduino: "+readLine);
//        return readLine;
//    }
//    void write(int b) throws IOException {
//        os.write(b);
//        os.flush();
//    }
//    private ArduinoSerial(SerialPort serial) {
//         this.os = serial.getOutputStream();
//        this.is = serial.getInputStream();
//
//    }
    public static SerialComm openSerialComm(String portName) throws PortInUseException, UnsupportedCommOperationException, IOException {
        CommPortIdentifier portId = Ports.getPortIdentifier(portName);
        if (portId == null) {
            throw new IllegalArgumentException("Port not found: " + portName);
        }


//        serial.setSerialPortParams(9600, SerialPort.DATABITS_8,
//                SerialPort.STOPBITS_2,
//                SerialPort.PARITY_NONE);

        return new SerialComm(portId);
//        System.out.println("ArduinoSerial Init");

//        int ver = i2c.readVersion();
//        System.out.println("USB-I2C Version: " + ver);
//        if (ver > 0) {
//            System.out.println(i2c.is);
//            System.out.println(i2c.os);
//
//            return i2c;
//        } else {
//            throw new IOException(i2c+" does not respond!");
//        }

    }

//    public static void main(String[] args) throws PortInUseException, UnsupportedCommOperationException, IOException {
//        System.out.println("USAGE: java I2CUSB i2cusbport");
//        //I2CUSB i2c = I2CUSB.getI2C("COM4");
//        I2CUSB i2c = I2CUSB.getI2C(args[0]);
//    }
//    private void openPort(CommPortIdentifier portId) {
//    }
    @Override
    public void shutdown() {
//        getLogger().log(Level.INFO, "closing");
        try {
            is.close();
        } catch (IOException ex) {
            Logger.getLogger(SerialComm.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (serialPort != null) {
            serialPort.close();
        }
//        if (!activeLog.isEmpty()) {
//            GPXIO.showSaveTrackDialog(activeLog, null);
//        }
//        getLogger().log(Level.INFO, "closed");
    }
//    public void run() {
//        try {
//            Thread.sleep(20000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//    public void run() {
////        Thread.currentThread().setDaemon(true);
//        while (true) {
//            try {
//                gps.parseNMEA(reader.readLine());
//
//                Thread.sleep(10);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(FileGPSInput.class.getName()).log(Level.SEVERE, null, ex);
//
//            } catch (IOException ex) {
//                Logger.getLogger(FileGPSInput.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }
    private StringBuilder serialBuffer = new StringBuilder();

    public void serialEvent(SerialPortEvent event) {

//        System.out.println("event");
        switch (event.getEventType()) {
            case SerialPortEvent.BI:
            case SerialPortEvent.OE:
            case SerialPortEvent.FE:
            case SerialPortEvent.PE:
            case SerialPortEvent.CD:
            case SerialPortEvent.CTS:
            case SerialPortEvent.DSR:
            case SerialPortEvent.RI:
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;
            case SerialPortEvent.DATA_AVAILABLE:
                dataAvailable();
                break;
        }
    }

    /**
     * Blocks until short comes.
     * @return
     * @throws IOException
     */
    public short readShort() throws IOException {
        while (true) {
            if (is.available() > 1) { // at least two bytes
                return dataInputStream.readShort();
            }
//            System.out.println("waiting"+is.available());
            waitForDataAvailable(100);
        }
    }

    /**
     * Blocks until short comes.
     * @return
     * @throws IOException
     */
    public byte readByte() throws IOException {
        while (true) {
            if (is.available() > 0) { // at least two bytes
                return dataInputStream.readByte();
            }
//            System.out.println("waiting"+is.available());
            waitForDataAvailable(100);
        }
    }

    private void dataAvailable() {
        synchronized (this) {
            this.notifyAll();
        }
//        try {
//            while (is.available() > 1) { // at least two bytes
//                if (remains == 0) {
//                    // read Length
//                    remains = dataInputStream.readShort();
//                    shorts = new ArrayList<Short>();
//                } else {
//                    short readShort = dataInputStream.readShort();
//                    shorts.add(readShort);
//                    remains--;
////                    serialBuffer.append(c);
//                    if (remains == 0) {
//                        // flush and start new sentence
//                        lineRead(serialBuffer.toString());
//                        shortsRead(shorts);
//                        shorts = null;
//                    }
//                }
//            }
//            while (inputStream.available() > 0) { 
//                char c = (char) inputStream.read();
//                if (c == '\n') {
//                    // flush and start new sentence
//                    lineRead(serialBuffer.toString());
//                    serialBuffer = new StringBuilder();
//                } else {
//                    serialBuffer.append(c);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public void waitForDataAvailable(int timeout) {
        synchronized (this) {
            try {
                this.wait(timeout);
            } catch (InterruptedException ex) {
                Logger.getLogger(SerialComm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void lineRead(String line) {
        System.out.println("Arduino: " + line);
    }

//    private void shortsRead(List<Short> shorts) {
////        for (Short short1 : shorts) {
//        double[] scan = new double[shorts.size()];
//        for (int i = 0; i < scan.length; i++) {
//
//            //  cm = (2914/(val+5)) - 1;
//            //    cm =  (6787 / (val - 3)) - 4; // magic formula
//            scan[i] = ((6787.0 / (shorts.get(i) - 3)) - 4) / 100.0; // magic formula
//            System.out.print(scan[i] + " ");
//        }
//        System.out.println();
//        sharpReader.vals = scan;
//    }
    @Override
    public String toString() {
        return "ArduinoSerial";
    }

    public DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    

}
