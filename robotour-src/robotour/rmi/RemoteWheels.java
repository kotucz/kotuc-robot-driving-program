package robotour.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author kotuc
 */
interface RemoteWheels extends Remote {

    /**
     * 1 .. max forward
     * 0 .. stop
     * -1 .. max backward
     * @param speed
     * @throws RemoteException
     */
    void setSpeed(double speed) throws RemoteException;

    /**
     *
     * 1 .. max right
     * 0 .. straight
     * -1 .. max left
     *
     * @param steer
     * @throws RemoteException
     */
    void setSteer(double steer) throws RemoteException;

    void stop() throws RemoteException;
}
