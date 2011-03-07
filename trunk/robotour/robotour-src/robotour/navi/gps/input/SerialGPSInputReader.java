package robotour.navi.gps.input;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.util.ShutdownManager;
import robotour.util.Shutdownable;

/**
 *
 * @author Kotuc
 */
class SerialGPSInputReader extends GPSInput implements Runnable, Shutdownable {

    private final InputStream inputStream;
    private final SerialPort serialPort;
    private final GPSInput gps = this;
    private BufferedReader reader;

    /** Creates a new instance of GPSInput */
    SerialGPSInputReader(CommPortIdentifier portId) throws IOException {
        try {
            //    super("GPS Input");
//        setDeviceName("GPS Input");
//        devicePanel = new GPSPanel(this);
            this.serialPort = (SerialPort) portId.open("RobotourGPS", 2000);
            inputStream = serialPort.getInputStream();
            System.out.println(this + ": " + portId.getName() + " opened");
//            setStateInd(STATE_ON);
// direct reading (polling)

            reader = new BufferedReader(new InputStreamReader(inputStream));
            new Thread(this).start();
            //
// end direct reading
// listening
//            serialPort.addEventListener(this);
//            serialPort.notifyOnDataAvailable(true);
//            serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

// end listening part

//        generateSignal();
            ShutdownManager.registerStutdown(this);
//            getLogger().log(Level.INFO, "listening on " + serialPort.getName());
        } catch (Exception ex) {
            throw new IOException("Opening error", ex);
        }
    }

//    private void openPort(CommPortIdentifier portId) {
//    }
    @Override
    public void shutdown() {
        getLogger().log(Level.INFO, "closing");
        try {
            inputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(SerialGPSInputReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        serialPort.close();
//        if (!activeLog.isEmpty()) {
//            GPXIO.showSaveTrackDialog(activeLog, null);
//        }
        getLogger().log(Level.INFO, "closed");
    }

//    public void run() {
//        try {
//            Thread.sleep(20000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
    public void run() {
//        Thread.currentThread().setDaemon(true);
        while (true) {
            try {
                gps.parseNMEA(reader.readLine());

                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(FileGPSInput.class.getName()).log(Level.SEVERE, null, ex);

            } catch (IOException ex) {
                Logger.getLogger(FileGPSInput.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
//    private StringBuffer serialBuffer = new StringBuffer();
//
//    public void serialEvent(SerialPortEvent event) {
//
//        System.out.println("event");
//        switch (event.getEventType()) {
//            case SerialPortEvent.BI:
//            case SerialPortEvent.OE:
//            case SerialPortEvent.FE:
//            case SerialPortEvent.PE:
//            case SerialPortEvent.CD:
//            case SerialPortEvent.CTS:
//            case SerialPortEvent.DSR:
//            case SerialPortEvent.RI:
//            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
//                break;
//            case SerialPortEvent.DATA_AVAILABLE:
//                //	    byte[] readBuffer = new byte[20];
//                try {
//                    //		while (inputStream.available() > 0) {
////		    int numBytes = inputStream.read(readBuffer);
////		}
////              System.out.print(new String(readBuffer));
//                    for (int i = 0; inputStream.available() > 0; i++) {
//                        char c = (char) inputStream.read();
//                        if (c == '\n') {
//                            gps.parseNMEA(serialBuffer.toString());
//                            serialBuffer = new StringBuffer();
//                        } else {
//                            serialBuffer.append(c);
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//        }
//    }
}
