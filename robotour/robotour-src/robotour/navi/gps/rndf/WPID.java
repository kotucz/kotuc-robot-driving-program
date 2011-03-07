/*
 * WPID.java
 *
 * Created on 14. srpen 2006, 0:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package robotour.navi.gps.rndf;

/**
 *
 * @author PC
 */
public class WPID {

    private final String id;
    private final int seg;
    private final int lane;
    private final int wp;

    /** Creates a new instance of WPID */
    private WPID(String id) {
        try {
        this.id = id;
        String[] nums = id.split("\\.");
        this.seg = Integer.parseInt(nums[0]);
        this.lane = Integer.parseInt(nums[1]);
        this.wp = Integer.parseInt(nums[2]);
        } catch (Exception ex) {
            throw new IllegalArgumentException("illegal wpid: \""+id+"\"", ex);
        }
    }

    public static WPID valueOf(String id) {
        return new WPID(id);
    }

    public int getSegment() {
        return seg;
    }

    public int getLane() {
        return lane;
    }

    public int getWaypoint() {
        return wp;
    }

    @Override
    public String toString() {
        return this.id;
    }
}
