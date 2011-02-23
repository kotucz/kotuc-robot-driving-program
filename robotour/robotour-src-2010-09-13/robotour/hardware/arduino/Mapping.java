package robotour.hardware.arduino;

import javax.swing.JFrame;
import robotour.behavior.Arbitrator;
import robotour.behavior.KeyboardDriving;
import robotour.gui.map.MapView;
import robotour.gui.map.RobotImgLayer;

/**
 *
 * @author Kotuc
 */
public class Mapping {

    public static void main(String[] args) throws Exception {

        final ArduinoEventRobot robot = new ArduinoEventRobot();

        SLIP slip = new SLIP(ArduinoSerial.getArduino("COM13"), new EventReceiver(robot));

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


    }
}
