package robotour.util.log.events;

/**
 *
 * @author Kotuc
 */
public abstract class ArduinoEvent extends Event {
    
    private final int atime;

    protected ArduinoEvent(EventType type, int arduinoTime, long time) {
        super(type, time);
        this.atime = arduinoTime;
    }

    public int getArduinoTime() {
        return atime;
    }



}
