package robotour.hardware.arduino;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.util.Binary;

/**
 *
 * @author Kotuc
 * @see http://www.tcpipguide.com/free/t_SerialLineInternetProtocolSLIP-2.htm
 */
public class SLIP {

    protected static final byte SLIP_END = (byte) 0xC0;
    protected static final byte SLIP_ESC = (byte) 0xDB;
    protected static final byte SLIP_ESC_END = (byte) 0xDC;
    protected static final byte SLIP_ESC_ESC = (byte) 0xDD;
    private final ArduinoSerial serial;
    
    final SLIPOutputStream slipos;
    final DataOutputStream sliposdata;
    final SLIPInput input;
    final MessageReceived messager;

    public SLIP(ArduinoSerial serial, MessageReceived messager) {
        this.slipos = new SLIPOutputStream(serial.getOutputStream());
        this.sliposdata = new DataOutputStream(slipos);
        this.input = new SLIPInput();
        this.serial = serial;

        this.messager = messager;

        listen();
    }

    void listen() {
        new Thread(input).start();
    }

    void received(List<Byte> buffer) {
        messager.messageRecieved(Binary.toArray(buffer));
    }

    class SLIPInput implements Runnable {

        private byte chsum = 0;
        private boolean prevEsc = false;
        private List<Byte> buffer = new ArrayList<Byte>();

        void receivedByte(byte b) {
            switch (b) {
                case SLIP_END:
                    prevEsc = false;
                    // verify and perform
                    if (0 == chsum) {
                        if (buffer.size() > 0) {
                            received(buffer);
                        } else {
//                        System.err.println("empty message ");
                        }
                    } else {
                        System.err.println("wrong checksum " + chsum + ": " + new String(Binary.toArray(buffer)));
                    }
                    // reset
                    chsum = 0;
                    buffer.clear();
                    break;
                case SLIP_ESC:
                    // replace next
                    prevEsc = true;
                    break;
                default:
                    if (prevEsc) {
                        prevEsc = false;
                        if (b == SLIP_ESC_END) {
                            b = SLIP_END;
                        } else if (b == SLIP_ESC_ESC) {
                            b = SLIP_ESC;
                        } else {
                            // error
                        }
                    }
                    // count data
                    buffer.add((byte) b);
                    chsum ^= b;
            }
        }

        public void run() {
            while (true) {
                try {
                    receivedByte(serial.readByte());
                } catch (IOException ex) {
                    Logger.getLogger(SLIP.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    void sendMessage(byte[] bytes) throws IOException {
        slipos.sendMessage(bytes);
    }
}
