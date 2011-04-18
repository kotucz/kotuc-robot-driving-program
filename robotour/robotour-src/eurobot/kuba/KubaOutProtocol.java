package eurobot.kuba;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import robotour.arduino.SerialComm;

/**
 *
 * @author Kotuc
 * @see http://www.tcpipguide.com/free/t_SerialLineInternetProtocolSLIP-2.htm
 */
public class KubaOutProtocol {

    private final SerialComm serial;
    
    final DataOutputStream dataOutStream;    

    public KubaOutProtocol(SerialComm serial) {
        this.dataOutStream = new DataOutputStream(serial.getOutputStream());
//        this.dataOutStream = new DataOutputStream(new BufferedOutputStream(serial.getOutputStream()));
        this.serial = serial;                
    }

    public void sendMessage(byte[] bytes) throws IOException {
        System.out.println("Quorra command: "+Arrays.toString(bytes));
        if (bytes[2] == bytes.length+3) {
            throw new IOException("Lengths does not match "+bytes[2]+"!="+bytes.length+"+3");
        }
        dataOutStream.write(bytes);
//        daaOutStream.write(bytes);
//        for (int i = 0; i < bytes.length; i++) {
//            byte b = bytes[i];
//            dataOutStream.write(b);
//        }
        dataOutStream.flush();
    }

    public synchronized void sendMessage() throws IOException {
        sendMessage(byteArrayOutputStream.toByteArray());
    }

    volatile ByteArrayOutputStream byteArrayOutputStream;

    public synchronized DataOutputStream createNewMessage(byte address, int length, byte command) throws IOException {
        this.byteArrayOutputStream = new ByteArrayOutputStream(length + 3);
        
        DataOutputStream data = new DataOutputStream(byteArrayOutputStream);
        data.writeByte((byte)0xFF);
        data.writeByte(address);
        data.writeByte(length);
        data.writeByte(command);
        return data;
    }

}
