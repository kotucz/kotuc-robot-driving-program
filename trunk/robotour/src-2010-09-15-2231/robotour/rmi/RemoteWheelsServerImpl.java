package robotour.rmi;

import robotour.util.TimeoutWheels;
import java.rmi.RemoteException;
import robotour.iface.Wheels;

/**
 *
 * @author Tomas
 */
class RemoteWheelsServerImpl implements RemoteWheels {

    private final Wheels wheels;

    public RemoteWheelsServerImpl(Wheels wheels) {
        this.wheels = new TimeoutWheels(wheels);
    }

    public synchronized void setSpeed(double speed) throws RemoteException {
        wheels.setSpeed(speed);
    }

    public synchronized void setSteer(double steer) throws RemoteException {
        wheels.setSteer(steer);
    }

    public synchronized void stop() throws RemoteException {
        wheels.stop();
    }

    @Override
    public String toString() {
        return "RemoteWheels";
    }
}
