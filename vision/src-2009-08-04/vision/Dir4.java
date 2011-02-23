package vision;

/**
 *
 * @author Kotuc
 */
public enum Dir4 {

    N(0, -1),
    E(1, 0),
    S(0, 1),
    W(-1, 0);
    final int dx,  dy;

    public final int dx() {
        return dx;
    }

    public final int dy() {
        return dy;
    }

    private Dir4(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public final Dir4 rotate(int clock) {
        return values()[(ordinal() + clock) & 3];
    }

    public final Dir4 left() {
        return rotate(3);
    }

    public final Dir4 right() {
        return rotate(1);
    }

    public final Dir4 back() {
        return rotate(2);
    }
}
