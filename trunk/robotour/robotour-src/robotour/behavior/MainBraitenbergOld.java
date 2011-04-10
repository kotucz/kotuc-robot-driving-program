/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package robotour.behavior;

import robotour.util.RobotSystems;
import robotour.iface.MeasureException;
import robotour.iface.Compass;
import robotour.iface.Wheels;
import robotour.util.Sonars;
import robotour.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kotuc
 */
public class MainBraitenbergOld implements Runnable {

    private final Compass cmps;
    private final Wheels wheels;
    private final Sonars sonars;
//    private final Odometry odometry;

    private MainBraitenbergOld() {
        RobotSystems systems = RobotSystems.getDefault();

        cmps = systems.getCompass();
//        wheels = Wheels.getFakeWheels(); // debug
        wheels = systems.getWheels();
        sonars = systems.getSonars();
//        odometry = new Odometry(systems.getEncoder(), cmps);
//        odometry.startOdometryThread();
//        LocalMap.showMap(systems);

    }
//    private int targetAzimuth = 90;

    public void run() {

//        try {
//            Thread.sleep(30 * 1000); // 30 s to start
//        } catch (InterruptedException ex) {
//            ex.printStackTrace();
//        }
        long startt = System.currentTimeMillis(); // 90 s to end
        while ((System.currentTimeMillis() - startt) < 90 * 1000) {

//        while (true) {
//          sonars.poll();    // using separate thread
            double speed = 0;
            try {
                double dist;

                dist = sonars.getCenter().getDistance();


                if (dist < 5.0) {
                    speed = Math.max(-1, (3 * Math.min((dist - 0.10), 1.0)));
                }
            } catch (MeasureException ex) {
                Logger.getLogger(MainBraitenbergOld.class.getName()).log(Level.SEVERE, null, ex);
            }

//            wheels.setSpeed(0);
            wheels.setSpeed(speed);


            double steer = 0;
            try {
                steer = (1 - sonars.getLeft().getDistance()) - (1 - sonars.getRight().getDistance());
            } catch (MeasureException ex) {
                Logger.getLogger(MainBraitenbergOld.class.getName()).log(Level.SEVERE, null, ex);
            }

            wheels.setSteer(Math.max(-1, Math.min(steer * 3, 1)));


            System.out.println(System.currentTimeMillis() + "; " + sonars + "; speed: " + speed + " steer: " + steer);
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }


        }
        wheels.stop();

        System.exit(0);
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
//        Wheels.getFakeWheels().setSpeed(0);
        new Thread(new MainBraitenbergOld()).start();
    }
}
