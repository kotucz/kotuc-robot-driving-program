package eurobot.kuba.remote;

/**
 *
 * @author Kotuc
 */
public interface RemoteMethods {
    void goTo(double mmx, double mmy);
    void updatePose(double mmx, double mmy, double azdegs);
}
