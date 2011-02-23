package robotour.util.log.events;

/**
 *
 * @author Kotuc
 */
public class UnknownEvent extends Event {

    private String text;

    public UnknownEvent(String text, long time) {
        super(EventType.unknown, time);
        this.text = text;
    }

}
