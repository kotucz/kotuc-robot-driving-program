package eurobot.kuba;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        ENABLE(6, 2),
        SET_SERVO(7, 4),
        READ_INT(8, 1),
        READ_COLOR(5, 1),
        READ_ENCODER(5, 1),;

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

    KubaInputReader in;

//    static final byte CMD_PING = 0;
//    static final byte CMD_GET_INFO = 1;
//    static final byte CMD_CHANGE_ADDR = 2;
//    static final byte CMD_CHANGE_BAUD = 3;
//    static final byte CMD_RESET = 4;
//    static final byte CMD_DRIVE_LR = 5;
//    static final byte CMD_ENABLE = 6;
    
    static final byte ADDR_DRIVER = 0;

    static final byte ADDR_PWR_MANAGER = 1;

    static final byte ADDR_COLOR_LEFT = 2;
    static final byte ADDR_COLOR_RIGHT = 3;

    static final byte ADDR_ENCODER_LEFT = 4;
    static final byte ADDR_ENCODER_RIGHT = 5;

    static final byte ADDR_ENCODER_WLEFT = 6;
    static final byte ADDR_ENCODER_WRIGHT = 7;

    static final byte ADDR_BUTTONS = 8;

    private final SerialComm serial;
    final DataOutputStream dataOutStream;

    public KubaOutProtocol(SerialComm serial) {
        this.dataOutStream = new DataOutputStream(serial.getOutputStream());
        in = new KubaInputReader(serial.getDataInputStream());
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

            try {
                Thread.sleep(2);
            } catch (InterruptedException ex) {
                Logger.getLogger(KubaOutProtocol.class.getName()).log(Level.SEVERE, null, ex);
            }

            in.readMessage();

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

    /**
     * pos -8000 .. 8000
     */
    public void setServo(byte id, short pos) {
        try {
            System.out.println("Set servo id: " + id + " pos: " + pos);
            DataOutputStream data = createNewMessage(ADDR_DRIVER, Command.SET_SERVO);
            data.writeByte(id);
            data.writeShort(pos);
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

    public void readOdometry() {
        readEncoders();
//        readEncoder(ADDR_ENCODER_LEFT);
//        readEncoder(ADDR_ENCODER_RIGHT);
        in.updateOdometry();
    }


    public void readEncoders() {
        System.out.println("Read encoders: ");
        DataOutputStream data = createNewMessage(ADDR_DRIVER, Command.READ_INT);
        sendMessage();
//            protocol.sendMessage(new byte[]{CMD_DRIVE_LR, (byte) left, (byte) right});
    }
//
//    public void readEncoder(byte address) {
//        System.out.println("Read encoder: " + address);
//        DataOutputStream data = createNewMessage(address, Command.READ_ENCODER);
//        sendMessage();
////            protocol.sendMessage(new byte[]{CMD_DRIVE_LR, (byte) left, (byte) right});
//    }

    void ioex(IOException ex) {
        System.out.println(""+ex.getMessage());
//        ex.printStackTrace();
    }


}
