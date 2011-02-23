package robotour.behavior;

import robotour.hardware.Switch;
import robotour.iface.Wheels;

/**
 *
 * @author Kotuc
 */
public class StartingProcedure {

    private final Switch swit;
    private final Wheels wheels;
    private final Behavior behavior;

    private StartingProcedure(Wheels wheels, Switch swit, Behavior behavior) {
        this.swit = swit;
        this.behavior = behavior;
        this.wheels = wheels;
    }
    private long runMillis = 0;

//    public void run() {
//        System.out.println("Waiting for release");
//        swit.waitForRelease();
//        runnable.run();
//    }
    public void run() {
        while (true) {
            System.out.println("Waiting for release");
            swit.waitForRelease();
            while (swit.isReleased()) {
                behavior.act();
            }

            wheels.stop();
            swit.waitForRelease();
        }

    }

    public void runEurobot() {
        for (int i = 10; i >
                0; i--) {
            try {
                System.out.println("" + i + " seconds to start");
                wheels.stop();
                Thread.sleep(1000); // 30 s to start
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

        }
        System.out.println("" + 0 + " seconds. Starting now!");
        long startt = System.currentTimeMillis(); // 90 s to end
        while ((runMillis = (System.currentTimeMillis() - startt)) < 90 * 1000) {
            behavior.act();
        }

        wheels.stop();

        System.exit(0);
    }

    protected long runTime() {
        return runMillis;
    }
}
