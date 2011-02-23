package robotour.hardware.arduino;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import robotour.behavior.Arbitrator;
import robotour.behavior.KeyboardDriving;
import robotour.gui.map.MapView;
import robotour.gui.map.RobotImgLayer;
import robotour.iface.Wheels;
import robotour.navi.local.birdseye.EventPoseEstimator;
import robotour.navi.local.birdseye.Exploring;
import robotour.util.log.SerialLog;
import robotour.util.log.events.CameraSnapEvent;
import robotour.util.log.events.CompassEvent;
import robotour.util.log.events.Event;
import robotour.util.log.events.EventListener;
import robotour.util.log.events.EventLog;
import robotour.util.log.events.OdometrEvent;
import robotour.util.log.events.SonarEvent;
import robotour.util.log.events.Tags;
import robotour.util.log.events.WheelCommandEvent;
import vision.ImageFrame;
import vision.input.VideoInput;
import vision.input.VideoInputs;
import vision.pathfinding.birdseye.BirdsEye;
import vision.pathfinding.knowledge.CharacteristicsRecog;

/**
 *
 * @author Kotuc
 */
public class ArduinoEventRobot implements EventListener {

    final ArdOdometer odometer = new ArdOdometer();
    // tracks current robot position
    final ArdOdometry odometry = new ArdOdometry();
    private final Wheels wheels;
    EventLog eventLog = new EventLog();
    public final EventPoseEstimator estimator = new EventPoseEstimator(odometry);

    public ArduinoEventRobot(ArduinoSerial arduinoSerial) {
        SLIP slip = new SLIP(arduinoSerial, new EventReceiver(this));

        ArduinoPuppet puppet = new ArduinoPuppet(slip, this);

        this.wheels = puppet.getWheels();
    }

    public EventLog getEventLog() {
        return eventLog;
    }

    public ArdOdometry getOdometry() {
        return odometry;
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
        } else if (event instanceof CameraSnapEvent) {
            CameraSnapEvent csevent = (CameraSnapEvent) event;
            try {
                process2(csevent.getImage(new File(SerialLog.getLogDirectory(),
                        "/run-20100913184513=chodniktam/video/")));
            } catch (IOException ex) {
                Logger.getLogger(ArduinoEventRobot.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (event instanceof WheelCommandEvent) {
            WheelCommandEvent csevent = (WheelCommandEvent) event;
            estimator.update(csevent);
        } else {
            System.err.println("unknown event: " + event.toString());
        }
    }
    ImageFrame iframe = new ImageFrame("BirdsEye");
    public final BirdsEye birdsEye = new BirdsEye();

    // in color
    void process(BufferedImage snap) {
//        System.out.println("process"+snap);
        birdsEye.setCameraPosition(odometry.getPoint().getX(), odometry.getPoint().getY(), odometry.getAzimuth().radians());
//        birdsEye.paintProjectedCameraImage(snap);
//        iframe.setImage(birdsEye.getFloorImage());
    }
    CharacteristicsRecog charecog = new CharacteristicsRecog();
    Exploring exploring = new Exploring(birdsEye.getFloor());

    void process2(BufferedImage snap) {
//        System.out.println("process2"+snap);

        charecog.process(snap);
        BufferedImage result = charecog.getDebugImage();
        iframe.setImage(result);

        birdsEye.setCameraPosition(odometry.getPoint().getX(), odometry.getPoint().getY(), odometry.getAzimuth().radians());
        birdsEye.paintProjectedCameraMatrix(charecog.getValues());

        exploring.explore(odometry);

        double speed = 0.5;
        double steer = exploring.randomSearch(odometry, null);
        wheels.setSpeed(speed);
        wheels.setSteer(steer);

//        iframe.setImage(birdsEye.getFloorImage());
    }

    public Wheels getWheels() {
        return wheels;
    }



    public static void main(String[] args) throws Exception {

        final ArduinoSerial arduino = ArduinoSerial.getArduino("COM13");
//        final ArduinoSerial arduino = new ArduinoSerial(
//                    //                        new FileInputStream("C:/Users/Kotuc/Desktop/logs/arduino-com132-2010-07-25-213138.log"),
//                    //                        new FileInputStream("C:/Users/Kotuc/Desktop/logs/arduino-com132-2010-07-25-213031.log")
//                    new FileInputStream(new File(SerialLog.getLogDirectory(), "run-20100831010244/arduino-in-20100831010244.log")),
//                    System.out);

        final ArduinoEventRobot robot = new ArduinoEventRobot(arduino);


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
        new Arbitrator(new KeyboardDriving(robot.getWheels())).start();

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
        eventLog.eventRecieved(event);
    }
}
