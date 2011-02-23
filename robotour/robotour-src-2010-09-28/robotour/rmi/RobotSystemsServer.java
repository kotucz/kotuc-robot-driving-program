package robotour.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import robotour.iface.Compass;
import robotour.iface.MeasureException;
import robotour.util.RobotSystems;
import robotour.navi.gps.Azimuth;

/**
 *
 * @author Kotuc
 */
public class RobotSystemsServer {

    static final String WHEELS_NAME = "RobotServerWheels";
    static final String SONARS_NAME_CENTER = "RobotServerSonarsCenter";
    static final String SONARS_NAME_LEFT = "RobotServerSonarsLeft";
    static final String SONARS_NAME_RIGHT = "RobotServerSonarsRight";
    static final String COMPASS_NAME = "RobotServerCompass";

    private static void configureServer() {
        try {
            RobotSystems systems = RobotSystems.getLocalHard();
            RemoteWheels wheelsImpl = new RemoteWheelsServerImpl(systems.getWheels());
            configure(WHEELS_NAME, wheelsImpl);

            final Compass cmps = systems.getCompass();
            configure(COMPASS_NAME,
                    new RemoteCompass() {

                        public Azimuth getAzimuth() throws RemoteException, MeasureException {
                            return cmps.getAzimuth();
                        }
                    });

            configure(SONARS_NAME_LEFT, new RemoteSonarImpl(systems.getLeftSonar()));
            configure(SONARS_NAME_CENTER, new RemoteSonarImpl(systems.getCenterSonar()));
            configure(SONARS_NAME_RIGHT, new RemoteSonarImpl(systems.getRightSonar()));

        } catch (Exception e) {
            System.err.println("Robot Server exception:");
            e.printStackTrace();
        }
    }

    private static <T extends Remote> T configure(String name, T object) throws RemoteException {
        T stub = (T) UnicastRemoteObject.exportObject(object, 0);
        Registry registry = LocateRegistry.getRegistry();
        registry.rebind(name, stub);
        System.out.println(name + "\t[bound]");
        return stub;
    }

    public static void main(String[] args) {
        System.err.println("NOTE: run with -Djava.rmi.server.hostname=<servername>");
        configureServer();
    }
}
