package robotour.util.log.events;

import robotour.iface.Sonar;

/**
 *
 * @author Kotuc
 */
public class SonarEvent extends ArduinoEvent implements Sonar {

    private final double distance;
    private final String name;

    public SonarEvent(String name, double distance, int arduinoTime, long time) {
        super(EventType.sonar, arduinoTime, time);
        this.distance = distance;
        this.name = name;
    }

    public static SonarEvent createSonarEventCM(String which, int distcm, int arduinoTime, long time) {
        return new SonarEvent(which, distcm, arduinoTime, time);
    }

    public double getDistance() {
        return distance;
    }

    public String getName() {
        return name;
    }

    
    


}
