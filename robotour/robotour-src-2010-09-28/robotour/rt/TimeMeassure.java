package robotour.rt;

/**
 *
 * @author Kotuc
 */
public abstract class TimeMeassure {

    public static TimeMeassure getTimeMeassure() {
//        return new TimeMeassure() {
//
//            private Clock clock = Clock.getRealtimeClock();
//            private AbsoluteTime startTime;
//
//            public abstract void reset() {
//                startTime = clock.getTime();
//            }
//
//            public abstract Object getTime() {
//                return clock.getTime().subtract(startTime);
//            }
//        };
        return new TimeMeassure() {

            private long startTime;

            public  void reset() {
                startTime = System.currentTimeMillis();
            }

            public long getTime() {
                return System.currentTimeMillis() - startTime;
            }
        };
    }

    private TimeMeassure() {
        reset();
    }

    public abstract void reset();

    public abstract long getTime();
}
