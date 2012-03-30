package robotour.iface;

/**
 *
 * @author kotuc
 */
public class MeasureException extends Exception {

    private static final long serialVersionUID = 123L;

    public MeasureException() {
    }

    public MeasureException(String message) {
        super(message);
    }

    public MeasureException(Throwable cause) {
        super(cause);
    }
}
