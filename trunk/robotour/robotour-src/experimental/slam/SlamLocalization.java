package experimental.slam;

import robotour.gui.map.Paintable;
import robotour.navi.basic.RobotPose;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import robotour.navi.basic.LocalPoint;
import robotour.gui.map.MapLayer;
import robotour.gui.map.MapView;
import robotour.gui.map.RobotImgLayer;
import robotour.arduino.SerialComm;
import robotour.arduino.SharpScanReader;
import robotour.navi.basic.Azimuth;

/**
 *
 * @author Kotuc
 */
public class SlamLocalization implements MapLayer, Runnable {

    RobotPose pose;
    RobotImgLayer robotImg;
    SharpScanReader scanner;
    MapView view;
//    ScanTracer tracer;

    public SlamLocalization() {
        LocalPoint center = new LocalPoint(0, 1);
        Azimuth azim = Azimuth.valueOfDegrees(0);

        pose = new RobotPose(center, azim);

        robotImg = new RobotImgLayer(pose);


        try {
            final SerialComm arduino = new SerialComm(
                    //                        new FileInputStream("C:/Users/Kotuc/Desktop/logs/arduino-com132-2010-07-25-213138.log"),
                    //                        new FileInputStream("C:/Users/Kotuc/Desktop/logs/arduino-com132-2010-07-25-213031.log")
                    new FileInputStream("./logs/run-20100726132909/arduino-in-20100726132909.log"),
                    new OutputStream() {

                        @Override
                        public void write(int b) throws IOException {
                            System.out.println("out:" + b);
                        }
                    });
            scanner = new SharpScanReader(arduino);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SlamLocalization.class.getName()).log(Level.SEVERE, null, ex);
        }


//        try {
//            final ArduinoSerial arduino = ArduinoSerial.getArduino("COM13");
//            scanner = new SharpScanReader(arduino);
//        } catch (Exception ex) {
//            Logger.getLogger(SlamLocalization.class.getName()).log(Level.SEVERE, null, ex);
//        }

        view = new MapView();

        view.addLayer(this);

//        view.addLayer(tracer = new ScanTracer(pose, new double[1]));

//   TODO     view.zoomTo(
//                new LocalPoint(0, 1), 25);
        view.showInFrame().setSize(640, 480);

    }

    public void paint(Paintable map) {

        map.setColor(Color.ORANGE);

        map.setColor(Color.RED);

        robotImg.paint(map);

        map.setColor(Color.YELLOW);

        visializeScan(map, pose);

        map.setColor(Color.CYAN);

//        visializeScan(map, sol);

        map.setColor(Color.RED);





    }

    void visializeScan(Paintable map, RobotPose pose) {
    }

    public void run() {
        while (true) {
            System.out.println("scanning");
            try {
                view.addLayer(new ScanTracer(pose, scanner.readScan()));
//                tracer.scan = scanner.readScan();


            } catch (IOException ex) {
                Logger.getLogger(SlamLocalization.class.getName()).log(Level.SEVERE, null, ex);
            }
//            System.out.println("Scan: " + tracer.scan.length + " " + Arrays.toString(tracer.scan));
            System.out.println("scanned");




            try {
                Thread.sleep(5000);






            } catch (InterruptedException ex) {
                Logger.getLogger(SlamLocalization.class.getName()).log(Level.SEVERE, null, ex);
            }




        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {

        SlamLocalization localization = new SlamLocalization();
//        new ArduinoSerialInputListener(localization, Ports.getPortIdentifier("COM13"));
//        Thread.sleep(10000);





        new Thread(localization).start();



    }
}
