package robotour.util.log.events;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Kotuc
 */
public class EventLog {
    List<Event> events = new LinkedList<Event>();

    public synchronized void logEvent(Event e) {
        events.add(e);
    }

    public List<Event> getEvents() {
        return new LinkedList<Event>(events);
    }



}
