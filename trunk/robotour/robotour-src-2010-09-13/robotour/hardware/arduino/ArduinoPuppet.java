package robotour.hardware.arduino;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.iface.Wheels;
import robotour.util.Binary;

/**
 *
 * @author Kotuc
 */
public class ArduinoPuppet {

//    private final ArduinoSerial serial;
    private final SLIP slip;
    public static final byte CMD_STOP = 100;
    public static final byte CMD_DRIVE_LR = 101;
    public static final byte CMD_DRIVE_FT = 102;
    public static final byte CMD_DRIVE_SET_SPEED = 105;
    public static final byte CMD_DRIVE_SET_AZIMUTH = 107;
    public static final byte CMD_CMPS_CALIBRATE = 97;

    public ArduinoPuppet(SLIP slip) {
        this.slip = slip;
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
            slip.sendMessage(new byte[]{CMD_STOP});
        } catch (IOException ex) {
            Logger.getLogger(ArduinoPuppet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setSpeediLR(int left, int right) {
        try {
            slip.sendMessage(new byte[]{CMD_DRIVE_LR, (byte) left, (byte) right});
        } catch (IOException ex) {
            Logger.getLogger(ArduinoPuppet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setSpeediFT(int forward, int turn) {
        try {
            slip.sendMessage(new byte[]{CMD_DRIVE_FT, (byte) forward, (byte) turn});
        } catch (IOException ex) {
            Logger.getLogger(ArduinoPuppet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setSpeedi(int speed) {
        try {
            slip.sendMessage(new byte[]{CMD_DRIVE_SET_SPEED, (byte) speed});
        } catch (IOException ex) {
            Logger.getLogger(ArduinoPuppet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setAzimuthWord(int azimuth) {
        try {
            slip.sendMessage(new byte[]{CMD_DRIVE_SET_AZIMUTH, Binary.highByte(azimuth), Binary.lowByte(azimuth)});
        } catch (IOException ex) {
            Logger.getLogger(ArduinoPuppet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void calibrateCompass() {
        try {
            slip.sendMessage(new byte[]{CMD_CMPS_CALIBRATE});
        } catch (IOException ex) {
            Logger.getLogger(ArduinoPuppet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



    public Wheels getWheels() {
        return new Wheels() {

            int spee;
            int stee;

            public void setSpeed(double speed) {
                spee = (int) (speed * 100);
                setSpeediFT(spee, stee);
            }

            public void setSteer(double steer) {
                stee = (int) (steer * 100);
                setSpeediFT(spee, stee);
            }

            public void stop() {
                spee = 0;
                stee = 0;
                stopMotors();
            }
        };
    }

   
}
