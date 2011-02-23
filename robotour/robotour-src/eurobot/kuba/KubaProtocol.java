package eurobot.kuba;

import robotour.hardware.arduino.*;
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
public class KubaProtocol {

    private final SerialComm serial;
    
    final DataOutputStream sliposdata;
    final KubaInput input;
    final MessageReceived messager;

    public KubaProtocol(SerialComm serial, MessageReceived messager) {
        this.sliposdata = new DataOutputStream(serial.getOutputStream());
        this.input = new KubaInput();
        this.serial = serial;

        this.messager = messager;

        listen();
    }

    private void listen() {
        new Thread(input).start();
    }

    void received(List<Byte> buffer) {
        messager.messageRecieved(Binary.toArray(buffer));
    }

    class KubaInput implements Runnable {
               
        public void run() {
            while (true) {
                try {
                    byte readByte = serial.readByte();
                } catch (IOException ex) {
                    Logger.getLogger(KubaProtocol.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    void sendMessage(byte[] bytes) throws IOException {
        sliposdata.write(bytes);
        sliposdata.flush();
    }
    
}
