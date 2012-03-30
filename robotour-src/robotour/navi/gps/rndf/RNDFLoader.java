package robotour.navi.gps.rndf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.navi.gps.GPSPoint;

/**
 *
 * @author Kotuc
 */
public class RNDFLoader {

    Logger getLogger() {
        Logger logger = Logger.getLogger("RNDFLoader");
        logger.setLevel(Level.OFF);
        return logger;
    }
    private List<String> loadingHelpExits = new ArrayList<String>();

    public RNDFMap load(File file) throws FileNotFoundException, IOException {

        getLogger().log(Level.INFO, "loading map " + file);

        RNDFMap map = new RNDFMap();


        BufferedReader reader = new BufferedReader(new FileReader(file));

//            System.out.println(lnr.readLine());

//            System.out.println(getParam(lnr.readLine(), 1));
        String line = null;
        while ((line = reader.readLine()) != null) {
            String[] params = line.split("\\s");
            String type = params[0];
//            System.out.println(Arrays.toString(params));

            if (type.equals("num_segments")) {
                getLogger().log(Level.INFO, line + "IGNORED");
            } else if (type.equals("num_zones")) {
                getLogger().log(Level.INFO, line + "IGNORED");
            } else if (type.equals("segment")) {
                int i = Integer.parseInt(params[1]);
//                    segments[i - 1] = new Segment(this);
//                    segments[i - 1].read(lnr, i);
                map.addSegment(readSegment(reader, i));
            } else //        if (type.equals("zone")) {
            //            readSegment(lnr, numInteger.parseInt(getParam(line, 1)));
            //        } else
            if (type.equals("RNDF_name")) {
                map.mapName = params[1];
            } else if (type.equals("creation_date")) {
                map.creationDate = params[1];
            } else if (type.equals("format_version")) {
                map.formatVersion = params[1];
            } else if (type.equals("end_file")) {
                for (String exitline : loadingHelpExits) {
                    getLogger().log(Level.INFO, exitline);

                    String[] split = exitline.split("\\s");

                    map.addExit(new Exit(WPID.valueOf(split[1]), WPID.valueOf(split[2])));

                    try {
                        map.getWaypoint(WPID.valueOf(split[1])).addNext(map.getWaypoint(WPID.valueOf(split[2])));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                getLogger().log(Level.INFO, "loaded");
                return map;
            } else {
                getLogger().log(Level.SEVERE, "unread " + line);
            }

        }

        throw new RuntimeException("end_file not found");

    }

    private Lane readLane(BufferedReader lnr, int laneId) throws IOException {
//        Lane lane = new Lane(map);
        getLogger().log(Level.INFO, "lane " + laneId);
        Lane lane = new Lane();
        String line;
        while ((line = lnr.readLine()) != null) {
            String[] params = line.split("\\s");
            String type = params[0];
            if (type.equals("end_lane")) {

                Waypoint prev = null;
                for (Waypoint waypoint : lane.waypoints) {
                    if (prev != null) {
                        prev.addNext(waypoint);
                    }
                    prev = waypoint;
                }
                getLogger().log(Level.INFO, "end_lane");
                lane.refreshBounds();
                return lane;
            } else if (type.equals("num_waypoints")) {
                getLogger().log(Level.INFO, line);
            } else if (type.equals("lane_width")) {
                lane.width = Integer.parseInt(params[1]);
            } else if (type.equals("left_boundary")) {
                lane.leftBoundary = params[1];
            } else if (type.equals("right_boundary")) {
                lane.rightBoundary = params[1];
            } else if (type.equals("exit")) {
                loadingHelpExits.add(line);
            } else if (type.equals("stop")) {
                getLogger().log(Level.WARNING, line + "INGORED");
//                map.getWaypoint(WPID.valueOf(params[1])).stop = true;
            } else if (type.equals("checkpoint")) {
                getLogger().log(Level.WARNING, line + "INGORED");
//                map.checkpoints.put(Integer.parseInt(params[2]), WPID.valueOf(params[1]));
//                getWaypoint(WPID.valueOf(params[1])).checkpoint = Integer.parseInt(params[2]);
            } else {
//                lane.waypoints.get(getId(type) - 1).read(line);
                lane.waypoints.add(readWaypoint(line));
            }
        }

        throw new RuntimeException("lane end not found");
//        return laneId;
    }

    private Segment readSegment(BufferedReader lnr, int segmentId) throws IOException {
        getLogger().log(Level.INFO, "segment " + segmentId);
        String line = null;
        Segment segment = new Segment();
        segment.id = segmentId;
        while ((line = lnr.readLine()) != null) {
            String[] params = line.split("\\s");
            String type = params[0];
            if (type.equals("end_segment")) {
                getLogger().log(Level.INFO, "end_segment");
                return segment;
            } else if (type.equals("num_lanes")) {
                getLogger().log(Level.WARNING, line + "INGORED");
            } else if (type.equals("segment_name")) {
                segment.name = params[1];
            } else if (type.equals("lane")) {
                segment.lanes.add(readLane(lnr, Integer.parseInt(params[1].split("\\.")[1])));
            } else {
                getLogger().log(Level.SEVERE, "unread " + line);
            }
        }

        throw new RuntimeException("no end_segment");
    }

    Waypoint readWaypoint(String line) {
        String[] params = line.split("\\s");
        Waypoint waypoint = new Waypoint();
        waypoint.wpid = WPID.valueOf(params[0]);
        waypoint.point = GPSPoint.fromDegrees(Double.parseDouble(params[1]), Double.parseDouble(params[2]));
        getLogger().log(Level.INFO, "" + waypoint);
        return waypoint;
    }
}
