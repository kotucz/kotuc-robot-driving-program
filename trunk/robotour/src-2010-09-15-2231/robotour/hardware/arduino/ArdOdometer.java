package robotour.hardware.arduino;

/**
 *
 * @author Kotuc
 */
public class ArdOdometer {

    private short lastticks;
    private volatile short ticks;

    void setTicks(short ticks) {
        this.ticks = ticks;
    }

    public short getTicks() {
        return ticks;
    }

    public int getChange() {
        short mem = ticks;

        int res = (mem - lastticks);
        this.lastticks = mem;
        
        return res;
    }


}
