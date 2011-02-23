package robotour.navi.gps;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Kotuc
 * @see http://robotika.cz/guide/gps/cs
 */
public class GPSInput {

    static final double KNOTS_TO_METRES = 0.51444;
    private Track activeLog = new Track();

    public GPSInput() {
    }

    public static GPSInput openPort(String port) throws IllegalArgumentException, IOException, NoSuchPortException {

//        CommPortIdentifier portId = Ports.getPortIdentifier(port);
        CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(port);

        if (portId != null) {
            return new SerialGPSInputListener(portId);
        } else {
            throw new IllegalArgumentException("Port not found.");
        }

    }

    public static GPSInput openFile(File file) throws IllegalArgumentException, IOException {
        return new FileGPSInput(file);
    }

    public Track getActiveLog() {
        return activeLog;
    }

    /**
     * test method
     */
    private void generateSignal() {
        parseNMEA("$GPGSV,3,1,09,29,82,232,26,26,77,279,33,28,57,070,32,08,29,077,24*77");
        parseNMEA("$GPGSV,3,2,09,18,22,314,25,09,21,269,18,17,20,128,11,10,16,197,*71");
        parseNMEA("$GPGSV,3,3,09,27,06,085,11*4E");
    }

    /**
     * Receives nmea raw sentence, checks if its valid and sends it to the right parsing (RMC, GGA, GSV, ..)
     * @param nmea whole NMEA sentence
     */
    public void parseNMEA(String nmea) {
        try {
            boolean valid = checkSum(nmea);
//            getLogger().log(Level.INFO, "Sentence: " + nmea + ";" + (valid ? "VALID" : "INVALID"));
            if (valid) {
                //            println("next parsing");
                String data = nmea.substring(nmea.indexOf("$") + 1, nmea.indexOf("*"));
//                getLogger().log(Level.INFO, "recieving " + data.substring(0, 5));
                if (data.startsWith("GPRMC")) {
                    parseRMC(data);
                }
                if (data.startsWith("GPGGA")) {
                    parseGGA(data);
                }
                if (data.startsWith("GPGSV")) {
                    parseGSV(data); // satelites
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean checkSum(String nmea) {

        //        println("$"+nmea.indexOf("$")+" *"+nmea.indexOf("*")+" chs"+chs);

        String data = nmea.substring(nmea.indexOf("$") + 1, nmea.indexOf("*"));

        String chs = nmea.substring(nmea.indexOf("*") + 1).trim();
        int chsvalue;
        try {
            chsvalue = Integer.parseInt(chs, 16);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            return false;
        }


        int checksum = 0;
        for (int i = 0; i < data.length(); i++) {
            checksum ^= data.charAt(i);
        }

        //        println(data+"; checksum: "+checksum+" chs: "+chs+" ("+chsvalue+")");
        return (checksum == chsvalue);
    }
    private GPSPoint point;
    private Azimuth azimuth;
    private double speed;
    private int numChannels;
    private double precision = -1;
    private double altitude = 0;
    private long lastupdatetime;

//    @Override
//    public int getState() {
//        if (isValid()) {
//            return STATE_ON;
//        } else {
//            return STATE_WARNING;
//        }
//    }
//    @Override
//    public String getStateText() {
//        if (!isValid()) {
//            return "no signal";
//        } else {
//            return super.getStateText();
//        }
//    }
    public boolean isValid() {
        return (System.currentTimeMillis() - lastupdatetime) < 3000;
    }
    private String lastrmc, lastgga;

    protected Logger getLogger() {
        return Logger.getLogger("GPSInput");
    }

    private void parseRMC(String data) {
        String[] param = data.split(",");

        String time = param[1];
        String date = param[9];

        boolean valid = "A".equals(param[2]);

        if (valid) {

            Latitude latitude = Latitude.valueOf(parseAngle(param[3], "S".equals(param[4])));

            Longitude longitude = Longitude.valueOf(parseAngle(param[5], "W".equals(param[6])));

            speed = Double.parseDouble(param[7]) * KNOTS_TO_METRES;

            azimuth = Azimuth.valueOfDegrees(Double.parseDouble(param[8]));

            if (valid) {
                updatePos(GPSPoint.at(latitude, longitude));
            }

            lastrmc = data;
        }
    }

    private void updatePos(GPSPoint pos) {
        this.point = pos;
        lastupdatetime = System.currentTimeMillis();

        activeLog.append(new TrackPoint(pos));
    }

    /**
     * Parses angle from stupid format.
     * Examples:
     * # 5006.3171 - latitude: 50 stupnu, 6.3171 minut
     * # 01425.6622 - longitude: 14 stupnu, 25.6622 minut
     * @param angle
     * @return angle
     */
    private Angle parseAngle(String raw, boolean negative) {
        double num = Double.parseDouble(raw) / 100.0;
        double angle = Math.floor(num);
        angle += (num - angle) / 0.60;
        return new Angle(((negative) ? -1 : 1) * Math.toRadians(angle));
    }

    /**
     *  parses GGA sentence
     *  contains geographic position, altitude, ..
     */
    private void parseGGA(String data) {
        String[] param = data.split(",");

        boolean valid = "1".equals(param[6]);
        if (valid) {

            String time = param[1];

            Latitude latitude = Latitude.valueOf(parseAngle(param[2], "S".equals(param[3])));

            Longitude longitude = Longitude.valueOf(parseAngle(param[4], "W".equals(param[5])));


            numChannels = toInt(param[7]);

            precision = Float.parseFloat(param[8]);

            altitude = Float.parseFloat(param[9]);

            if (valid) {

                System.out.println("GGA " + time + ": " + point + " hdp: " + precision + " altitude: " + altitude + " channels: " + numChannels + " valid: " + valid + " ");

                updatePos(GPSPoint.at(latitude, longitude));
            }

            lastgga = data;
        }
    }
    private int[] tempSatsInt = null;
    private int[] satelitesInt = new int[]{10, 70, 320, 60};

    public int[] getSatelitesInt() {
        return satelitesInt;
    }

    public List<Satellite> getSatellites() {
        return satelites;
    }
    private List<Satellite> satelites = new LinkedList<Satellite>();
    private List<Satellite> satelitesTemp = new LinkedList<Satellite>();
    private List<Integer> satints = new ArrayList<Integer>();

    /**
     *  parses GSV sentence
     *  information about satelites, their positions and signal intensity
     *
     *
     */
    private void parseGSV(String data) {
        String[] param = (data+" ").split(","); // space to detect last place even if nothing 

        System.out.println(Arrays.toString(param));

        int numMessages = toInt(param[1]);

        int thisMesgNum = toInt(param[2]);

        int numSats = toInt(param[3]);
        if (1 == thisMesgNum) {
            tempSatsInt = new int[4 * numSats];
            this.satelitesTemp = new LinkedList<Satellite>();
            this.satints = new ArrayList<Integer>();
        }

        for (int i = 4; i < param.length; i++) {
        }

        for (int i = 4; i < param.length; i++) {
            final int index = (thisMesgNum - 1) * 16 - 4 + i;
            final int val = toInt(param[i]);

//            System.out.println("" + (index) + "=" + val);

            tempSatsInt[index] = val;

        }

        for (int i = 4; i < param.length; i += 4) {
            satelitesTemp.add(new Satellite(
                    toInt(param[i + 0]),
                    toInt(param[i + 1]),
                    toInt(param[i + 2]),
                    toInt(param[i + 3])));
        }

        // if this is the last message, all satelites recieved .. show
        if (thisMesgNum == numMessages) {
            this.satelitesInt = this.tempSatsInt;
            this.satelites = this.satelitesTemp;
        }

    }

    private int toInt(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException numberFormatException) {
            return 0;
        }
    }

    /**
     * 29,82,232,26 - PRN cislo satelitu, elevace ve stupnich (90max), azimut 000..359, sila signalu dB 00..99
     */
    public class Satellite {

        private final int id, elevation, azimuth, signal;

        private Satellite(int id, int elevation, int azimuth, int signal) {
            this.id = id;
            this.elevation = elevation;
            this.azimuth = azimuth;
            this.signal = signal;
        }

        public int getId() {
            return id;
        }

        public int getElevation() {
            return elevation;
        }

        public int getAzimuth() {
            return azimuth;
        }

        public int getSignal() {
            return signal;
        }
    }

    /**
     * 
     * @return last recieved position
     */
    public GPSPoint getPosition() {
        return point;
    }

    public Azimuth getAzimuth() {
        return azimuth;
    }

    public double getSpeed() {
        return speed;
    }

    public double getAltitude() {
        return altitude;
    }

    public int getNumChannels() {
        return numChannels;
    }

    public double getPrecision() {
        return precision;
    }
}
