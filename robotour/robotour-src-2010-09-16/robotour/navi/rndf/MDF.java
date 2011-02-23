package robotour.navi.rndf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;

/**
 * help class for loading path from MDF file
 */
class MDF {

    int num_checkpoints;
    int[] chpIds;
    ArrayList<WPID> chppath;
    private LinkedList<Waypoint> path;
    RNDFMap outer;

    MDF(RNDFMap outer) {
        super();
        this.outer = outer;
    }

    void setNumCheckpoints(int n) {
        num_checkpoints = n;
        chpIds = new int[n];
    }

    public void loadCheckpoints(String mdffile) {
//        outer.getLogger().log(Level.SEVERE, "loading MDF " + mdffile);
//        chppath = new ArrayList<WPID>();
//        File fin = new File(mdffile);
//        LineNumberReader lnr = null;
//        try {
//            lnr = new LineNumberReader(new FileReader(fin));
//        } catch (FileNotFoundException ex) {
//            ex.printStackTrace();
//            return;
//        }
//        try {
//            String line = null;
//            while ((line = lnr.readLine()) != null) {
//                String[] params = line.split(" ");
//                String type = params[0];
//                if (type.equals("num_checkpoints")) {
//                    setNumCheckpoints(Integer.parseInt(params[1]));
//                    for (int i = 0; i < num_checkpoints; i++) {
//                        chpIds[i] = Integer.parseInt(lnr.readLine().split(" ")[0]);
//                        chppath.add(outer.checkpoints.get(chpIds[i]));
//                    }
//                } else if (type.equals("end_checkpoints")) {
//                    outer.getLogger().log(Level.SEVERE, "CHECKPOINTS SUCCESSFUL OTHER IGNORING");
//                    return;
//                } else if (type.equals("end_file")) {
//                    outer.getLogger().log(Level.SEVERE, "SUCCESSFUL");
//                    return;
//                } else if (type.equals("speed_limits")) {
//                } else if (type.equals("end_speed_limits")) {
//                } else {
//                    outer.getLogger().log(Level.SEVERE, "unread " + line);
//                }
//            }
//        } catch (IOException ex) {
//            outer.getLogger().log(Level.SEVERE, "Line number " + lnr.getLineNumber());
//            ex.printStackTrace();
//        }
//        outer.getLogger().log(Level.SEVERE, "FINISHED EOF NOT FOUND!");
    }
}
