package robotour.hardware;

import gnu.io.NoSuchPortException;
import robotour.util.Shutdownable;
import robotour.util.ShutdownManager;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.rt.RealTime;

/**
 *
 * Alternative command for opening port in windows:
 * File serial = new File("com1:115200,n,8,1");
 *
 * @author Kotuc
 */
public class SSC32 implements Shutdownable {

    private final DataOutputStream os;
    private final DataInputStream is;
    /**
     * Changes to true when no more writing is allowed during program shutdown.
     * Flag means sending commands is vorbiden.
     */
    private final AtomicBoolean off = new AtomicBoolean(false);
    private final SerialPort port;

    private SSC32(SerialPort port) throws IOException {
        this.port = port;
        this.os = new DataOutputStream(port.getOutputStream());
        this.is = new DataInputStream(port.getInputStream());
        ShutdownManager.registerStutdown(this);
    }

    public static SSC32 getSSC32(String port) throws PortInUseException, IOException, UnsupportedCommOperationException, NoSuchPortException {
//        CommPortIdentifier portId = Ports.getPortIdentifier(port);
        CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(port);
        if (portId == null) {
            throw new IOException("SSC32 No such portId: " + port);
        }
        SerialPort serial = (SerialPort) portId.open("SSC32 Robotour", 2000);

//        serial.setSerialPortParams(115200, SerialPort.DATABITS_8,
        serial.setSerialPortParams(9600,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);
        SSC32 ssc = new SSC32(serial);
//        System.out.println("SSC32 Version: "+ssc.readVersion());
        return ssc;
    }

    /**
     * Opens port from file 
     * @param port
     * @return
     */
    private static SSC32 getSSC32onFile(String port) throws FileNotFoundException {
        throw new UnsupportedOperationException();
//        return new SSC32(
//                new DataOutputStream(new FileOutputStream(new File(port))),
//                new DataInputStream(new FileInputStream(new File(port))));

    }

    /**
     * Sets pins on bank to byt bite values.
     * @param bank
     * @param byt
     * @see SSC doc
     */
    public synchronized void setBank(int bank, int byt) {
        if (bank < 0 || 3 < bank) {
            throw new IllegalArgumentException("Illegal bank id: " + bank);
        }
        sendCommand("#" + bank + ":" + byt);
    }

    /**
     * Sets pin to logicLevel.
     * @param pin
     * @param logicLevel
     * @see SSC doc
     */
    public synchronized void setPin(int pin, boolean logicLevel) {
        if (pin < 0 || 31 < pin) {
            throw new IllegalArgumentException("Illegal pin id: " + pin);
        }
        sendCommand("#" + pin + ((logicLevel) ? "H" : "L"));
    }

    public enum InputPin {

        A, B, C, D;
    }

    public enum InputMode {

        DIGITAL, DIGITAL_LOW, ANALOGUE;
    }

    public synchronized String readVersion() throws IOException {
        sendCommand("VER");
        int c = is.read();
        if (c==-1) {
            throw new IOException("SSC OFF");
        }
        String version = ""+(char)c;
        while ((c = is.read()) != -1) {
//            System.out.println("char " + c);
            version += (char) c;
        }
        return version;
//        return is.readUTF();
    }

    /**
     *
     * @param input input to be read
     * @return boolean value of input
     * @throws java.io.IOException
     */
    public synchronized boolean readDigitalInput(InputPin input) throws IOException {
        if (input == null) {
            throw new NullPointerException("input pin");
        }
        sendCommand(input.toString());
        char res = (char) is.read();
        switch (res) {
            case '0':
                return false;
            case '1':
                return true;
            default:
                throw new IllegalArgumentException("Unexpected read value: \"" + res + "\" . Expected 0 or 1 ");
        }
    }
    EnumMap<InputPin, Switch> inputs = new EnumMap<InputPin, Switch>(InputPin.class);

