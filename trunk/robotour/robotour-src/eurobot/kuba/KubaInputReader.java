package eurobot.kuba;

import robot.navi.diff.DiffOdometry;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

class KubaInputReader implements Runnable {

    final DataInputStream dataInStream;
//    final SerialComm serial;
//    final BinaryMessageReceived messager;
    final DiffOdometry odometry = new DiffOdometry();
    final KubaPuppet puppet;

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
                byte startBit = dataInStream.readByte();
                byte address = dataInStream.readByte();
                byte length = dataInStream.readByte();
                byte[] array = new byte[length];
                for (int i = 0; i < length; i++) {
                    array[i] = dataInStream.readByte();
                }
                System.out.println("Robot received: " + startBit + " " + address + " " + length + " " + Arrays.toString(array));
                //                    received(array);
            } catch (EOFException ex) {
//                System.out.println("Robot received: " + startBit + " " + address + " " + length + " " + Arrays.toString(array));
                System.out.print("eof");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex1) {
                    Logger.getLogger(KubaInputReader.class.getName()).log(Level.SEVERE, null, ex1);
                }
            } catch (IOException ex) {
                Logger.getLogger(KubaOutProtocol.class.getName()).log(Level.SEVERE, null, ex);                
            }
        }
    }

    void startListening() {
        System.out.print("Listening ... ");
        new Thread(this).start();
    }
//    private void received(List<Byte> buffer) {
//        messager.messageRecieved(Binary.toArray(buffer));
//    }
}
