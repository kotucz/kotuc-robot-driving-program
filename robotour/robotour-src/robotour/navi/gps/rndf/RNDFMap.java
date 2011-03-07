package robotour.navi.gps.rndf;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import robotour.util.Setup;
import robotour.gui.map.MapLayer;
import robotour.gui.map.MapView;
import robotour.navi.gps.Track;
import robotour.navi.gps.TrackPoint;

/**
 * TODO TODO TODO TODO TODO
 * Once using this class consider remaking it totally
 * @author PC
 */
public class RNDFMap implements MapLayer {

    /** Creates a new instance of RNDFMap */
    RNDFMap() {
//        super("RNDFMap");
//        loadRNDF(Setup.get(Setup.RNDF_FILE));
    }
    String mapName;
    String formatVersion;
    String creationDate;
    List<Segment> segments = new ArrayList<Segment>();
//    private int s, l, wp;
    Map<Integer, WPID> checkpoints = new HashMap<Integer, WPID>();
    List<Exit> exits = new LinkedList<Exit>();

    void addExit(Exit exit) {
        exits.add(exit);
    }

    public void paint(MapView map) {

        Graphics2D g = map.getGraphics();

        for (Segment segment : segments) {
            segment.paint(g, map);
        }

        for (WPID wpid : checkpoints.values()) {
            getWaypoint(wpid).paint(g, map);
        }

    }

    private Segment getSegment(int id) {
        return segments.get(id - 1);
    }

    Waypoint getWaypoint(WPID id) {
        return segments.get(id.getSegment() - 1).lanes.get(id.getLane() - 1).waypoints.get(id.getWaypoint() - 1);
//        throw new RuntimeException();
    }

//    public void println(String s) {
////        System.out.println(s);
//    }
    /**
     *
     *   @param file
     * @deprecated use getCheckpoints()
     */
//    private void loadMDF(String file) {
//        MDF mdf = new MDF(this);
//        mdf.loadCheckpoints(file);
//
//        List<WPID> chppath = new ArrayList<WPID>();
//        getLogger().log(Level.INFO, "path start");
//        for (int i = 0; i < mdf.num_checkpoints; i++) {
//            WPID wpid;
//            chppath.add(wpid = checkpoints.get(mdf.chpIds[i]));
//            getLogger().log(Level.INFO, "" + (i + 1) + ". " + wpid);
//        }
//        getLogger().log(Level.INFO, "path end");
//
//        LinkedList<Waypoint> path = new LinkedList<Waypoint>();
//
//        getLogger().log(Level.INFO, "detail path start");
//        LinkedList<Waypoint> p1 = null;
//
//        for (int i = 0; i < chppath.size() - 1; i++) {
//            p1 = findPathDepth(chppath.get(i), chppath.get(i + 1));
//            while (p1.size() > 0) {
//                path.addLast(p1.get(0));
//                getLogger().log(Level.INFO, "" + p1.get(0));
//                p1.remove(0);
//            }
//
//        }
//        getLogger().log(Level.INFO, "detail path end");
//    }
    public ArrayList<WPID> getCheckpoints(/*String mdffile*/) {
        MDF mdf = new MDF(this);
        mdf.loadCheckpoints(Setup.get(Setup.MDF_FILE));
        return mdf.chppath;
    }

