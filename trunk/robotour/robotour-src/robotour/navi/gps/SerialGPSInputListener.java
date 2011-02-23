package robotour.navi.gps;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.util.ShutdownManager;
import robotour.util.Shutdownable;

/**
 * User SerialEvent to read port when data are ready.
 * @author Kotuc
 */
class SerialGPSInputListener extends GPSInput implements SerialPortEventListener, Shutdownable {

    private final InputStream inputStream;
    private final SerialPort serialPort;
    private final GPSInput gps = this;
//    private BufferedReader reader;

    /** Creates a new instance of GPSInput */
    SerialGPSInputListener(CommPortIdentifier portId) throws IOException {
        try {

// listening
            this.serialPort = (SerialPort) portId.open("RobotourGPS", 12000);
            
            serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);

            
            inputStream = serialPort.getInputStream();
            System.out.println(this + ": " + portId.getName() + " opened");

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
            Logger.getLogger(SerialGPSInputListener.class.getName()).log(Level.SEVERE, null, ex);
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

        System.out.println("event");
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
                //	    byte[] readBuffer = new byte[20];
                try {
                    //		while (inputStream.available() > 0) {
//		    int numBytes = inputStream.read(readBuffer);
//		}
//              System.out.print(new String(readBuffer));
//                    for (int i = 0; inputStream.available() > 0; i++) {
                    while (inputStream.available() > 0) {
                        char c = (char) inputStream.read();
                        if (c == '\n') {
                            // flush and start new sentence
                            gps.parseNMEA(serialBuffer.toString());
                            serialBuffer = new StringBuilder();
                        } else {
                            serialBuffer.append(c);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
