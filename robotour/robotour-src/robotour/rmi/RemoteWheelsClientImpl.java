package robotour.rmi;

import java.rmi.RemoteException;
import java.util.concurrent.atomic.AtomicBoolean;
import robotour.iface.Tachometer;
import robotour.iface.Wheels;
import robotour.util.ShutdownManager;
import robotour.util.Shutdownable;

/**
 *
 * @author Tomas
 */
class RemoteWheelsClientImpl implements Wheels, Tachometer {

    private volatile double speed;
    private final AtomicBoolean off = new AtomicBoolean(false);
    private final RemoteWheels remoteWheels;

    public RemoteWheelsClientImpl(RemoteWheels wheels) {
        this.remoteWheels = wheels;
        ShutdownManager.registerStutdown(new Shutdownable() {

            @Override
            public synchronized void shutdown() {
                stop();
                off.set(true);
                System.out.println("RemoteWheels terminated");
    }
        });
    }

    @Override
    public synchronized void setSpeed(double speed) {
        try {
            if (off.get()) {
                throw new RemoteException("already disconnected");
            }
            this.speed = speed;
            remoteWheels.setSpeed(speed);
        //System.out.println(this + " setSpeed");
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    public synchronized double getSpeed() {
        return this.speed;
    }

    @Override
    public synchronized void setSteer(double steer) {
        try {
            if (off.get()) {
                throw new RemoteException("already disconnected");
            }
            remoteWheels.setSteer(steer);
        //System.out.println(this + " setSteer");
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public synchronized void stop() {
        try {
            if (off.get()) {
                throw new RemoteException("already disconnected");
            }
            remoteWheels.stop();
            this.speed = 0;
        //System.out.println(this + " stop");
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "RemoteWheels(" + off + ")";
    }
}