    /**
     *  returns sequence of waypoints on shortest path
     *
     */
    private LinkedList<Waypoint> findPathDepth(WPID fromid, WPID toid) {
        LinkedList<Waypoint> explore = new LinkedList<Waypoint>();

        // once waypoint explored keeps its shortest distance
        Map<Waypoint, Double> dists = new HashMap<Waypoint, Double>();


        Waypoint end = getWaypoint(toid);
        Waypoint start = getWaypoint(fromid);



        explore.add(start);
        dists.put(start, 0.0);

        Waypoint cur = null;

        while (!explore.isEmpty()) {
            cur = explore.getFirst();
            explore.removeFirst();
            if (cur == end) {
// found end
                break;
            }
            double dis = dists.get(cur);
            for (Waypoint next : cur.nexts) {
                double ndis = dis + 1.0;// to do pythagor
                try {
                    double d = dists.get(next);
                    if (ndis < d) {
                        throw new NullPointerException();
                    }
                } catch (NullPointerException ex) {
//                    println("unexpolored");
                    dists.put(next, ndis);
                    explore.addLast(next);
                }
            }
        }

// rebuild path
        LinkedList<Waypoint> path = new LinkedList<Waypoint>();

        path.addLast(end);

        double shrt = Double.MAX_VALUE;

        while (cur != start) {

            ArrayList<Waypoint> list = cur.prevs;

            for (Waypoint prev : list) {
                try {
                    double d = dists.get(prev);
                    if (d < shrt) {
                        shrt = d;
                        cur = prev;
                    }
                } catch (NullPointerException ex) {
                }
            }

            path.addFirst(cur);
        }

        return path;
    }
//
//    LinkedList<Waypoint> findPathStrange(WPID fromid, WPID toid) {
//        ArrayList<Waypoint> noway = new ArrayList();
//
//        LinkedList<Waypoint> path = new LinkedList();
//
//        Waypoint next = null;
//
//        path.add(getWaypoint(toid));
//
//        while (next!=path.getFirst()) {
//            for (Waypoint prev:path.getFirst().prevs) {
//                if (noway.contains(prev)) continue;
//// revisited
//                if (path.contains(prev)) {
//                    while (path.getFirst()!=prev) path.removeFirst();
//                    path.removeFirst();
//                    noway.add(prev);
//                    continue;
//                }
//
//                path.addFirst(prev);
//                next = prev;
//                break;
//            }
//
//        }
//
//
//
//        return path;
//    }

//    private final double footsToMetres = 0.3048;
    Track createTrack(String path) {
        Track track = new Track();
        Segment prev = getSegment("" + path.charAt(path.length() - 1));
        for (int i = 0; i < path.length(); i++) {
            Segment seg = getSegment("" + path.charAt(i));
            for (Exit exit : exits) {
                if (exit.from().getSegment() == prev.id && (exit.to().getSegment() == seg.id)) {
                    Lane lane = getSegment(exit.to().getSegment()).getLane(exit.to().getLane());
                    for (Waypoint waypoint : lane.waypoints) {
                        track.append(new TrackPoint(waypoint.getPoint()));
                    }
                }
            }
            prev = seg;
        }
        return track;
    }

    private Segment getSegment(String name) {
        if (segmap.containsKey(name)) {
            return segmap.get(name);
        } else {
            throw new IllegalArgumentException("Segment '" + name + "' not found!");
        }
    }

    /**
     *  returns track from lanes where specified checkpoints are
     * @param checkpointpath
     * @return
     */
    public Track getTrack(int[] checkpointpath) {

        throw new RuntimeException("not inplem");

//        Track track = new Track();
//
//        for (int i = 0; i < checkpointpath.length; i++) {
//
//            WPID wpid = checkpoints.get(checkpointpath[i]);
//            Lane lane = getSegment(wpid.getSegment()).getLane(wpid.getLane());
//            track.track().addAll(lane.getTrack().track());
//
//        }

//        GPX.exportTrack(track, new File(Setup.rootPath+"tracks/rndfexport.xml"));

//        return track;
    }
    Map<String, Segment> segmap = new HashMap<String, Segment>();

    void addSegment(Segment segment) {
        segmap.put(segment.getName(), segment);
        segments.add(segment);
    }
//    class Exit extends Waypoint {
//
//        void paint(Graphics g) {
//            g.setColor(Color.RED);
//            g.fillOval(getX()-2, getY()-2, 4, 4);
//        }
//
//
//    }
}
