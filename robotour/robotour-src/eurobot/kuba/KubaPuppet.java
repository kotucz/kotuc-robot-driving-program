package eurobot.kuba;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.iface.DiffWheels;
import robotour.util.Binary;
import robotour.util.log.events.EventListener;
import robotour.util.log.events.WheelCommandEvent;

/**
 *
 * @author Kotuc
 */
public class KubaPuppet {

//    private final ArduinoSerial serial;
    private final KubaProtocol protocol;
    public static final byte CMD_STOP = 100;
    public static final byte CMD_DRIVE_LR = 101;
    public static final byte CMD_DRIVE_FT = 102;
    public static final byte CMD_DRIVE_SET_SPEED = 105;
    public static final byte CMD_DRIVE_SET_AZIMUTH = 107;
    public static final byte CMD_CMPS_CALIBRATE = 97;
    private final EventListener eventlog;

    public KubaPuppet(KubaProtocol slip, EventListener eventlog) {
        this.protocol = slip;
        this.eventlog = eventlog;
//        this.serial = serial;

    }

//    public void goForward(double distm) {
////        slip.sendMessage(new int[] {CMD_GO_CM});
//    }
//
//    public void setSpeedLR(double mpers) {
//        slip.sendMessage(new int[]{CMD_DRIVE_FT,});
//    }
    public void stopMotors() {
        try {
            protocol.sendMessage(new byte[]{CMD_STOP});
        } catch (IOException ex) {
            Logger.getLogger(KubaPuppet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setSpeediLR(int left, int right) {
        try {
            protocol.sendMessage(new byte[]{CMD_DRIVE_LR, (byte) left, (byte) right});
        } catch (IOException ex) {
            Logger.getLogger(KubaPuppet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setSpeediFT(int forward, int turn) {
        try {
            protocol.sendMessage(new byte[]{CMD_DRIVE_FT, (byte) forward, (byte) turn});
            System.out.println("FT " + CMD_DRIVE_FT + " " + (byte) forward + " " + (byte) turn);
        } catch (IOException ex) {
            Logger.getLogger(KubaPuppet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setSpeedi(int speed) {
        try {
            protocol.sendMessage(new byte[]{CMD_DRIVE_SET_SPEED, (byte) speed});
        } catch (IOException ex) {
            Logger.getLogger(KubaPuppet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setAzimuthWord(int azimuth) {
        try {
            protocol.sendMessage(new byte[]{CMD_DRIVE_SET_AZIMUTH, Binary.highByte(azimuth), Binary.lowByte(azimuth)});
        } catch (IOException ex) {
            Logger.getLogger(KubaPuppet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void calibrateCompass() {
        try {
            protocol.sendMessage(new byte[]{CMD_CMPS_CALIBRATE});
        } catch (IOException ex) {
            Logger.getLogger(KubaPuppet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public DiffWheels getDiffWheels() {
        return new DiffWheels() {
            
            public void setSpeedsLR(double leftSpeed, double rightSpeed) {
                double left = checkRange(leftSpeed, "leftSpeed");
                double right = checkRange(rightSpeed, "rightSpeed");
                
                int lef = (int) (left * 90);
                int righ = (int) (right * 90);

                long currentTimeMillis = System.currentTimeMillis();
                setSpeediLR(lef, righ);
                eventlog.eventRecieved(new WheelCommandEvent(left, right, currentTimeMillis));
            }

            public void stop() {
                stopMotors();
            }
        };
    }

    static double checkRange(double val, String name) {
        if (Math.abs(val) > 1.0) {
            System.err.println("out of range " + name + " : " + val);
            return Math.max(-1.0, Math.min(val, 1.0));
        }
        return val;
    }
}
