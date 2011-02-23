package robotour.rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import robotour.iface.Compass;
import robotour.iface.MeasureException;
import robotour.util.RobotSystems;
import robotour.iface.Sonar;
import robotour.util.Sonars;
import robotour.navi.gps.Azimuth;

/**
 *
 * @author Kotuc
 */
public class RobotSystemsClient {

    public static RobotSystems bindServer() throws RemoteException, NotBoundException {
        return bindServer("localhost");
    }

    public static RobotSystems bindServer(String name) throws RemoteException, NotBoundException {

        RobotSystems systems = new RobotSystems();

        Registry registry = LocateRegistry.getRegistry(name);



        RemoteWheels remoteWheels = (RemoteWheels) registry.lookup(RobotSystemsServer.WHEELS_NAME);
        RemoteWheelsClientImpl remoteWheelsClientImpl = new RemoteWheelsClientImpl(remoteWheels);
        systems.setWheels(remoteWheelsClientImpl);
        systems.setEncoder(remoteWheelsClientImpl);



        Sonar remoteSonarCenter = bindSonar(registry, RobotSystemsServer.SONARS_NAME_CENTER);
        Sonar remoteSonarLeft = bindSonar(registry, RobotSystemsServer.SONARS_NAME_LEFT);
        Sonar remoteSonarRight = bindSonar(registry, RobotSystemsServer.SONARS_NAME_RIGHT);
        systems.setSonars(new Sonars(remoteSonarLeft, remoteSonarCenter, remoteSonarRight));

        final RemoteCompass remoteCompass = (RemoteCompass) registry.lookup(RobotSystemsServer.COMPASS_NAME);
        systems.setCompass(
                new Compass() {

                    public Azimuth getAzimuth() throws MeasureException {
                        try {
                            return remoteCompass.getAzimuth();
                        } catch (RemoteException ex) {
                            throw new MeasureException(ex);
                        }
                    }

                    @Override
                    public String toString() {
                        return "RemoteCompass";
                    }
                });

        return systems;

    }

    static Sonar bindSonar(Registry registry, String name) throws RemoteException, NotBoundException, NotBoundException {
        final RemoteSonar remoteSonar = (RemoteSonar) registry.lookup(name);
        return new Sonar() {

            public double getDistance() throws MeasureException {
                try {
                    return remoteSonar.getDistance();
                } catch (RemoteException ex) {
                    throw new MeasureException(ex);
                }

            }
        };
    }
}
