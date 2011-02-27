package eurobot.kuba;

import java.io.DataInputStream;
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
                short length = dataInStream.readShort();
                short[] array = new short[length];
                for (int i = 0; i < length; i++) {
                    array[i] = dataInStream.readShort();
                }
                System.out.println("Robot received: " + Arrays.toString(array));
                //                    received(array);
            } catch (IOException ex) {
                Logger.getLogger(KubaOutProtocol.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    void startListening() {
        new Thread(this).start();
    }

//    private void received(List<Byte> buffer) {
//        messager.messageRecieved(Binary.toArray(buffer));
//    }

}
