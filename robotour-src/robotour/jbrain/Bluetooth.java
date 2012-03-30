package robotour.jbrain;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.util.ShutdownManager;
import robotour.util.Shutdownable;

/**
 *
 * @author Kotuc
 */
public class Bluetooth implements Shutdownable {

    private final BufferedOutputStream os;
    private final BufferedInputStream is;
    /**
     * Changes to true when no more writing is allowed during program shutdown.
     * Flag means sending commands is vorbiden.
     */
    private final AtomicBoolean off = new AtomicBoolean(false);
    private final SerialPort btport;

    private Bluetooth(SerialPort port) throws IOException {
        this.btport = port;
        this.os = new BufferedOutputStream(port.getOutputStream());
        this.is = new BufferedInputStream(port.getInputStream());
        ShutdownManager.registerStutdown(this);
    }

    public static Bluetooth getBluetooth(String port) throws PortInUseException, IOException, UnsupportedCommOperationException, NoSuchPortException {
//        CommPortIdentifier portId = Ports.getPortIdentifier(port);
        CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(port);
        if (portId == null) {
            throw new NullPointerException("port");
        }
        SerialPort serial = (SerialPort) portId.open("Bluetooth Robotour", 2000);

//        serial.setSerialPortParams(115200, SerialPort.DATABITS_8,
        serial.setSerialPortParams(9600,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);
        Bluetooth bt = new Bluetooth(serial);
//        System.out.println("SSC32 Version: "+ssc.readVersion());
        return bt;
    }


    @Override
    public synchronized void shutdown() {
        this.close();
    }

    public synchronized void close() {
//        stop()        !
//        setBank(0, 127);
//        setBank(1, 127);
        off.set(true);
        getLogger().log(Level.INFO, "closing os");
        try {
            if (os != null) {
                os.close();
                //os = null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        getLogger().log(Level.INFO, "closing is");
        try {
            if (is != null) {
                is.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        getLogger().log(Level.INFO, "closed");
    }

    private Logger getLogger() {
        return Logger.getLogger(this.toString());
    }

    @Override
    public String toString() {
        return "JBrain Bluetoorh (" + btport + ")";
    }
}
