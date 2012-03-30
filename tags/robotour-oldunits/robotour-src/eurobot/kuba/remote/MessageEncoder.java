package eurobot.kuba.remote;

/**
 *
 * @author Kotuc
 */
public class MessageEncoder implements RemoteMethods {

    StringMessageListener message;

    public MessageEncoder(StringMessageListener message) {
        this.message = message;
    }

    public void goTo(double mmx, double mmy) {
        message.message("goto "+mmx+" "+mmy);
    }

    public void updatePose(double mmx, double mmy, double azdegs) {
        message.message("at "+mmx+" "+mmy+" "+azdegs);
    }

}
