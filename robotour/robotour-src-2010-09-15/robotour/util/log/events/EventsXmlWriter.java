package robotour.util.log.events;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

/**
 * This class is an adaptation for org.openstreetmap.josm.io.XmlWriter and *.GpxWriter
 * for robotour Event logging.
 * @author Kotuc
 * 
 */
public class EventsXmlWriter {

    protected PrintWriter out;

    public EventsXmlWriter(PrintWriter out) {
        this.out = out;
    }

    public EventsXmlWriter(OutputStream out) throws UnsupportedEncodingException {
        this(new PrintWriter(new BufferedWriter(new OutputStreamWriter(out, "UTF-8"))));
    }

    /**
     * Encode the given string in XML1.0 format.
     * Optimized to fast pass strings that don't need encoding (normal case).
     */
    public static String encode(String unencoded) {
        StringBuilder buffer = null;
        for (int i = 0; i < unencoded.length(); ++i) {
            String encS = EventsXmlWriter.encoding.get(unencoded.charAt(i));
            if (encS != null) {
                if (buffer == null) {
                    buffer = new StringBuilder(unencoded.substring(0, i));
                }
                buffer.append(encS);
            } else if (buffer != null) {
                buffer.append(unencoded.charAt(i));
            }
        }
        return (buffer == null) ? unencoded : buffer.toString();
    }
    /**
     * The output writer to save the values to.
     */
    final private static HashMap<Character, String> encoding = new HashMap<Character, String>();

    static {
        encoding.put('<', "&lt;");
        encoding.put('>', "&gt;");
        encoding.put('"', "&quot;");
        encoding.put('\'', "&apos;");
        encoding.put('&', "&amp;");
        encoding.put('\n', "&#xA;");
        encoding.put('\r', "&#xD;");
        encoding.put('\t', "&#x9;");
    }
    private String indent = "";
    EventLog eventLog;

    public void write(EventLog eventLog) {
        writeEvents(eventLog.events);
        close();
    }

    void start() {
        out.println("<?xml version='1.0' encoding='UTF-8'?>");
        out.println("<log version=\"1.0\" >");
        indent = "  ";
    }

    void close() {
        out.print("</log>");
        out.flush();
        out.close();
    }

    private void writeEvents(List<Event> events) {
        for (Event event : events) {
            writeEvent(event);
        }
    }

    void writeEvent(Event event) {
        String attributes = attribute(Tags.type, event.getType().toString());
        attributes += attribute(Tags.time, "" + event.getTime());
        if (event instanceof ArduinoEvent) {
            attributes += attribute(Tags.arduinotime, "" + ((ArduinoEvent) event).getArduinoTime());
        }
        switch (event.getType()) {
            case compass:
                attributes += attribute(Tags.azim, "" + ((CompassEvent) event).getAzimuth().degrees());
                break;
            case encoder:
                attributes += attribute(Tags.ticks, "" + ((OdometrEvent) event).getTicks());
                break;
            case sonar:
                attributes += attribute(Tags.sonarid, "" + ((SonarEvent) event).getName());
                attributes += attribute(Tags.distance, "" + ((SonarEvent) event).getDistance());
                break;
            case camera:
                attributes += attribute(Tags.filename, "" + ((CameraSnapEvent) event).getFilename());
                break;            
            case wheels:
                attributes += attribute(Tags.speed, "" + ((WheelCommandEvent) event).getSpeed());
                attributes += attribute(Tags.steer, "" + ((WheelCommandEvent) event).getSteer());
                break;
            default:
                System.err.println("warning: no special attrs "+event.getType());
        }
        inline(Tags.event, attributes);
        out.flush();
    }

    private String attribute(String key, String value) {
        return " " + key + "=\"" + value + "\"";
    }

    private void openln(String tag) {
        open(tag);
        out.println();
    }

    private void open(String tag) {
        out.print(indent + "<" + tag + ">");
        indent += "  ";
    }

    private void openAtt(String tag, String attributes) {
        out.println(indent + "<" + tag + " " + attributes + ">");
        indent += "  ";
    }

    private void inline(String tag, String attributes) {
        out.println(indent + "<" + tag + " " + attributes + " />");
    }

    private void close(String tag) {
        indent = indent.substring(2);
        out.print(indent + "</" + tag + ">");
    }

    private void closeln(String tag) {
        close(tag);
        out.println();
    }

    /**
     * if content not null, open tag, write encoded content, and close tag
     * else do nothing.
     */
    private void simpleTag(String tag, String content) {
        if (content != null && content.length() > 0) {
            open(tag);
            out.print(encode(content));
            out.println("</" + tag + ">");
            indent = indent.substring(2);
        }
    }
}
