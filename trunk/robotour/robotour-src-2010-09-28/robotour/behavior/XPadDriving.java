package robotour.behavior;

import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.util.RobotSystems;
import robotour.iface.Wheels;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import robotour.hardware.ControllerTools;

/**
 *
 * @author Tomas
 */
public class XPadDriving implements Behavior {

    private final Controller gamepad;
    private final Wheels wheels;
    private Component xcomponent;
    private Component ycomponent;

    public XPadDriving(Wheels wheels) {
        this(wheels, ControllerTools.getActiveGamePad());
    }

    public XPadDriving(Wheels wheels, Controller gamepad) {
        if (wheels == null) {
            throw new NullPointerException("wheels");
        }
        if (gamepad == null) {
            throw new NullPointerException("gamepad");
        }
        this.gamepad = gamepad;
        this.wheels = wheels;
        findComponents();
    }

    private void findComponents() {

        for (Component component : gamepad.getComponents()) {

            if ("x".equals(component.getIdentifier().getName())) {
                xcomponent = component;
            } else if ("y".equals(component.getIdentifier().getName())) {
                ycomponent = component;
            }

        }

    }

    public boolean act() {
        gamepad.poll();
        double speed = 0.0;
        double steer = 0.0;

        // ackerman
//                    steer = Math.signum(speed) *component.getPollData();
        // differential
        steer = xcomponent.getPollData();


        speed = -ycomponent.getPollData();


        if (Math.abs(steer) < 0.05) {
            steer = 0;
        }

        if (Math.abs(speed) < 0.05) {
            speed = 0;
        }

        wheels.setSpeed(speed);
//        wheels.setSteer(steer * speed);
        wheels.setSteer(steer);
        try {
//            Logger.getLogger("XPadDriving").log(Level.INFO, "speed: " + speed + "; steer: " + steer);
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(XPadDriving.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;

    }

    public static void main(String[] args) {
        new Arbitrator(new XPadDriving(RobotSystems.getDefault().getWheels())).start();
    }
}
