package robotour.behavior;

import robotour.util.RobotSystems;
import robotour.iface.MeasureException;
import robotour.iface.Sonar;
import robotour.iface.Compass;
import robotour.iface.Wheels;
import robotour.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.navi.basic.Azimuth;

/**
 * compas 3x sonar
 * @author Kotuc
 */
public class MainC3S implements Runnable {

    private final Compass cmps;
    private final Wheels wheels;
    private final Sonar srfc; // center sonar

    private MainC3S() throws RemoteException, NotBoundException {
        RobotSystems systems = RobotSystems.getDefault();
        cmps = systems.getCompass();
        wheels = systems.getWheels();
        srfc = systems.getCenterSonar();//new SRF08(DeviceManager.getI2C(), 0xE0);
    }
    private Azimuth targetAzimuth = Azimuth.valueOfRadians(0.0);

    public void run() {
        while (true) {

            double diff = 0;
            Azimuth azimuth = null;
            try {
                azimuth = cmps.getAzimuth();
                diff = (targetAzimuth.radians() - azimuth.radians());
            } catch (MeasureException ex) {
                Logger.getLogger(MainC3S.class.getName()).log(Level.SEVERE, null, ex);
            }

//            System.out.println("azimuth: "+azimuth);
            while (diff > Math.PI) {
                diff -= 2 * Math.PI;
            }
            while (diff < -Math.PI) {
                diff += 2 * Math.PI;
            }

// not influenced by compass
//            wheels.setSteer(0.0);
// lineary

            wheels.setSteer(diff);

// digitaly
//            wheels.setSteer(Math.signum(diff));
            double dist = 0;
            double speed = 0;
            try {
                dist = srfc.getDistance();
            } catch (MeasureException ex) {
                ex.printStackTrace();
            }
            if (dist < 5.0) {
                speed = (Math.min(dist, 1.0));
            }

//            wheels.setSpeed(0);
            wheels.setSpeed(speed);

            System.out.println(new Date() + "; cmps: " + azimuth + "; " + targetAzimuth + " diff: " + diff +
                    "; dist: " + dist + "; speed: " + speed);
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
//        Wheels.getFakeWheels().setSpeed(0);
        new Thread(new MainC3S()).start();
    }
}
