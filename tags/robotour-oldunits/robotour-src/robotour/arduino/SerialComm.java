package robotour.arduino;

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
    private static final int DEFAULT_BAUD = 9600;

    public SerialComm(InputStream inputStream, OutputStream outputStream) {
        this.is = inputStream;
        this.os = outputStream;
        dataInputStream = new DataInputStream(inputStream);
        ShutdownManager.registerStutdown(this);
    }

//    SerialComm(CommPortIdentifier portId) throws IOException {
//        this(portId, 9600);
//    }

    /** Creates a new instance of GPSInput */
    SerialComm(CommPortIdentifier portId, int speed) throws IOException {
        try {

// listening
            this.serialPort = (SerialPort) portId.open("SerialComm", 2000);

            Thread.sleep(5000);

//            serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
//            serialPort.setSerialPortParams(38400, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
//            serialPort.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            serialPort.setSerialPortParams(speed, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);

//            is = serialPort.getInputStream();
            is = SerialLog.getLoggedInputStream(serialPort.getInputStream(), SerialLog.createLogFile("kuba-in"));
            dataInputStream = new DataInputStream(is);


//            os = serialPort.getOutputStream();
            os = SerialLog.getLoggedOutputStream(serialPort.getOutputStream(), SerialLog.createLogFile("kuba-out"));

            System.out.println(this + ": " + portId.getName() + " opened "+speed+" baud");

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

    public static SerialComm openSerialComm(String portName) throws PortInUseException, UnsupportedCommOperationException, IOException {
        return openSerialComm(portName, DEFAULT_BAUD);
    }

    public static SerialComm openSerialComm(String portName, int speed) throws PortInUseException, UnsupportedCommOperationException, IOException {
        CommPortIdentifier portId = Ports.getPortIdentifier(portName);

        if (portId == null) {
            throw new IllegalArgumentException("Port not found: " + portName);
        }

        return new SerialComm(portId, speed);
    }

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
    }

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
    }

    void waitForDataAvailable(int timeout) {
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

    @Override
    public String toString() {
        return "SerialComm";
    }

    public DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    

}
