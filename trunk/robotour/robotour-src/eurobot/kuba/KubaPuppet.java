package eurobot.kuba;

import eurobot.kuba.remote.Server;
import robot.navi.diff.DiffOdometry;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.arduino.SerialComm;
import robotour.iface.DiffWheels;
import robotour.navi.basic.RobotPose;

/**
 *
 * @author Kotuc
 */
public class KubaPuppet {

    final DiffWheels diffWheels;
    final DiffOdometry diffOdometry;
//    private final ArduinoSerial serial;
    final KubaOutProtocol protocol;
    
    public static final byte CMD_PING = 0;
    public static final byte CMD_GET_INFO = 1;
    public static final byte CMD_CHANGE_ADDR = 2;
    public static final byte CMD_RESET = 3;
    public static final byte CMD_DRIVE_LR = 4;
    public static final byte CMD_ENABLE = 5;
    
//    public static final byte CMD_DRIVE_FT = 102;
//    public static final byte CMD_DRIVE_SET_SPEED = 105;
//    public static final byte CMD_DRIVE_SET_AZIMUTH = 107;
//    public static final byte CMD_CMPS_CALIBRATE = 97;

    public static final byte ADDR_DRIVER = 0x42;

//    private final EventListener eventlog;
    public KubaPuppet(KubaOutProtocol kprotocol/*, EventListener eventlistener*/) {
        this.protocol = kprotocol;
//        this.eventlog = eventlistener;
//        this.serial = serial;
        this.diffOdometry = new DiffOdometry();

        this.diffWheels = new DiffWheels() {

            public void setSpeedsLR(double leftSpeed, double rightSpeed) {
                double left = checkRange(leftSpeed, "leftSpeed");
                double right = checkRange(rightSpeed, "rightSpeed");
                int ileft = (int) (left * 90);
                int iright = (int) (right * 90);
                long currentTimeMillis = System.currentTimeMillis();
                setSpeediLR(ileft, iright);
//                eventlog.eventRecieved(new WheelCommandEvent(left, right, currentTimeMillis));
            }

            public void stop() {
                stopMotors();
            }
        };

    }


//    public void goForward(double distm) {
////        slip.sendMessage(new int[] {CMD_GO_CM});
//    }
//
//    public void setSpeedLR(double mpers) {
//        slip.sendMessage(new int[]{CMD_DRIVE_FT,});
//    }
    public void stopMotors() {
//        try {
        setSpeediLR(0, 0);
//            protocol.sendMessage(new byte[]{CMD_STOP});
//        } catch (IOException ex) {
//            Logger.getLogger(KubaPuppet.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public void getInfo() {
        try {
//            DataOutputStream data =
            System.out.println("Get info: ");
            protocol.createNewMessage(ADDR_DRIVER, 1, CMD_GET_INFO);
            protocol.sendMessage();
//            protocol.sendMessage(new byte[]{CMD_DRIVE_LR, (byte) left, (byte) right});
        } catch (IOException ex) {
            Logger.getLogger(KubaPuppet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setSpeediLR(int left, int right) {
        try {
            System.out.println("Set speed int left: " + left + " right: " + right);
            DataOutputStream data = protocol.createNewMessage(ADDR_DRIVER, 5, CMD_DRIVE_LR);
            data.writeShort(left);
            data.writeShort(right);
            protocol.sendMessage();
//            protocol.sendMessage(new byte[]{CMD_DRIVE_LR, (byte) left, (byte) right});
        } catch (IOException ex) {
            Logger.getLogger(KubaPuppet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setEnabled(boolean enabled) {
        try {
            System.out.println("Set enabled: " + enabled);
            DataOutputStream data = protocol.createNewMessage(ADDR_DRIVER, 2, CMD_DRIVE_LR);
            data.writeByte((enabled)?1:0);            
            protocol.sendMessage();
//            protocol.sendMessage(new byte[]{CMD_DRIVE_LR, (byte) left, (byte) right});
        } catch (IOException ex) {
            Logger.getLogger(KubaPuppet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void incrementOdometry(int left, int right) {
        diffOdometry.addEncoderDiff(left, right);
        positionUpdated();
    }

    private void positionUpdated() {
                RobotPose pose = diffOdometry.getPose();
        broadcastMessage("pos "+pose.getPoint().getX()*1000+" "+
                pose.getPoint().getY()*1000+" "+
                pose.getAzimuth().degrees());
    }

    public Server server;

    private void broadcastMessage(String message) {
        if (server!=null) {
            server.broadcoastMessage(message);
        }
    }

    public static void main(String[] args) throws IOException, PortInUseException, UnsupportedCommOperationException {
        final SerialComm openSerialComm = SerialComm.openSerialComm("/dev/ttyUSB0");
        KubaPuppet puppet = new KubaPuppet(new KubaOutProtocol(openSerialComm));

        KubaInputReader inreader = new KubaInputReader(openSerialComm.getDataInputStream(), puppet);
        inreader.startListening();
        Server server = Server.createServer(puppet, Server.DEFAULT_PORT);
        server.start();

        puppet.setSpeediLR(100, 100);

        puppet.getInfo();

        puppet.stopMotors();

//        kubaPuppet.protocol.dataOutStream.write('K');
//        kubaPuppet.protocol.dataOutStream.flush();

        System.out.println("DONE");
//        System.exit(0);

    }

//    public void setSpeediFT(int forward, int turn) {
//        try {
//            protocol.sendMessage(new byte[]{CMD_DRIVE_FT, (byte) forward, (byte) turn});
//            System.out.println("FT " + CMD_DRIVE_FT + " " + (byte) forward + " " + (byte) turn);
//        } catch (IOException ex) {
//            Logger.getLogger(KubaPuppet.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//    public void setSpeedi(int speed) {
//        try {
//            protocol.sendMessage(new byte[]{CMD_DRIVE_SET_SPEED, (byte) speed});
//        } catch (IOException ex) {
//            Logger.getLogger(KubaPuppet.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    public void setAzimuthWord(int azimuth) {
//        try {
//            protocol.sendMessage(new byte[]{CMD_DRIVE_SET_AZIMUTH, Binary.highByte(azimuth), Binary.lowByte(azimuth)});
//        } catch (IOException ex) {
//            Logger.getLogger(KubaPuppet.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    public void calibrateCompass() {
//        try {
//            protocol.sendMessage(new byte[]{CMD_CMPS_CALIBRATE});
//        } catch (IOException ex) {
//            Logger.getLogger(KubaPuppet.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    public DiffWheels getDiffWheels() {
        return diffWheels;
    }

    static double checkRange(double val, String name) {
        if (Math.abs(val) > 1.0) {
            System.err.println("out of range " + name + " : " + val);
            return Math.max(-1.0, Math.min(val, 1.0));
        }
        return val;
    }
}
