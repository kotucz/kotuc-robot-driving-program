package robotour.util.log.events;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.SAXParserFactory;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.io.UTFInputStreamReader;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import robotour.navi.gps.Azimuth;
import robotour.util.log.SerialLog;

/**
 *
 * This class is inspired by org.openstreetmap.josm.io.GpxReader
 *
 * @author Kotuc
 *
 */
public class EventsXmlReader {

    private InputSource inputSource;
    private EventLog eventLog;

    public EventLog getEventLog() {
        return eventLog;
    }

    /**
     * Parse the input stream and store the result in trackData and markerData
     *
     */
    public EventsXmlReader(InputStream source) throws IOException {
        this.inputSource = new InputSource(UTFInputStreamReader.create(source, "UTF-8"));
    }

    /**
     *
     * @return True if file was properly parsed, false if there was error during parsing but some data were parsed anyway
     * @throws SAXException
     * @throws IOException
     */
    public boolean parse() throws Exception {
        Parser parser = new Parser();
        SAXParserFactory factory = SAXParserFactory.newInstance();

        //        factory.setNamespaceAware(true);
        factory.newSAXParser().parse(inputSource, parser);
        System.out.println("events "+eventLog.events.size());
        return true;
    }

    private class Parser extends DefaultHandler {

        private EventLog currentData;

        @Override
        public void startDocument() {
            currentData = new EventLog();
        }

        private double parseCoord(String s) {
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException ex) {
                return Double.NaN;
            }
        }

        private LatLon parseLatLon(Attributes atts) {
            return new LatLon(
                    parseCoord(atts.getValue("lat")),
                    parseCoord(atts.getValue("lon")));
        }

        @Override
        public void startElement(String namespaceURI, String qName, String rqName, Attributes atts) throws SAXException {
//            System.out.println(""+qName+";"+rqName);
            if (Tags.event.equals(rqName)) {
//                parseLatLon(atts);

                long time = Long.parseLong(atts.getValue(Tags.time));

                int arduinoTime = Integer.parseInt(atts.getValue(Tags.arduinotime));

                String typeStr = atts.getValue(Tags.type);
                EventType type = EventType.valueOf(typeStr);
//                System.out.println(""+type);
                switch (type) {
                    case compass:
                        currentData.logEvent(new CompassEvent(Azimuth.valueOfDegrees(Double.parseDouble(atts.getValue(Tags.azim))), arduinoTime, time));
                        break;
                    case encoder:
                        currentData.logEvent(new OdometrEvent(Short.parseShort(atts.getValue(Tags.ticks)), arduinoTime, time));
                        break;
                    default:
                        System.err.println("Unprocessed type " + type);
                }
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            String string = new String(ch, start, length);
            if (!string.trim().isEmpty()) {
                System.err.println("characters: " + string);
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public void endElement(String namespaceURI, String qName, String rqName) {
            // nothing necessary
        }

        @Override
        public void endDocument() throws SAXException {
            eventLog = currentData;
        }
    }

    public static void main(String[] args) throws Exception {
        new EventsXmlReader(new FileInputStream(new File(SerialLog.getLogDirectory(), "/run-20100913022400/events-20100913022400.xml"))).parse();
    }
}
