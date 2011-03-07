/*
 * GPX.java
 *
 * Created on 12. srpen 2007, 14:01
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package robotour.navi.gps.input;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import javax.swing.JFileChooser;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import robotour.navi.gps.GPSPoint;
import robotour.navi.gps.Track;
import robotour.navi.gps.TrackPoint;
import robotour.util.Setup;

/**
 * This class provides methods for loading and saving Tracks in GPX format.
 *
 * @author Kotuc
 */
public final class GPXIO {

    /** Creates a new instance of GPX */
    private GPXIO() {
    }

    public static Track loadTrack(File gpxfile) {

        try {

            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();

            GPXHandler gpx = new GPXHandler();

            parser.parse(gpxfile, gpx);

            if ("trackName".equals(gpx.getTrack().getName())) {
                gpx.getTrack().setName(gpxfile.getName());
            }

            return gpx.getTrack();

        } catch (SAXException ex) {
            ex.printStackTrace();
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void saveTrack(Track track, File gpxfile) {
        try {

            PrintStream out = new PrintStream(gpxfile);

            out.println("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
            out.println("<gpx xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" version=\"1.1\" creator=\"kotuc:robotour\" xmlns=\"http://www.topografix.com/GPX/1/1\" xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\">");

            out.println("<metadata>");
            out.println("<author>");
            out.println("<name>" + System.getProperty("user.name") + "</name>");
            out.println("</author>");
            out.println("<time>2007-08-04T17:12:31Z</time>");
//  <bounds minlat="49.92515182495117" minlon="18.011280059814453" maxlat="49.934078216552734" maxlon="18.040918350219727" />
            out.println("</metadata>");
            out.println("<trk>");

            out.println("<name>" + track.getName() + "</name>");

            for (TrackPoint point : track.track()) {
                out.println("<trkpt lat=\"" + point.getPoint().latitude().degrees() + "\" lon=\"" + point.getPoint().longitude().degrees() + "\">");
                out.println("<ele>" + point.getAltitude() + "</ele>");
                out.println("<time>" + point.getTime() + "</time>");
                out.println("</trkpt>");
            }

            out.println("</trk>");
            out.println("</gpx>");

            out.flush();
            out.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private static JFileChooser jfch = new JFileChooser();


    static {
        jfch.setSelectedFile(new File(Setup.getRoot() + "*.xml"));
    }

    public static void showSaveTrackDialog(Track track, Component owner) {
        if (jfch.showSaveDialog(owner) == JFileChooser.APPROVE_OPTION) {
            GPXIO.saveTrack(track, jfch.getSelectedFile());
        }
    }

    public static Track showLoadTrackDialog(Component owner) {
        if (jfch.showOpenDialog(owner) == JFileChooser.APPROVE_OPTION) {
            return GPXIO.loadTrack(jfch.getSelectedFile());
        }
        return null;
    }
}

class GPXHandler extends DefaultHandler {

    private Track track;
    private TrackPoint point;
    private String lastTag = "";

    public Track getTrack() {
        return track;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        lastTag = qName;
        if ("rte".equals(qName) || "trk".equals(qName)) {
            track = new Track();
//            track.trackName = ;
        }
        if ("rtept".equals(qName) || "trkpt".equals(qName)) {
            point = new TrackPoint(GPSPoint.fromDegrees(Double.parseDouble(attributes.getValue("lat")), Double.parseDouble(attributes.getValue("lon"))));
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

        String chars = new String(ch, start, length);

//        println("characters:"+chars);

        if ("ele".equals(lastTag)) {
            throw new UnsupportedOperationException("setAltitude");
        //point.setAltitude(Double.parseDouble(chars));
        } else if ("time".equals(lastTag)) {
        } else if ("name".equals(lastTag)) {
            if (point != null) {
                point.setName(chars);
            } else if (track != null) {
                track.setName(chars);
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("rte".equals(qName) || "trk".equals(qName)) {
            // route complete
            }
        if ("rtept".equals(qName) || "trkpt".equals(qName)) {
            track.append(point);
            point = null;
        }
        lastTag = "";
    }
}