package robotour.util;

import robotour.iface.Servo;
import robotour.iface.Wheels;
import robotour.iface.Tachometer;
import robotour.hardware.SSC32;

/**
 *
 * @author Kotuc
 */
public class SscWheels implements Wheels, Tachometer {

    private final SSC32 ssc;
    private final Servo speedServo;
    private final Servo steerServo;
    private volatile double speed;
    private volatile double steer;
    private final static int SPEED_SERVO_CHANNEL = 1+16;
    private final static int STEER_SERVO_CHANNEL = 0+16;

    public SscWheels(SSC32 ssc) {
        if (ssc == null) {
            throw new NullPointerException("ssc");
        }
        this.ssc = ssc;
        this.speedServo = ssc.getServo(SPEED_SERVO_CHANNEL);
        this.steerServo = ssc.getServo(STEER_SERVO_CHANNEL);
        stop();
        ssc.registerShutdownable(new Shutdownable() {
            @Override
            public synchronized void shutdown() {
                stop();
            }
        });
    }

    /**
     * 1 .. max forward
     * 0 .. stop
     * -1 .. max backward
     * @param speed
     */
    public synchronized void setSpeed(double speed) {
        this.speed = speed;
        speedServo.setPosition(-speed); // invertet so positive is forward
//        int sp = (int) (1500 - 500 * speed);
//        ssc.setServo(SPEED_SERVO_CHANNEL, sp);
////                        ssc32.setServo(8, (int) (1500 + 1000 * component.getPollData()));
//        System.out.println("speed:\t" + sp);
    }

    /**
     * 
     * 1 .. max right
     * 0 .. straight
     * -1 .. max left
     * 
     * @param steer
     */
    public synchronized void setSteer(double steer) {
        this.steer = steer;
        steerServo.setPosition(steer);
//        int st = (int) (1500 + 500 * steer);
//        ssc.setServo(STEER_SERVO_CHANNEL, st);
//        System.out.println("steer:\t" + st);
    }

    public synchronized double getSpeed() {
        return speed;
    }

    public synchronized double getSteer() {
        return steer;
    }

    public synchronized void stop() {
        this.setSpeed(0);
        this.setSteer(0);
    }
}
