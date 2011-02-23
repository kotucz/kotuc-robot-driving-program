package robotour.hardware.arduino;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.iface.Wheels;
import robotour.util.Binary;
import robotour.util.log.events.EventListener;
import robotour.util.log.events.WheelCommandEvent;

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
    private final EventListener eventlog;

    public ArduinoPuppet(SLIP slip, EventListener eventlog) {
        this.slip = slip;
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

            private int spee;
            private int stee;
            private double sp;
            private double st;

            private double checkRange(double val, String name) {
                if (Math.abs(val) > 1.0) {
                    System.err.println("out of range " + name + " : " + val);
                    val = Math.max(-1.0, Math.min(val, 1.0));
                }
                return val;
            }

            private void send() {
                long currentTimeMillis = System.currentTimeMillis();
                setSpeediFT(spee, stee);
                eventlog.eventRecieved(new WheelCommandEvent(sp, st, currentTimeMillis));
            }

            public void setSpeed(double speed) {
                checkRange(speed, "speed");
                sp = speed;
                spee = (int) (sp * 125);
                send();
            }

            public void setSteer(double steer) {
                checkRange(steer, "steer");
                st = steer;
                stee = (int) (st * 125);
                send();
            }

            public void stop() {
                spee = 0;
                stee = 0;
                stopMotors();
            }
        };
    }
}
