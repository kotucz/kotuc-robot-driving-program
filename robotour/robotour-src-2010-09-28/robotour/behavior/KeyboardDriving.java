package robotour.behavior;

import robotour.util.RobotSystems;
import robotour.iface.Wheels;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import robotour.hardware.ControllerTools;

/**
 *
 * @author Tomas
 */
public class KeyboardDriving implements Behavior {

    private final Controller keyboard;
    private final Wheels wheels;
    private Component forwardKey;
    private Component backwardKey;
    private Component rightKey;
    private Component leftKey;
    private Component stopKey;
    // factor of influence of arrows on speed
    final double factor = 0.2;
    double speed = 0.0;
    double steer = 0.0;

    public KeyboardDriving(Wheels wheels) {
        this(wheels, ControllerTools.getKeyboard());
    }

    public KeyboardDriving(Wheels wheels, Controller keyboard) {
        if (wheels == null) {
            throw new NullPointerException("wheels");
        }
        if (keyboard == null) {
            throw new NullPointerException("gamepad");
        }
        this.keyboard = keyboard;
        this.wheels = wheels;

        findComponents();

        System.out.println("Keyboard driving ready.");
    }

    private void findComponents() {

        for (Component component : keyboard.getComponents()) {

            if ("Up".equals(component.getIdentifier().getName())) {
                forwardKey = component;
            } else if ("Down".equals(component.getIdentifier().getName())) {
                backwardKey = component;
            } else if ("Right".equals(component.getIdentifier().getName())) {
                rightKey = component;
            } else if ("Left".equals(component.getIdentifier().getName())) {
                leftKey = component;
            } else if (" ".equals(component.getIdentifier().getName())) {
                stopKey = component;
            }

        }

    }

    public boolean act() {
        keyboard.poll();

        speed = (1 - factor) * speed + factor * (forwardKey.getPollData() - backwardKey.getPollData());

        steer = (1 - factor) * steer + factor * (rightKey.getPollData() - leftKey.getPollData());

        

        if (stopKey.getPollData() > 0.5) {
            wheels.stop();
            speed = 0;
            steer = 0;
        } else {

            if (Math.abs(speed) < 0.05) {
                speed = 0;
            }
            if (Math.abs(steer) < 0.05) {
                steer = 0;
            }

            System.out.println("Keyboard " + speed + " " + steer);

            wheels.setSpeed(speed);
//        wheels.setSteer(steer * speed);

            wheels.setSteer(steer);
        }
//            Logger.getLogger("XPadDriving").log(Level.INFO, "speed: " + speed + "; steer: " + steer);
        return true;

    }

    public static void main(String[] args) {
        new Arbitrator(new KeyboardDriving(RobotSystems.getDefault().getWheels())).start();
    }
}
