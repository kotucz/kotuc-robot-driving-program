package robotour.behavior;

/**
 *
 * @author Kotuc
 */
public class Arbitrator extends Thread {

    private Behavior[] behs;

    public Arbitrator(Behavior... behs) {
        this.behs = behs;
    }

//    public void run() {
//        while (true) {
//            for (Behavior beh : behs) {
//                if (beh.act()) {
//                    break;
//                }
//            }
//        }
//    }
    @Override
    public void run() {
//        new Thread(this).start();
        while (true) {
            for (Behavior beh : behs) {
                if (beh.act()) {
                    break;
                }
            }
        }
    }
}
