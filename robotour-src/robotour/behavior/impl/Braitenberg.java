package robotour.behavior.impl;

import robotour.iface.MeasureException;
import robotour.iface.Wheels;
import robotour.util.Sonars;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.behavior.Arbitrator;
import robotour.behavior.Behavior;
import robotour.util.RobotSystems;

/**
 *
 * @author Kotuc
 */
public class Braitenberg implements Behavior {

    private final Wheels wheels;
    private final Sonars sonars;

    public Braitenberg(Wheels wheels, Sonars sonars) {
        this.wheels = wheels;
        this.sonars = sonars;
    }

    public boolean act() {

        double speed = 0;

        try {
            double dist = sonars.getCenter().getDistance();
            if (dist < 5.0) {
                speed = Math.max(-1, (Math.min((dist - 0.10), 1.0)));
            }
        } catch (MeasureException ex) {
            Logger.getLogger(Braitenberg.class.getName()).log(Level.SEVERE, null, ex);
            wheels.stop();
            return false;
        }

        wheels.setSpeed(speed);

        double steer = 0;
        try {
            steer = Math.max(0, 0.5 - sonars.getLeft().getDistance()) - Math.max(0, 0.5 - sonars.getRight().getDistance());
//1 - ...
//        steer += Math.signum(steer)*Math.max(0, 1-dist); // proxiity boost // digital behavior left/right
            steer = Math.signum(steer) * Math.max(0, 1 - speed); // fill the rest of speed with turnign
        } catch (MeasureException ex) {
            Logger.getLogger(Braitenberg.class.getName()).log(Level.SEVERE, null, ex);
            wheels.stop();
            return false;
        }

        wheels.setSteer(Math.max(-1, Math.min(steer, 1)));

        System.out.println(System.currentTimeMillis() + "; " + sonars + "; speed: " + speed + " steer: " + steer);

        try {
            Thread.sleep(15);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        return true;
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
//        Wheels.getFakeWheels().setSpeed(0);
        RobotSystems aDefault = RobotSystems.getDefault();
        new Arbitrator(new Braitenberg(aDefault.getWheels(), aDefault.getSonars())).start();
    }
}
