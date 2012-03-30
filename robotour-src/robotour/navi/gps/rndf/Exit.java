package robotour.navi.gps.rndf;

/**
 *
 * @author Kotuc
 */
public class Exit {

    private WPID from;
    private WPID to;

    public Exit(WPID from, WPID to) {
        this.from = from;
        this.to = to;
    }

    public WPID from() {
        return from;
    }

    public WPID to() {
        return to;
    }
}
