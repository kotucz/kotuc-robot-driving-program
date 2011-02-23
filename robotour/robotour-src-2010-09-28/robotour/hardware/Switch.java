package robotour.hardware;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kotuc
 */
public class Switch {

    public enum State {

        PRESSED,
        RELEASED
    }
//    private static final boolean PRESSED = true;
//    private static final boolean RELEASED = false;
    private State state;

    public Switch() {
        this.state = null; // unknown? !!!
    }

    public Switch(State state) {
        this.state = state;
    }

    public synchronized void setState(State state) {
        if (state != this.state) {
            System.out.println(this + ": " + this.state + " -> " + state);
            this.state = state;
            notifyAll();
        }
    }

    public synchronized void waitForChange() {
        State prev = state;
        while (prev == state) {
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Switch.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public synchronized void waitForPress() {
        while (State.RELEASED.equals(state)) {
            waitForChange();
        }
    }

    public synchronized void waitForRelease() {
        while (State.PRESSED.equals(state)) {
            waitForChange();
        }
    }

    public synchronized boolean isPressed() {
        return State.PRESSED.equals(state);
    }

    public synchronized boolean isReleased() {
        return State.RELEASED.equals(state);
    }
}
