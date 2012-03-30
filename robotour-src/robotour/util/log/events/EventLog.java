package robotour.util.log.events;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.util.ShutdownManager;
import robotour.util.Shutdownable;
import robotour.util.log.SerialLog;

/**
 *
 * @author Kotuc
 */
public class EventLog implements EventListener {

    final List<Event> events = new LinkedList<Event>();
    EventsXmlWriter xmlwriter;

    public EventLog() {
        this(true);
    }

    public EventLog(boolean logEnabled) {
        if (logEnabled) {
            try {
                File file = new File(SerialLog.getRunLogDirectory(), "events-" + SerialLog.createReadableTime() + ".xml");
                this.xmlwriter = new EventsXmlWriter(new FileOutputStream(file));
                xmlwriter.start();
                try {
                } catch (Exception ex) {
                    Logger.getLogger(SerialLog.class.getName()).log(Level.SEVERE, null, ex);
                }
                ShutdownManager.registerStutdown(new Shutdownable() {

                    public void shutdown() {
                        System.out.println("Closing Event Log");
//                        System.out.println("Writing Event Log");
//                        SerialLog.saveEvents(eventLog);
                        xmlwriter.close();
                    }
                });
            } catch (Exception ex) {
                Logger.getLogger(EventLog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public synchronized void eventRecieved(Event e) {
        events.add(e);
        if (xmlwriter!=null) {
            xmlwriter.writeEvent(e);
        }
    }

    public synchronized List<Event> getEvents() {
        return new LinkedList<Event>(events);
    }

    public void sortEventsByTime() {
        List<Event> events1 = getEvents();
        Collections.sort(events1, new TimeEventComparator());
    }


    class TimeEventComparator implements Comparator<Event> {

        public int compare(Event event1, Event event2) {
            if (event1.equals(event2)) {
                return 0;
            }
            if (event1.getTime() < event2.getTime()) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
