package eurobot.kuba;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import robotour.arduino.SerialComm;

/**
 *
 * @author Kotuc
 */
public class KubaOutProtocol {

    private enum Command {
        PING(0, 1),
        GET_INFO(1, 1),
        CHANGE_ADDR(2, -1),
        CHANGE_BAUD(3, -1),
        RESET(4, 1),
        DRIVE_LR(5, 5),
        ENABLE(6, 2);

        private Command(int cmd, int length) {
            this.id = (byte)cmd;
            this.length = (byte)length;
        }

        final byte id;
        final byte length;

        static Command parse(byte cmdid) {
            return values()[cmdid];
        }

    }


//    static final byte CMD_PING = 0;
//    static final byte CMD_GET_INFO = 1;
//    static final byte CMD_CHANGE_ADDR = 2;
//    static final byte CMD_CHANGE_BAUD = 3;
//    static final byte CMD_RESET = 4;
//    static final byte CMD_DRIVE_LR = 5;
//    static final byte CMD_ENABLE = 6;
    
    static final byte ADDR_DRIVER = 1;


    private final SerialComm serial;
    final DataOutputStream dataOutStream;

    public KubaOutProtocol(SerialComm serial) {
        this.dataOutStream = new DataOutputStream(serial.getOutputStream());
//        this.dataOutStream = new DataOutputStream(new BufferedOutputStream(serial.getOutputStream()));
        this.serial = serial;
    }

    public void sendMessage(byte[] bytes) {
        try {
            System.out.println("Quorra command: " + Arrays.toString(bytes));
            if (bytes[2] == bytes.length + 3) {
                throw new IOException("Lengths does not match " + bytes[2] + "!=" + bytes.length + "+3");
            }
            dataOutStream.write(bytes);
//        daaOutStream.write(bytes);
//        for (int i = 0; i < bytes.length; i++) {
//            byte b = bytes[i];
//            dataOutStream.write(b);
//        }
            dataOutStream.flush();
        } catch (IOException ex) {
            ioex(ex);
        }
    }

    public synchronized void sendMessage() {
        sendMessage(byteArrayOutputStream.toByteArray());
    }
    volatile ByteArrayOutputStream byteArrayOutputStream;

    private synchronized DataOutputStream createNewMessage(byte address, Command cmd) {
        return createNewMessage(address, cmd.length, cmd.id);
    }

    private synchronized DataOutputStream createNewMessage(byte address, int length, byte command) {
        this.byteArrayOutputStream = new ByteArrayOutputStream(length + 3);

        DataOutputStream data = new DataOutputStream(byteArrayOutputStream);
        try {
            data.writeByte((byte) 0xFF);
            data.writeByte(address);
            data.writeByte(length);
            data.writeByte(command);
        } catch (IOException ex) {
            ioex(ex);
        }
        return data;
    }

    public void ping() {

//            DataOutputStream data =
        System.out.println("ping: ");
        createNewMessage(ADDR_DRIVER, Command.PING);
        sendMessage();
//            protocol.sendMessage(new byte[]{CMD_DRIVE_LR, (byte) left, (byte) right});

    }

    public void getInfo() {
//            DataOutputStream data =
        System.out.println("Get info: ");
        createNewMessage(ADDR_DRIVER, Command.GET_INFO);
        sendMessage();
//            protocol.sendMessage(new byte[]{CMD_DRIVE_LR, (byte) left, (byte) right});

    }

    public void setSpeediLR(int left, int right) {
        try {
            System.out.println("Set speed int left: " + left + " right: " + right);
            DataOutputStream data = createNewMessage(ADDR_DRIVER, Command.DRIVE_LR);
            data.writeShort(left);
            data.writeShort(right);
            sendMessage();
//            protocol.sendMessage(new byte[]{CMD_DRIVE_LR, (byte) left, (byte) right});
        } catch (IOException ex) {
            ioex(ex);
        }
    }

    public void setEnabled(boolean enabled) {
        try {
            System.out.println("Set enabled: " + enabled);
            DataOutputStream data = createNewMessage(ADDR_DRIVER, Command.ENABLE);
            data.writeByte((enabled) ? 1 : 0);
            sendMessage();
//            protocol.sendMessage(new byte[]{CMD_DRIVE_LR, (byte) left, (byte) right});
        } catch (IOException ex) {
            ioex(ex);
        }
    }

    void ioex(IOException ex) {
        ex.printStackTrace();
    }


}
