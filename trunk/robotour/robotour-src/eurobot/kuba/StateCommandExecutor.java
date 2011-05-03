package eurobot.kuba;

import robotour.gui.map.LocalPoint;
import robotour.navi.basic.Azimuth;
import robotour.navi.basic.RobotPose;
import state.State;

/**
 *
 * @author Kotuc
 */
public class StateCommandExecutor implements Runnable {

    

    final State st = new State();
    final RobotPose pose = new RobotPose(new LocalPoint(0, 0), Azimuth.valueOfDegrees(100));
    LocalPoint dest = new LocalPoint(0, 0);
    Command cmd = Command.READY;

    final KubaPuppet puppet;

    public StateCommandExecutor(KubaPuppet puppet) {
        this.puppet = puppet;
    }

    static enum Command {

        READY, TAKE;
    }

    public void run() {
        try {
            while (true) {
                switch (cmd) {
                    case READY:
                        break;
                    case TAKE:
                        break;
                    default:
                        System.out.println("Undefined command: " + cmd);
                }

//            if (st.query("test")) {
//                System.out.println(st.get());
//            }
//
//            st.set("test", "value");
//            st.set("test", 0.32);
//
//            if (st.query("test")) {
//                System.out.println(st.get());
//            }

            }
        } finally {
            st.close();
        }
    }

    boolean readCommand() {
        if (st.query("cmd")) {
            String[] split = st.get().split(" ");
            Command cm = Command.valueOf(split[0]);
            double x = Double.parseDouble(split[1]);
            double y = Double.parseDouble(split[2]);
            Azimuth.valueOfDegrees(Double.valueOf(split[3]));
//            switch (cm) {
//                case READY:
//                    break;
//            }
//            System.out.println();
            return true;
        }
        return false;
    }
}
