package robotour.util.log.events;

/**
 *
 * @author Kotuc
 */
public class OdometrEvent extends ArduinoEvent {

    private final short ticks;

    OdometrEvent(short ticks, int arduinoTime, long time) {
        super(EventType.encoder, arduinoTime, time);
        this.ticks = ticks;
    }

    public short getTicks() {
        return ticks;
    }

    public static OdometrEvent createOdometrEventTicks(short ticks, int arduinoTime, long time) {
        return new OdometrEvent(ticks, arduinoTime, time);
    }
}
