package robotour.util.log.events;

/**
 *
 * @author Kotuc
 */
public enum EventType {

    compass(CompassEvent.class),
    sonar(SonarEvent.class),
    encoder(OdometrEvent.class),
    camera(CameraSnapEvent.class),
    wheels(WheelCommandEvent.class),
    unknown(UnknownEvent.class);
    public final Class<? extends Event> clas;

    private EventType(Class<? extends Event> clas) {
        this.clas = clas;
    }
}
