package robotour.util.log.events;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.hardware.arduino.ArduinoEventRobot;
import robotour.hardware.arduino.ArduinoSerial;
import robotour.util.log.SerialLog;

/**
 *
 * @author Kotuc
 */
public class EventPlayer {

    private static File findEventsXml(File dir) {
        for (String name : dir.list()) {
            System.out.println("" + name);
            if (name.contains("event")) {
                File eventsXml = new File(dir, name);
                return eventsXml;
            }
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        final File replayDir = new File(SerialLog.getLogDirectory(), "/run-20100915173305-chodnik-qr-n/");
//        final File replayDir = new File(SerialLog.getLogDirectory(), "/run-20100916000507-shortride/");
//        final String eventsXml = "/events-20100915173305.xml";

//        final ArduinoSerial arduino = ArduinoSerial.getArduino("COM13");
        final ArduinoSerial arduino = new ArduinoSerial(
                //                        new FileInputStream("C:/Users/Kotuc/Desktop/logs/arduino-com132-2010-07-25-213138.log"),
                //                        new FileInputStream("C:/Users/Kotuc/Desktop/logs/arduino-com132-2010-07-25-213031.log")
                //                    new FileInputStream(new File(SerialLog.getLogDirectory(), "run-20100831010244/arduino-in-20100831010244.log"))
                System.in,
                System.out);

        final ArduinoEventRobot robot = new ArduinoEventRobot(arduino);

        robot.showView();

//        MapView mapView = new MapView();
//
//        mapView.addLayer(new FloorLayer(robot.birdsEye.getFloor()));
//        mapView.addLayer(robot.getOdometry().getTrack());
//        mapView.addLayer(new RobotImgLayer(robot.getOdometry()));
//        final Exploring exploring = new Exploring(robot.birdsEye.getFloor());
//        exploring.explore(robot.getOdometry());
//        mapView.addLayer(exploring);
//        mapView.addLayer(robot.estimator.track);
//
//        System.err.println("Showing");
//
//        JFrame showInFrame = mapView.showInFrame();
//        showInFrame.setLocation(500, 0);

//        EventsXmlReader eventsXmlReader = new EventsXmlReader(new FileInputStream(new File(SerialLog.getLogDirectory(),
//                //                "/run-20100913022400/events-20100913022400.xml"
//                "/run-20100913184513=chodniktam/events-20100913184658.xml")));
        EventsXmlReader eventsXmlReader = new EventsXmlReader(new FileInputStream(
                //                new File(replayDir, eventsXml)
                findEventsXml(replayDir)));
        eventsXmlReader.parse();
        EventLog eventLog = eventsXmlReader.getEventLog();

        for (Event event : eventLog.getEvents()) {
//            long time = event.getTime();

            if (event instanceof CameraSnapEvent) {
                CameraSnapEvent csevent = (CameraSnapEvent) event;
                try {
                    robot.process2(csevent.getImage(new File(replayDir, "/video/")));
                } catch (IOException ex) {
                    Logger.getLogger(ArduinoEventRobot.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

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
