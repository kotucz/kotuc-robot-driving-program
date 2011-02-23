package vision;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Kotuc
 */
public class ShutdownManager {

    private static List<Shutdownable> all;

    public static void registerStutdown(Shutdownable dev) {
        if (null == all) {
            Runtime.getRuntime().addShutdownHook(new Thread() {

                @Override
                public void run() {
                    System.out.println("DeviceManager: shutdownhook");
                    turnOffAll();
                    System.out.println("DeviceManager: correct shutdownhook");
                }
            });
            all = new LinkedList<Shutdownable>();
        }
        all.add(0, dev);
        System.out.println(dev + " shutdown registered");
    }

    private static synchronized void turnOffAll() {
//        body.turnOff();
//        gps.turnOff();
//        screen.turnOff();
//        video.turnOff();

        for (Shutdownable dev : all) {
            try {
                dev.shutdown();
                System.out.println(dev + "\t[OFF]");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        System.out.println("ALL Systems OFF");

    }
}
