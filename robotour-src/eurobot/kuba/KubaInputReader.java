package eurobot.kuba;

import robotour.pathing.simple.DiffOdometry;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.util.Binary;
//import state.State;

class KubaInputReader {

//    final State state = new State();

    void readMessage() throws IOException {
        byte startByte = dataInStream.readByte();
        byte address = dataInStream.readByte();
        byte length = dataInStream.readByte();
        byte[] array = new byte[length];
        for (int i = 0; i < length; i++) {
            array[i] = dataInStream.readByte();
        }
        received(startByte, address, length, array);
    }

    private enum Event {

        NOT_USED(0, 4),
        COLOR(1, 13),
        ENCODER(2, 4);

        private Event(int cmd, int length) {
            this.id = (byte) cmd;
            this.length = (byte) length;
        }
        final byte id;
        final byte length;

        static Event parse(byte cmdid) {
            return values()[cmdid];
        }
    }
//    public static final byte EVENT_SENSOR = 3;
    final DataInputStream dataInStream;
//    final SerialComm serial;
//    final BinaryMessageReceived messager;
    private final DiffOdometry odometry = new DiffOdometry();
//    private final KubaPuppet puppet;

    public KubaInputReader(DataInputStream dataInStream/*, KubaPuppet puppet*/) {
        this.dataInStream = dataInStream;
//        this.puppet = puppet;
    }

//    public KubaInputReader(DataInputStream dataInStream
////            , SerialComm serial, BinaryMessageReceived messager
//            ) {
//        this.dataInStream = dataInStream;
////        this.serial = serial;
////        this.messager = messager;
//    }
    void received(byte startByte, byte address, byte length, byte[] array) {
//        Event cmd = Event.parse(array[0]);
        System.out.println("Robot received: " + startByte + " " + address + " " + length + " " + Arrays.toString(array));

        switch (address) {
            case KubaOutProtocol.ADDR_DRIVER:
                if (length > 13) {
                    {
                        byte error = array[1];
                        int dist = Binary.toInt32Little(array, 2);
                        short v = Binary.toInt16Little(array, 6);
                        short a = Binary.toInt16Little(array, 8);
                        encoder(KubaOutProtocol.ADDR_ENCODER_LEFT, error, dist, v, a);
                    }
                    {
                        int starti = 9;
                        byte error = array[1 + starti];
                        int dist = Binary.toInt32Little(array, 2 + starti);
                        short v = Binary.toInt16Little(array, 6 + starti);
                        short a = Binary.toInt16Little(array, 8 + starti);
                        encoder(KubaOutProtocol.ADDR_ENCODER_RIGHT, error, dist, v, a);
                    }
                    break;
                }
//                Binary.toInt16(array, 1);
//                Binary.toInt16(array, 3);
                break;
            case KubaOutProtocol.ADDR_COLOR_LEFT:
                short dark = Binary.toInt16(array, 1);
                short ir = Binary.toInt16(array, 3);
                short r = Binary.toInt16(array, 5);
                short g = Binary.toInt16(array, 7);
                short b = Binary.toInt16(array, 9);
                short uv = Binary.toInt16(array, 11);
                colorSensor(address, dark, ir, r, g, b, uv);
                break;
            case KubaOutProtocol.ADDR_ENCODER_LEFT:
            case KubaOutProtocol.ADDR_ENCODER_RIGHT:
                byte error = array[1];
                int dist = Binary.toInt32Little(array, 2);
                short v = Binary.toInt16Little(array, 6);
                short a = Binary.toInt16Little(array, 8);
                encoder(address, error, dist, v, a);
                break;

            default:
                System.out.println("Not recognized!");
        }


    }

    void colorSensor(byte id, short dark, short ir, short red, short green, short blue, short uv) {
    }

    void button(byte id, byte state) {
    }
    private int leftEncoderNew, rightEncoderNew;
    private int leftEncoderPrev, rightEncoderPrev;

    void encoder(byte address, byte error, int dist, short v, short a) {
        if ((((byte) 0xC0) & error) == 0) {
            // ok
            if (address == KubaOutProtocol.ADDR_ENCODER_LEFT) {
                leftEncoderNew = dist;
                System.out.println("Left encoder " + dist);
            } else if (address == KubaOutProtocol.ADDR_ENCODER_RIGHT) {
                rightEncoderNew = dist;
                System.out.println("Right encoder " + dist);
            } else {
                System.out.println("WTF encoder");
            }
        } else {
            System.out.println("encoder " + address + " error: " + error);
        }
    }

    public void updateOdometry() {
        incrementOdometry(leftEncoderNew - leftEncoderPrev, rightEncoderNew - rightEncoderPrev);
        leftEncoderPrev = leftEncoderNew;
        rightEncoderPrev = rightEncoderNew;
    }

    public void incrementOdometry(int left, int right) {
        odometry.addEncoderDiff(left, right);
        System.out.println("Encoders " + left + " " + right);
//        positionUpdated(odometry.getPose());
    }
    private int oposid = 0;

//    private void positionUpdated(RobotPose pose) {
//        state.set("oposid", oposid++);
//        state.set("oposx", pose.getPoint().getX() * 1000);
//        state.set("oposy", pose.getPoint().getY() * 1000);
//        state.set("oposa", pose.getAzimuth().radians());
//        state.set("oposid", oposid++);
//    }

    void startListening() {
        System.out.print("Listening ... ");
        Thread t = new Thread("Kuba Listener") {

            public void run() {
                while (true) {
                    try {
                        readMessage();
                    } catch (EOFException ex) {
//                System.out.println("Robot received: " + startBit + " " + address + " " + length + " " + Arrays.toString(array));
                        System.out.print("eof");
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ex1) {
                            Logger.getLogger(KubaInputReader.class.getName()).log(Level.SEVERE, null, ex1);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(KubaOutProtocol.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
    t.setDaemon(true);
        t.start();
    }
//    private void received(List<Byte> buffer) {
//        messager.messageRecieved(Binary.toArray(buffer));
//    }
}
