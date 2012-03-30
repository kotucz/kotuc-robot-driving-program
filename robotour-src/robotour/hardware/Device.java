package robotour.hardware;

import java.util.logging.Logger;

/**
 *
 * @author Tomas Kotula
 * @deprecated
 */
abstract class Device {

    private final String deviceName;

    public Device(String name) {
        this.deviceName = name;
        setStateInd(STATE_OFF);
        logger = Logger.getLogger(name);
    }

    public String getName() {
        return deviceName;
    }    // public JPanel devicePanel;
    private String stateText = "state";
    private final Logger logger;

    protected Logger getLogger() {
        return logger;
    }
    private int state = 0;
    public static final int STATE_OFF = 0;
    public static final int STATE_BEFORE_ON = 1;
    public static final int STATE_ON = 2;
    public static final int STATE_BEFORE_OFF = 3;
    public static final int STATE_NORMAL = 4;
    public static final int STATE_WARNING = 5;
    public static final int STATE_ERROR = 6;

    protected void setStateInd(int i) {
    }

    protected void showState(String st) {
    }

    public int getState() {
        return this.state;
    }

    public String getStateText() {
        return this.stateText;
    }

    @Override
    public String toString() {
        return this.deviceName + "[" + this.stateText + "]";
    }
}
