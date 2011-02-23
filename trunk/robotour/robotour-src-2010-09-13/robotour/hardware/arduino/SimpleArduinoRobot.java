package robotour.hardware.arduino;

import javax.swing.JFrame;
import robotour.behavior.Arbitrator;
import robotour.behavior.KeyboardDriving;
import robotour.gui.map.MapView;
import robotour.gui.map.RobotImgLayer;
import robotour.iface.Compass;
import robotour.util.Sonars;

/**
 *
 * @author Kotuc
 */
public class SimpleArduinoRobot {

    final ArdSonar leftSrf = new ArdSonar();
    final ArdSonar rightSrf = new ArdSonar();
    final ArdSonar centerSrf = new ArdSonar();
    final ArdCompass compass = new ArdCompass();
    final ArdOdometer odometer = new ArdOdometer();
    // tracks current robot position
    final ArdOdometry odometry = new ArdOdometry();

    public Compass getCompass() {
        return compass;
    }

    public Sonars getSonars() {
        return new Sonars(leftSrf, centerSrf, rightSrf);
    }

    public static void main(String[] args) throws Exception {

        final SimpleArduinoRobot robot = new SimpleArduinoRobot();

        SLIP slip = new SLIP(ArduinoSerial.getArduino("COM13"), new SimpleMessageInterpreter(robot));

        ArduinoPuppet puppet = new ArduinoPuppet(slip);

//        VideoInput vi = SerialLog.getLoggedVideoInput(VideoInputs.getVideo());

//        new ViewPanel(robot.getSonars(), robot.getCompass(), vi).openWindow();

        MapView mapView = new MapView();

        mapView.addLayer(robot.odometry.track);
        mapView.addLayer(new RobotImgLayer(robot.odometry));


        JFrame showInFrame = mapView.showInFrame();
        showInFrame.setLocation(500, 0);

//        new Arbitrator(new XPadDriving(puppet.getWheels())).start();
        new Arbitrator(new KeyboardDriving(puppet.getWheels())).start();


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
