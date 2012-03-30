package robotour.iface;

/**
 *
 * @author Kotuc
 */
public interface Servo {
    /**
     * Requests Servo to move to the specified position
     * -1, 1 are extremal positions
     * @param position in interval -1 .. 1
     */
    void setPosition(double position);
}
