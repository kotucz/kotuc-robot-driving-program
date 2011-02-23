package vision;

/**
 *
 * @author Kotuc
 */
public enum Dir8 {

    N(0, -1),
    NE(1, -1),
    E(1, 0),
    SE(1, 1),
    S(0, 1),
    SW(-1, 1),
    W(-1, 0),
    NW(-1, 1);
    final int dx,  dy;

    public final int dx() {
        return dx;
    }

    public final int dy() {
        return dy;
    }

    private Dir8(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public final Dir8 rotate(int clock) {
        return values()[(ordinal() + clock) & 7];
    }

    public final Dir8 left() {
        return rotate(6);
    }

    public final Dir8 right() {
        return rotate(2);
    }

    public final Dir8 back() {
        return rotate(4);
    }


}
