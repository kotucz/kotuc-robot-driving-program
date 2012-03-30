package eurobot.kuba;

//simport state.State;

/**
 *
 * @author Kotuc
 */
public class StateCommandExecutor /*implements Runnable*/ {
//
////    final State st = new State();
//    final Pose pose = new Pose(new Point(0, 0), Azimuth.valueOfDegrees(100));
//    Point dest = new Point(0, 0);
//    Command cmd = Command.READY;
//
//    final KubaPuppet puppet;
//
//    public StateCommandExecutor(KubaPuppet puppet) {
//        this.puppet = puppet;
//    }
//
//    static enum Command {
//
//        READY, TAKE;
//    }
//
//    void goTo(Point dest) {
//        double dist = pose.getPoint().getDistanceTo(dest);
//        if (dist>0.01) {
//            Angle angleR = pose.angleTo(dest);
//            if (angleR.degrees()>5) {
//                puppet.turnR(angleR);
//                return;
//            }
//            puppet.forward(dist);
//            return;
//        }
//    }
//
//    public void run() {
//        try {
//            while (true) {
//                readCommand();
//                switch (cmd) {
//                    case READY:
//                        break;
//                    case TAKE:
//                        break;
//                    default:
//                        System.out.println("Undefined command: " + cmd);
//                }
//                puppet.out.readOdometry();
//
//                    //            if (st.query("test")) {
//                    //                System.out.println(st.get());
//                    //            }
//                    //
//                    //            st.set("test", "value");
//                    //            st.set("test", 0.32);
//                    //
//                    //            if (st.query("test")) {
//                    //                System.out.println(st.get());
//                    //            }
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(StateCommandExecutor.class.getName()).log(Level.SEVERE, null, ex);
//                }
//
//            }
//        } finally {
//            st.close();
//        }
//    }
//
//    boolean readCommand() {
//        if (st.query("cmd")) {
//            String[] split = st.get().split(" ");
//            Command cm = Command.valueOf(split[0]);
//            double x = Double.parseDouble(split[1]);
//            double y = Double.parseDouble(split[2]);
//            Azimuth.valueOfDegrees(Double.valueOf(split[3]));
////            switch (cm) {
////                case READY:
////                    break;
////            }
////            System.out.println();
//            return true;
//        }
//        return false;
//    }
}
