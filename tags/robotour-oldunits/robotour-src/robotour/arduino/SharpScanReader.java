package robotour.arduino;

import java.io.IOException;

/**
 *
 * @author Kotuc
 */
public class SharpScanReader {

    final SerialComm arduino;

    public SharpScanReader(SerialComm arduino) {
        this.arduino = arduino;
//        arduino.sharpReader = this;
//        dis = new DataInputStream(arduino.is);
    }

    public double[] readScan() throws IOException {
        arduino.getOutputStream().write(55);// any byte to scan
        arduino.getOutputStream().flush();// any byte to scan
//        System.out.println("written");

        double[] vals = new double[arduino.readShort()];

        System.out.println(vals.length);

        for (int i = 0; i < vals.length; i++) {
            final short readShort = arduino.readShort();
            if (readShort>1023) {
                System.err.println(""+i+" "+readShort);
            }
            vals[i] = ((6787.0 / (readShort - 3)) - 4) / 100.0;
        }

        return vals;
    }
//    public
//    public double[] readScan() throws IOException {
//        vals = null;
//        arduino.os.write(55);// any byte to scan
//        arduino.os.flush();// any byte to scan
//        System.out.println("written");
//        while (vals == null) {
////            System.out.println("waiting");
//            try {
//                Thread.sleep(10);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(SharpScanReader.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
////        int r;
////        while ((r = arduino.is.read()) != 180) {
////            System.out.println(r);
////        }
////    double[] vals = new double[180];
////        for (int i = 0; i < vals.length; i++) {
////            while (arduino.is.available() == 0) {
////                try {
////                    Thread.sleep(1);
////                } catch (InterruptedException ex) {
////                    Logger.getLogger(SharpScanReader.class.getName()).log(Level.SEVERE, null, ex);
////                }
////            }
////            double d = arduino.is.read() / 100.0;
////            vals[i] = d;
////        }
//        return vals;
//    }
//    public double[] readScan() {
//        arduino.write(55);// any byte to scan
//        double[] vals = new double[Integer.parseInt(arduino.readLine())];
//        for (int i = 0; i < vals.length; i++) {
//            String line = arduino.readLine();
//            double d = vals[i];
//            vals[i] = Double.parseDouble(line);
//        }
//        return vals;
//    }
}
