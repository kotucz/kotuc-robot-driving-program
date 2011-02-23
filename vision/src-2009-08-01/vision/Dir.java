package vision;

/**
 *
 * @author Kotuc
 */
public enum Dir {

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

    private Dir(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public final Dir rotate(int clock) {
        return values()[(ordinal() + clock) & 7];
    }

    public final Dir left() {
        return rotate(6);
    }

    public final Dir right() {
        return rotate(2);
    }

    public final Dir back() {
        return rotate(4);
    }


}
