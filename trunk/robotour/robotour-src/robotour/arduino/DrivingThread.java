package robotour.arduino;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.gui.InfoJFrame;
import robotour.iface.Wheels;
import robotour.navi.basic.RobotPose;
import robotour.navi.local.birdseye.Exploring;

class DrivingThread extends Thread {

//    ArduinoEventRobot outer;

    private final Wheels wheels;
    private final RobotPose pose;
    private final Exploring exploring;
    private static final InfoJFrame infoFrame = new InfoJFrame();

    public DrivingThread(Wheels wheels, RobotPose pose, Exploring exploring) {
//        this.outer = outer;
        this.wheels = wheels;
        this.pose = pose;
        this.exploring = exploring;
        
        infoFrame.setLocation(0, 400);        
        infoFrame.setVisible(true);
    }

//    public DrivingThread(ArduinoEventRobot outer) {
//        super();
//        this.outer = outer;
//    }

    public static void waitForTime(int hours, int mins) {
        
        infoFrame.setVisible(true);

        Date date = new Date();
        date.setHours(hours);
        date.setMinutes(mins);
        date.setSeconds(0);
        
        long starttime = date.getTime();



        while (starttime>System.currentTimeMillis()) {
            infoFrame.setTime(System.currentTimeMillis()-starttime);
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(DrivingThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @Override
    public void run() {

        waitForTime(16, 32);
        
//        final int starthours = 11;
//        final int startmins = 03;
//
//        Date date = new Date();
//        date.setHours(starthours);
//        date.setMinutes(startmins);
//
//        long starttime = date.getTime();
//
//        while (starttime>System.currentTimeMillis()) {
//            infoFrame.setTime(System.currentTimeMillis()-starttime);
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(DrivingThread.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }

        final double setspeed = 0.5;
        while (true) {
            try {
                double speed = setspeed;
                double steer = exploring.randomSearch(pose, speed);
                speed = exploring.recomendedSpeed;
//                        wheels.setSpeed(exploring.recomendedSpeed);
                infoFrame.setSpeed(speed);
                wheels.setSpeed(speed);
                //                        wheels.setSteer();
                infoFrame.setSteer(steer);
                wheels.setSteer(steer);
                Thread.sleep(10);
            } catch (Exception ex) {
                Logger.getLogger(ArduinoEventRobot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
