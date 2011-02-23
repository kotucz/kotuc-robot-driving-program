package robotour.hardware.arduino;

import javax.swing.JFrame;
import robotour.behavior.Arbitrator;
import robotour.behavior.KeyboardDriving;
import robotour.gui.ViewPanel;
import robotour.gui.map.MapView;
import robotour.gui.map.RobotImgLayer;
import robotour.util.ShutdownManager;
import robotour.util.Shutdownable;
import robotour.util.log.SerialLog;
import robotour.util.log.events.CompassEvent;
import robotour.util.log.events.Event;
import robotour.util.log.events.EventListener;
import robotour.util.log.events.EventLog;
import robotour.util.log.events.OdometrEvent;
import robotour.util.log.events.SonarEvent;
import robotour.util.log.events.Tags;
import vision.input.VideoInput;
import vision.input.VideoInputs;

/**
 *
 * @author Kotuc
 */
public class ArduinoEventRobot implements EventListener {

    final ArdOdometer odometer = new ArdOdometer();
    // tracks current robot position
    final ArdOdometry odometry = new ArdOdometry();
    EventLog eventLog = new EventLog();

    public EventLog getEventLog() {
        return eventLog;
    }

    public ArduinoEventRobot() {
        ShutdownManager.registerStutdown(new Shutdownable() {

            public void shutdown() {
                System.out.println("Writing Event Log");
                SerialLog.saveEvents(eventLog);
            }
        });
    }

    public void eventRecieved(Event event) {
        logEvent(event);
        if (event == null) {
            throw new NullPointerException("event");
        }
        if (event instanceof CompassEvent) {
            CompassEvent cevent = (CompassEvent) event;
            odometry.updatedAzimuth(cevent.getAzimuth());
        } else if (event instanceof SonarEvent) {
            SonarEvent sevent = (SonarEvent) event;

            if (Tags.left.equals(sevent.getName())) {
//                leftSrf.measuredCM(val);
            } else if (Tags.center.equals(sevent.getName())) {
//                case 'c':
//            System.out.println("center cm: " + val);
//                centerSrf.measuredCM(val);
            } else if (Tags.right.equals(sevent.getName())) {
//                case 'r':
//                System.out.println("right cm: " + val);
//                rightSrf.measuredCM(val);
            }
        } else if (event instanceof OdometrEvent) {
            OdometrEvent oevent = (OdometrEvent) event;
//            System.out.println("encoder ticks: " + val);
            odometer.setTicks(oevent.getTicks());
            odometry.updatedOdometer(odometer.getChange());
        } else {
            System.err.println("unknown event: " + event.toString());
        }
    }

    public static void main(String[] args) throws Exception {

        final ArduinoEventRobot robot = new ArduinoEventRobot();

        final ArduinoSerial arduino = ArduinoSerial.getArduino("COM13");
//        final ArduinoSerial arduino = new ArduinoSerial(
//                    //                        new FileInputStream("C:/Users/Kotuc/Desktop/logs/arduino-com132-2010-07-25-213138.log"),
//                    //                        new FileInputStream("C:/Users/Kotuc/Desktop/logs/arduino-com132-2010-07-25-213031.log")
//                    new FileInputStream(new File(SerialLog.getLogDirectory(), "run-20100831010244/arduino-in-20100831010244.log")),
//                    System.out);

        SLIP slip = new SLIP(arduino, new EventReceiver(robot));

        ArduinoPuppet puppet = new ArduinoPuppet(slip);

        MapView mapView = new MapView();

        mapView.addLayer(robot.odometry.track);
        mapView.addLayer(new RobotImgLayer(robot.odometry));

        JFrame showInFrame = mapView.showInFrame();
        showInFrame.setLocation(500, 0);

//        Thread.sleep(10000);

        VideoInput vi = VideoInputs.getVideo();

//        VideoInput vi = SerialLog.getLoggedVideoInput(VideoInputs.getVideo());

//        new ViewPanel(robot.getSonars(), robot.getCompass(), vi).openWindow();

//        new Arbitrator(new XPadDriving(puppet.getWheels())).start();
        new Arbitrator(new KeyboardDriving(puppet.getWheels())).start();

        new VideoEventRecorder(vi, robot.getEventLog()).startRecording();


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

    private void logEvent(Event event) {
        eventLog.logEvent(event);
    }
}
