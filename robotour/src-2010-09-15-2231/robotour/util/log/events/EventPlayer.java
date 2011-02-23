package robotour.util.log.events;

import java.io.File;
import java.io.FileInputStream;
import javax.swing.JFrame;
import robotour.gui.map.FloorLayer;
import robotour.gui.map.MapView;
import robotour.gui.map.RobotImgLayer;
import robotour.hardware.arduino.ArduinoEventRobot;
import robotour.hardware.arduino.ArduinoPuppet;
import robotour.hardware.arduino.ArduinoSerial;
import robotour.hardware.arduino.EventReceiver;
import robotour.hardware.arduino.SLIP;
import robotour.navi.local.birdseye.Exploring;
import robotour.util.log.SerialLog;

/**
 *
 * @author Kotuc
 */
public class EventPlayer {

    public static void main(String[] args) throws Exception {


//        final ArduinoSerial arduino = ArduinoSerial.getArduino("COM13");
        final ArduinoSerial arduino = new ArduinoSerial(
                //                        new FileInputStream("C:/Users/Kotuc/Desktop/logs/arduino-com132-2010-07-25-213138.log"),
                //                        new FileInputStream("C:/Users/Kotuc/Desktop/logs/arduino-com132-2010-07-25-213031.log")
                //                    new FileInputStream(new File(SerialLog.getLogDirectory(), "run-20100831010244/arduino-in-20100831010244.log"))
                System.in,
                System.out);

        final ArduinoEventRobot robot = new ArduinoEventRobot(arduino);

        MapView mapView = new MapView();

        mapView.addLayer(new FloorLayer(robot.birdsEye.getFloor()));
        mapView.addLayer(robot.getOdometry().getTrack());
        mapView.addLayer(new RobotImgLayer(robot.getOdometry()));
        final Exploring exploring = new Exploring(robot.birdsEye.getFloor());
        exploring.explore(robot.getOdometry());
        mapView.addLayer(exploring);
        mapView.addLayer(robot.estimator.track);

        System.err.println("Showing");

        JFrame showInFrame = mapView.showInFrame();
        showInFrame.setLocation(500, 0);

        EventsXmlReader eventsXmlReader = new EventsXmlReader(new FileInputStream(new File(SerialLog.getLogDirectory(),
                //                "/run-20100913022400/events-20100913022400.xml"
                "/run-20100913184513=chodniktam/events-20100913184658.xml")));
        eventsXmlReader.parse();
        EventLog eventLog = eventsXmlReader.getEventLog();

        for (Event event : eventLog.getEvents()) {
//            long time = event.getTime();
            robot.eventRecieved(event);
            Thread.sleep(1);
        }

        System.out.println("All events played");
//        Thread.sleep(10000);

//        VideoInput vi = VideoInputs.getVideo();

//        VideoInput vi = SerialLog.getLoggedVideoInput(VideoInputs.getVideo());

//        new ViewPanel(robot.getSonars(), robot.getCompass(), vi).openWindow();

//        new Arbitrator(new XPadDriving(puppet.getWheels())).start();
//        new Arbitrator(new KeyboardDriving(puppet.getWheels())).start();

//        new VideoEventRecorder(vi, robot.getEventLog()).startRecording();


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
