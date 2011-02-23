package robotour.util.log.events;

/**
 *
 * @author Kotuc
 */
public abstract class Event {

    private final EventType type;
    private final long time;

    protected Event(EventType type, long time) {
        this.type = type;
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public EventType getType() {
        return type;
    }

    
    
//    public abstract void fire(EventListener listener);

}
