package robotour.tests;

import java.io.File;
import java.io.FileInputStream;
import javax.swing.JFrame;
import robotour.gui.map.MapView;
import robotour.hardware.arduino.ArduinoEventRobot;
import robotour.hardware.arduino.ArduinoPuppet;
import robotour.hardware.arduino.ArduinoSerial;
import robotour.hardware.arduino.EventReceiver;
import robotour.hardware.arduino.SLIP;
import robotour.util.log.SerialLog;

/**
 *
 * @author Kotuc
 */
public class EventsSaveTest {

    public static void main(String[] args) throws Exception {

        final ArduinoEventRobot robot = new ArduinoEventRobot();

//        final ArduinoSerial arduino = ArduinoSerial.getArduino("COM13");
        final ArduinoSerial arduino = new ArduinoSerial(
                //                        new FileInputStream("C:/Users/Kotuc/Desktop/logs/arduino-com132-2010-07-25-213138.log"),
                //                        new FileInputStream("C:/Users/Kotuc/Desktop/logs/arduino-com132-2010-07-25-213031.log")
                new FileInputStream(new File(SerialLog.getLogDirectory(), "run-20100831010244/arduino-in-20100831010244.log")),
                System.out);

        SLIP slip = new SLIP(arduino, new EventReceiver(robot));

        ArduinoPuppet puppet = new ArduinoPuppet(slip);

//        VideoInput vi = SerialLog.getLoggedVideoInput(VideoInputs.getVideo());

//        new ViewPanel(robot.getSonars(), robot.getCompass(), vi).openWindow();

        MapView mapView = new MapView();
        
        JFrame showInFrame = mapView.showInFrame();
        showInFrame.setLocation(500, 0);

        Thread.sleep(10000);

        SerialLog.saveEvents(robot.getEventLog());

//        new Arbitrator(new XPadDriving(puppet.getWheels())).start();
//        new Arbitrator(new KeyboardDriving(puppet.getWheels())).start();


//        puppet.calibrateCompass();

//        System.out.println("calibrating done");

//        puppet.setSpeediLR(30, 30);
//        Thread.sleep(1000);
//        puppet.setSpeediLR(0, 0);
//        Thread.sleep(1000);
//        puppet.setSpeediLR(-30, -30);
//        Thread.sleep(1000);
//        puppet.setSpeediLR(0, 0);
//        Thread.sleep(1000);
    }
}
