package eurobot.kuba;

import robotour.pathing.simple.DiffOdometry;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.util.Binary;

class KubaInputReader implements Runnable {

    private enum Event {
        NOT_USED(0, 4),
        COLOR(1, 4),
        ENCODER(2, 4);

        private Event(int cmd, int length) {
            this.id = (byte)cmd;
            this.length = (byte)length;
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
    private final KubaPuppet puppet;

    public KubaInputReader(DataInputStream dataInStream, KubaPuppet puppet) {
        this.dataInStream = dataInStream;
        this.puppet = puppet;
    }

//    public KubaInputReader(DataInputStream dataInStream
////            , SerialComm serial, BinaryMessageReceived messager
//            ) {
//        this.dataInStream = dataInStream;
////        this.serial = serial;
////        this.messager = messager;
//    }
    public void run() {
        while (true) {
            try {
                byte startByte = dataInStream.readByte();
                byte address = dataInStream.readByte();
                byte length = dataInStream.readByte();
                byte[] array = new byte[length];
                for (int i = 0; i < length; i++) {
                    array[i] = dataInStream.readByte();
                }
                received(startByte, address, length, array);
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

    void received(byte startByte, byte address, byte length, byte[] array) {
        Event cmd = Event.parse(array[0]);
        System.out.println("Robot received: " + startByte + " " + address + " " + length + " " + Arrays.toString(array));

        switch (cmd) {
            case ENCODER:
                Binary.toInt16(array, 1);
                Binary.toInt16(array, 3);
                break;
            default:
                System.out.println("Not recognized!");
        }


    }

    void colorSensor(byte id, short red, short green, short blue, short ir, short uv) {
        
    }

    void button(byte id, byte state) {

    }

    void encoder(byte id, byte state) {

    }

    public void incrementOdometry(int left, int right) {
        odometry.addEncoderDiff(left, right);
//        positionUpdated();
    }

    void startListening() {
        System.out.print("Listening ... ");
        Thread t = new Thread(this, "Kuba Listener");
        t.setDaemon(true);
        t.start();
    }
//    private void received(List<Byte> buffer) {
//        messager.messageRecieved(Binary.toArray(buffer));
//    }
}
