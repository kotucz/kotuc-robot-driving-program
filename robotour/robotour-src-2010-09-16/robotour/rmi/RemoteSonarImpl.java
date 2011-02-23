package robotour.rmi;

import java.rmi.RemoteException;
import robotour.iface.MeasureException;
import robotour.iface.Sonar;

class RemoteSonarImpl implements RemoteSonar {

    private final Sonar sonar;

    public RemoteSonarImpl(Sonar sonar) {
        super();
        this.sonar = sonar;
    }

    public double getDistance() throws RemoteException, MeasureException {
        return sonar.getDistance();
    }
}
