package robotour.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import robotour.iface.MeasureException;
import robotour.navi.gps.Azimuth;

/**
 *
 * @author Kotuc
 */
interface RemoteCompass extends Remote {

    /**
     * Returns orientation in world in radians.
     * 0 is North
     * PI/2 East
     * PI South
     * 3*PI/2 West
     * 
     * @return azimuth in radians
     */
    Azimuth getAzimuth() throws RemoteException, MeasureException;
}
