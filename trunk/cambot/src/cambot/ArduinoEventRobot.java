package cambot;

import cambot.DrivingThread;
import cambot.log.LoggedVideoInput;
import java.awt.image.BufferedImage;
import robotour.arduino.ArdOdometer;
import robotour.arduino.ArdOdometry;
import robotour.arduino.ArduinoPuppet;
import robotour.arduino.EventReceiver;
import robotour.arduino.SLIP;
import robotour.arduino.SerialComm;
import robotour.behavior.Arbitrator;
import robotour.behavior.impl.KeyboardDriving;
import robotour.gui.map.LocalPoint;
import robotour.gui.map.MapView;
import robotour.gui.map.RobotImgLayer;
import robotour.iface.Wheels;
import robotour.navi.local.birdseye.BirdMap;
import robotour.localization.odometry.PosePredictor;
import robotour.util.log.events.CameraSnapEvent;
import robotour.util.log.events.CompassEvent;
import robotour.util.log.events.Event;
import robotour.util.log.events.EventListener;
import robotour.util.log.events.EventLog;
import robotour.util.log.events.OdometrEvent;
import robotour.util.log.events.SonarEvent;
import robotour.util.log.events.WheelCommandEvent;
import vision.ImageFrame;
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
    public final PosePredictor estimator = new PosePredictor(odometry);
    public final BirdMap birdMap = new BirdMap();


    public ArduinoEventRobot(SerialComm arduinoSerial) {
        SLIP slip = new SLIP(arduinoSerial, new EventReceiver(this));

        ArduinoPuppet puppet = new ArduinoPuppet(slip, this);

        this.wheels = puppet.getWheels();
        showView();
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
            birdMap.density.process(odometry, sevent);
        } else if (event instanceof OdometrEvent) {
            OdometrEvent oevent = (OdometrEvent) event;
//            System.out.println("encoder ticks: " + val);
            odometer.setTicks(oevent.getTicks());
            odometry.updatedOdometer(odometer.getChange());
        } else if (event instanceof CameraSnapEvent) {
            // ignored
            //            CameraSnapEvent csevent = (CameraSnapEvent) event;
            //            try {
            //                process2(csevent.getImage(new File(SerialLog.getLogDirectory(),
            //                        "/run-20100913184513=chodniktam/video/")));
            //            } catch (IOException ex) {
            //                Logger.getLogger(ArduinoEventRobot.class.getName()).log(Level.SEVERE, null, ex);
            //            }
        } else if (event instanceof WheelCommandEvent) {
            WheelCommandEvent csevent = (WheelCommandEvent) event;
            estimator.update(csevent);
        } else {
            System.err.println("unknown event: " + event.toString());
        }
    }
    ImageFrame camframe = new ImageFrame("Camera");
    ImageFrame iframe = new ImageFrame("Recog");

    {
        iframe.setLocation(0, 300);
    }
    public final BirdsEye birdsEye = new BirdsEye(birdMap.floor);

    // in color
//    void process(BufferedImage snap) {
////        System.out.println("process"+snap);
//        birdsEye.setCameraPosition(odometry.getPoint().getX(), odometry.getPoint().getY(), odometry.getAzimuth().radians());
////        birdsEye.paintProjectedCameraImage(snap);
////        iframe.setImage(birdsEye.getFloorImage());
//    }
    CharacteristicsRecog charecog = new CharacteristicsRecog();
   
    public void process2(BufferedImage snap) {
//        System.out.println("process2"+snap);

        charecog.process(snap);
        camframe.setImage(snap);
        BufferedImage result = charecog.getDebugImage();
        iframe.setImage(result);

        birdsEye.setCameraPosition(odometry.getPoint().getX(), odometry.getPoint().getY(), odometry.getAzimuth().radians());
        birdsEye.paintProjectedCameraMatrix(charecog.getValues());



//        iframe.setImage(birdsEye.getFloorImage());
    }

    public void startDrivingThread() {
        new DrivingThread(wheels, odometry, birdMap.exploring).start();
    }

    public Wheels getWheels() {
        return wheels;
    }

    void startVideoSamplingThread() {
        final LoggedVideoInput videoRecorder = new LoggedVideoInput(VideoInputs.getVideo(), eventLog);

        new Thread() {

            @Override
            public void run() {
                while (true) {
                    try {
                        BufferedImage snap = videoRecorder.snap();
                        process2(snap);
                        Thread.sleep(10);
                    } catch (Exception ex) {
                        System.err.println("video "+ex.getMessage());
//                        Logger.getLogger(ArduinoEventRobot.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }.start();
    }

    public static void main(String[] args) throws Exception {

        final SerialComm arduino = SerialComm.openSerialComm("COM13");
//        final ArduinoSerial arduino = new ArduinoSerial(
//                    //                        new FileInputStream("C:/Users/Kotuc/Desktop/logs/arduino-com132-2010-07-25-213138.log"),
//                    //                        new FileInputStream("C:/Users/Kotuc/Desktop/logs/arduino-com132-2010-07-25-213031.log")
//                    new FileInputStream(new File(SerialLog.getLogDirectory(), "run-20100831010244/arduino-in-20100831010244.log")),
//                    System.out);

        final ArduinoEventRobot robot = new ArduinoEventRobot(arduino);

//        robot.startVideoSamplingThread();

//        Thread.sleep(5000);

        // 10 meters ahead
        robot.birdMap.exploring.setTarget(new LocalPoint(0, 0).move(robot.odometry.getAzimuth(), 10.0));
        robot.showView();

        
//        robot.startDrivingThread();

//        new Arbitrator(new XPadDriving(robot.getWheels())).start();

        new Arbitrator(new KeyboardDriving(robot.getWheels())).start();

//        robot.startVideoThread();




//        MapView mapView = new MapView();
//
//        mapView.addLayer(new FloorLayer(robot.birdsEye.getFloor()));
//        mapView.addLayer(robot.odometry.track);
//        mapView.addLayer(new RobotImgLayer(robot.odometry));
////        mapView.addLayer(robot.birdsEye.)
//
//        JFrame showInFrame = mapView.showInFrame();
//        showInFrame.setLocation(500, 0);

//        Thread.sleep(10000);

//        VideoInput vi = VideoInputs.getVideo();

//        VideoInput vi = SerialLog.getLoggedVideoInput(VideoInputs.getVideo());

//        new ViewPanel(robot.getSonars(), robot.getCompass(), vi).openWindow();



//        new VideoEventRecorder(vi, robot.getEventLog()).startRecording();
//        robot.videoRecorder.startRecording();




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

    void showView() {

        MapView mapView = birdMap.mapView;
        mapView.eyelock = odometry;

//        mapView.addLayer(new FloorLayer(birdsEye.getFloor()));
        mapView.addLayer(getOdometry().getTrack());
        mapView.addLayer(new RobotImgLayer(getOdometry()));
//        final Exploring expl = new Exploring(birdsEye.getFloor(), density);
        mapView.addLayer(estimator.track);

//        System.err.println("Showing");


    }

    private void logEvent(Event event) {
        eventLog.eventRecieved(event);
    }
}
