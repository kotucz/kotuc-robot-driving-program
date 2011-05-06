package eurobot.kuba;

import eurobot.kuba.remote.Server;
import eurobot.kuba.remote.StringMessageListener;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import robotour.arduino.SerialComm;
import robotour.driving.DiffPilot;
import robotour.hardware.Ports;
import robotour.iface.DiffWheels;
import robotour.navi.basic.Angle;

/**
 *
 * @author Kotuc
 */
public class KubaPuppet {

    final DiffWheels diffWheels;
    
//    private final ArduinoSerial serial;
    public final KubaOutProtocol out;

    final DiffPilot pilot;

    //    private final EventListener eventlog;
    public KubaPuppet(KubaOutProtocol kprotocol/*, EventListener eventlistener*/) {
        this.out = kprotocol;
//        this.eventlog = eventlistener;
//        this.serial = serial        

        this.diffWheels = new DiffWheels() {

            public void setSpeedsLR(double leftSpeed, double rightSpeed) {
                double left = checkRange(leftSpeed, "leftSpeed");
                double right = checkRange(rightSpeed, "rightSpeed");
                int ileft = (int) (left * 90);
                int iright = (int) (right * 90);
                long currentTimeMillis = System.currentTimeMillis();
                out.setSpeediLR(ileft, iright);
//                eventlog.eventRecieved(new WheelCommandEvent(left, right, currentTimeMillis));
            }

            public void stop() {
                out.setSpeediLR(0, 0);
            }
        };

        this.pilot = new DiffPilot(diffWheels);

    }


//    public void goForward(double distm) {
////        slip.sendMessage(new int[] {CMD_GO_CM});
//    }
//
//    public void setSpeedLR(double mpers) {
//        slip.sendMessage(new int[]{CMD_DRIVE_FT,});
//    }
//    public void stop() {
//        out.setEnabled(false);
//        out.setSpeediLR(0, 0);
//    }

    void forward(double dist) {
        pilot.forward(dist, true);
    }

    void turnR(Angle right) {
        pilot.rotate(right, true);
    }



    

//    public Server server;
    public StringMessageListener server;

    private void broadcastMessage(String message) {
        if (server!=null) {
            server.message(message);
        }
    }

    public static void main(String[] args) throws IOException, PortInUseException, UnsupportedCommOperationException, InterruptedException {
//        String port = "COM23";
//        String port = "/dev/ttyUSB0";
        String port = Ports.getSomeName();
        int baud = 115200;
       
        final SerialComm serial = SerialComm.openSerialComm(port, baud);
        KubaPuppet puppet = new KubaPuppet(new KubaOutProtocol(serial));

//        KubaInputReader inreader = new KubaInputReader(serial.getDataInputStream(), puppet);
//        inreader.startListening();

        System.out.println("Starting server");
        Server server = Server.createServer(puppet, Server.DEFAULT_PORT);
        server.start();

        puppet.out.setEnabled(true);
        
        System.out.println("Starting state command executor");
        new Thread(new StateCommandExecutor(puppet)).start();
        

        System.out.println("READY");


//        puppet.ping();
//
//        Thread.sleep(1000);
//
//        puppet.getInfo();
//
//        Thread.sleep(1000);
//
//        puppet.setEnabled(true);
//
//        Thread.sleep(1000);
//
//        puppet.setSpeediLR(100, 100);
//
//        Thread.sleep(1000);
//
//        puppet.stopMotors();
//
//        Thread.sleep(1000);
//System.out.println("DONE");

        
//        kubaPuppet.protocol.dataOutStream.write('K');
//        kubaPuppet.protocol.dataOutStream.flush();

        
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
//    public DiffWheels getDiffWheels() {
//        return diffWheels;
//    }

    static double checkRange(double val, String name) {
        if (Math.abs(val) > 1.0) {
            System.err.println("out of range " + name + " : " + val);
            return Math.max(-1.0, Math.min(val, 1.0));
        }
        return val;
    }
}
