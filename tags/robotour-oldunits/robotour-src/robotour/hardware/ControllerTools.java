package robotour.hardware;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;

/**
 *
 * @author Kotuc
 */
public final class ControllerTools {

    // no instances
    private ControllerTools() {
    }

    public static Controller getKeyboard() {
        Controller[] conts = ControllerEnvironment.getDefaultEnvironment().getControllers();

        return conts[1];
    }

    public static Controller getActiveGamePad() {
        Controller gamepad = null;
        Event event = new Event();
        System.err.println("PRESS ANY KEY ON CONTROLLER");
        while (null == gamepad) {
            for (Controller controller : ControllerEnvironment.getDefaultEnvironment().getControllers()) {
                System.out.println("controler: " + controller);
                controller.poll();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ControllerTools.class.getName()).log(Level.SEVERE, null, ex);
                }
                controller.poll();
                if (controller.getEventQueue().getNextEvent(event)) {
                    gamepad = controller;
                }
            }

        }
//        return gamepad;
        return gamepad;
    }

//    public static Controller getGamePad() {
//    }
    private static void printComponentsList(Controller controller) {
        System.out.println("Listing " + controller.getName() + " components: ");
        for (Component component : controller.getComponents()) {
            System.out.println("" + component.getName()
                    + " id: \"" + component.getIdentifier().getName()
                    + "\"\t" + ((component.isAnalog()) ? "anal" : "digi")
                    + ": " + component.getPollData());
        }
    }

    public static void main(String[] args) {
        Controller[] conts = ControllerEnvironment.getDefaultEnvironment().getControllers();

        int i = 0;
        System.out.println("Listing controllers:");
        for (Controller controller : conts) {
            System.out.println(i + ": " + controller.getName() + ":  " + controller.getType());
            i++;
        }

        printComponentsList(conts[1]);

    }
}
