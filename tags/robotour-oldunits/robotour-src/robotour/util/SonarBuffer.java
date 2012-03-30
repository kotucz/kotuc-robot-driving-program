package robotour.util;

import robotour.iface.MeasureException;
import robotour.iface.Sonar;
import robotour.iface.Bufferable;

public class SonarBuffer implements Sonar, Bufferable {

    private final Sonar sonar;

    // volatile so another thread can read updated information
    private volatile double distance;
    private volatile MeasureException error;

    public SonarBuffer(Sonar sonar) {
        super();
        this.sonar = sonar;
    }
    
    public synchronized void readToBuffer() {
        try {
            distance = sonar.getDistance();
            error = null;
        } catch (MeasureException ex) {
            this.error = ex;
        }
    }

    public synchronized double getDistance() throws MeasureException {
        if (error != null) {
            throw error;
        }
        return distance;
    }

    @Override
    public String toString() {
        return "SonarBuffer("+sonar+")";
    }



}
