package robotour.behavior;

/**
 *
 * @author Kotuc
 */
public interface Behavior {

    /**
     *
     * @return true if the behavior was activated succsesfully and took the lead.
     * Stops following behaviors.
     */
    public boolean act();

}