    public void readAllInputs() throws IOException {

        String readCommand = "B";

        sendCommand(readCommand);

        //        String res = is.readUTF();
        byte[] res = new byte[1];
        is.read(res);

        for (int i = 0; i < res.length; i++) {
            switch (res[i]) {
                case '0':
                    inputs.get(InputPin.B).setState(Switch.State.PRESSED);//setfalse;
                    System.out.println("B PRESSED");
                    break;
                case '1':
                    inputs.get(InputPin.B).setState(Switch.State.RELEASED);
                    System.out.println("B RELEASED");
                    //settrue;
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected read value: \"" + res + "\" . Expected 0 or 1 ");
            }
        }

//        for (InputPin inputPin : inputs.keySet()) {
//            command += " " + inputPin.toString() + "L";
//        }
//
//        for (InputPin inputPin : inputs.keySet()) {
//            command += " " + inputPin.toString() + "L";
//        }

//        for (Switch swtch : inputs.values()) {
//            command +=
//        }
    }

    public Switch getSwitch(InputPin input) {
        if (inputs.containsKey(input)) {
            throw new IllegalArgumentException("Already registered: " + input);
        }
        Switch swit = new Switch(Switch.State.PRESSED);
        inputs.put(input, swit);
        RealTime.newThread(new InputsBuffer(), 20).start();
        return swit;
    }

    class InputsBuffer implements Runnable {

        public void run() {
            try {
                while (true) {
                    //readAllInputs();
                    inputs.get(InputPin.B).setState(
                            readDigitalInput(InputPin.B) ? Switch.State.RELEASED : Switch.State.PRESSED);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SSC32.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(SSC32.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private Logger getLogger() {
        return Logger.getLogger("SSC-32");
    }

    /**
     * Send raw command to assigned SSC.
     * @param command
     */
    public synchronized void sendCommand(String command) {
        System.out.println("SSC: " + command);

        try {
            if (off.get()) {
                throw new IOException("ssc already closed");
            }
            os.writeUTF(command);
            os.write(13);
            os.flush();
            if (off.get()) {
                throw new AssertionError("ssc off cannot go there");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }

    }
    private List<Shutdownable> shutdownableList = new LinkedList<Shutdownable>();

    /**
     * Register dependet devices. This lets them to turn off devices driven by SSC32.
     * For example set motor speeds to zero or servos to initial position.
     * @param shutdownable
     */
    public void registerShutdownable(Shutdownable shutdownable) {
        shutdownableList.add(shutdownable);
    }

    @Override
    public synchronized void shutdown() {
        for (Shutdownable shutdownable : shutdownableList) {
            shutdownable.shutdown();
            System.out.println(shutdownable + "\t[OFF]");
        }
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

    public synchronized void setServo(int chn, int pos) {
        if (chn < 0 || 32 <= chn) {
            throw new IllegalArgumentException("servo channel: " + chn);
        }
        pos = Math.min(2250, Math.max(750, pos));
        sendCommand("# " + chn + " P" + pos);
    }

    public class SSCServo implements robotour.iface.Servo {

        private final int channel;
        private int center = 1500;
        private int amplitude = 500; // normal
//        private int amplitude = 750; // extended
        private Integer speedLimit = null;

        private SSCServo(int channel) {
            if (channel < 0 || 32 <= channel) {
                throw new IllegalArgumentException("servo channel: " + channel);
            }
            this.channel = channel;
            setPos(center);
//            setAmplitude(1000);
//            setSpeedLimit(1000);
        }

        public void setCenter(int center) {
            this.center = center;
        }



        public void setPosition(double position) {
            int pos = (int) Math.round(center + (position * amplitude));
            setPos(pos);
        }

        public void setPositionMillis(double position, int millis) {
            int pos = (int) Math.round(center + (position * amplitude));
            goToPosInTime(pos, millis);
        }

        /**
         *
         * @param amplitude max amplitude of servo (setPosition((-)1))
         */
        public void setAmplitude(int amplitude) {
            this.amplitude = amplitude;
        }

        /**
         *
         * @param speedLimit max servo speed limit int us of signal per second
         */
        public void setSpeedLimit(int speedLimit) {
            this.speedLimit = speedLimit;
        }

        void setPos(int pos) {
            pos = Math.max(center - amplitude, Math.min(pos, center + amplitude));
            String command = "#" + channel + " P" + pos;
            if (speedLimit != null) {
                command += " S" + speedLimit;
            }
            sendCommand(command);
        }

        public void goToPosInTime(int pos, int time) {
            pos = Math.max(center - amplitude, Math.min(pos, center + amplitude));
            String command = "#" + channel + " P" + pos;
            command += " T" + time;
            sendCommand(command);
        }

    }

    public SSCServo getServo(int channel) {
        return new SSCServo(channel);
    }

    @Override
    public String toString() {
        return "SSC32(" + port + ")";
    }
}
