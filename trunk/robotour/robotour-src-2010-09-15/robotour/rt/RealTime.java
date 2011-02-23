package robotour.rt;

/**
 *
 * @author Kotuc
 */
public class RealTime {

    static final boolean realtimeEnabled = false;

    public static boolean isRealtimeEnabled() {
        return realtimeEnabled;
    }

    public static void sleep(long millis, int nanos) throws InterruptedException {
        
//        if (realtimeEnabled) {
//            RealtimeThread.sleep(new RelativeTime(millis, nanos));
//        } else {
            Thread.sleep(millis, nanos);
//        }
    }

    public static Thread newThread(final Runnable runnable, int priority) {
//        if (realtimeEnabled) {
//            return new RealtimeThread(new PriorityParameters(priority)) {
//
//                @Override
//                public void run() {
//                    runnable.run();
//                }
//            };
//        } else {
            return new Thread(runnable);
//        }
    }

//    public static void main(String[] args) throws InterruptedException {
//        System.out.println("pred");
//        Clock clock = Clock.getRealtimeClock();
//
//        RealTime.newThread(new Runnable() {
//
//            public void run() {
//                int i = 0;
//                for (int idx = 0; idx < 1000000; idx++) {
//                    i++;
//                }
//                System.out.println("I " + i);
//            }
//        }, 20).start();
//
//
//        AbsoluteTime time = clock.getTime();
//        RealTime.sleep(14, 556143);
//        System.out.println("dur " + clock.getTime().subtract(time));
//
//
//        System.out.println("po");
//    }
}
