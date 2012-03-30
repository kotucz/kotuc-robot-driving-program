package robotour.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import robotour.iface.MeasureException;

/**
 *
 * @author kotuc
 */
interface RemoteSonar extends Remote {

    double getDistance() throws RemoteException, MeasureException;
}
