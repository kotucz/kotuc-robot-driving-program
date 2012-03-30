/*
 * Motors.java
 *
 */
package robotour.hardware.oldmotors;

import java.util.SortedMap;
import robotour.hardware.SSC32;
import robotour.util.ShutdownManager;
import robotour.util.Shutdownable;

/**
 *
 * @author PC
 */
class Motors implements Runnable, Shutdownable {

    static int STOP = 127;
    static int FULL = 0;
    static int BACK = 128;
    static final int LEFT_BANK = 0;
    static final int RIGHT_BANK = 1;
    private final SSC32 ssc32;

    public Motors(SSC32 ssc32) {
        this.ssc32 = ssc32;
        bytes = new int[128];
        speeds = new double[128];
        SortedMap<Float, Integer> table = KoscaksPatent.INSTANCE.createTable();
        int i = 0;
        for (Float float1 : table.keySet()) {
            speeds[i] = float1;
            bytes[i] = table.get(float1);
            i++;
        }
        ShutdownManager.registerStutdown(this);
    }
    private int[] bytes;
    //    static int[] bytes = new int [] {
    //        STOP, 1, 126, 14, 6, 10, 18, 34, 2, 124, 60, 28, 44, 76, 12, 32, 96, 64, FULL, FULL};
    private double[] speeds;
//     double[] speeds = new double[]{
//        0, 0.01, 0.2, 0.4, 0.6, 0.8, 1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9, 2, 2.1, 2.2, 2.3, 2.4
//    };

    private void motorTest() {

//        Device.motors.turnOn();
//        double sp = 0;
//       for (; sp < 2; sp+=0.1) {
//            Device.motors.setSpeeds(sp, sp);
//            delay();
//        }
//        for (; sp > -2; sp-=0.1) {
//            Device.motors.setSpeeds(sp, sp);
//            delay();
//        }
//        for (; sp < 0; sp+=0.1) {
//            Device.motors.setSpeeds(sp, sp);
//            delay();
//        }
        int i;
        for (i = 0; i < bytes.length; i++) {
            ssc32.setBank(LEFT_BANK, bytes[i]);
            ssc32.setBank(RIGHT_BANK, bytes[i]);
            System.out.println("speed " + i + "i " + bytes[i]);
            delay();
        }
        for (i = bytes.length - 1; i >= 0; i--) {
            ssc32.setBank(LEFT_BANK, bytes[i]);
            ssc32.setBank(RIGHT_BANK, bytes[i]);
            System.out.println("speed " + i + "i " + bytes[i]);
            delay();
        }
        for (i = 0; i < bytes.length; i++) {
            ssc32.setBank(LEFT_BANK, 128 + bytes[i]);
            ssc32.setBank(RIGHT_BANK, 128 + bytes[i]);
            System.out.println("speed " + i + "i " + (128 + bytes[i]));
            delay();
        }
        for (i = bytes.length - 1; i >= 0; i--) {
            ssc32.setBank(LEFT_BANK, 128 + bytes[i]);
            ssc32.setBank(RIGHT_BANK, 128 + bytes[i]);
            System.out.println("speed " + i + "i " + (128 + bytes[i]));
            delay();
        }

        stop();



//        double speed = 0;
//        System.out.println(" getByte() test "+speed+" mps : "+getByte(speed));
//
//        speed = 0.1;
//        System.out.println(" getByte() test "+speed+"mps : "+getByte(speed));
//
//        speed = 0.3;
//        System.out.println(" getByte() test "+speed+"mps : "+getByte(speed));
//
//        speed = 0.7;
//        System.out.println(" getByte() test "+speed+"mps : "+getByte(speed));
//
//        speed = 1.2;
//        System.out.println(" getByte() test "+speed+"mps : "+getByte(speed));
    }

    public void turbo() {
        ssc32.setBank(LEFT_BANK, FULL);
        ssc32.setBank(RIGHT_BANK, FULL);
        try {
            Thread.sleep(444);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    double targetLeft, targetRight;
    double speedLeft, speedRight;

    public void setSpeeds(int left, int right) {
        if (left < 0) {
            this.speedLeft = -speeds[-left];
            ssc32.setBank(LEFT_BANK, 128 + bytes[-left]);
        } else {
            this.speedLeft = speeds[left];
            ssc32.setBank(LEFT_BANK, bytes[left]);
        }
        if (right < 0) {
            this.speedRight = -speeds[-right];
            ssc32.setBank(RIGHT_BANK, 128 + bytes[-right]);
        } else {
            this.speedRight = speeds[right];
            ssc32.setBank(RIGHT_BANK, bytes[right]);
        }
//        System.out.println("Motors.setSpeeds()       Left: "+left+"      Right: "+right);
//        this.targetLeft = left;
//        this.targetRight = right;
    }
//    public void setSpeeds(double left, double right) {
//        this.speedLeft = left;
//        this.speedRight = right;
//        Device.ssc32.setBank(LEFT_BANK, getByte(speedLeft));
//        Device.ssc32.setBank(RIGHT_BANK, getByte(speedRight));
////        System.out.println("Motors.setSpeeds()       Left: "+left+"      Right: "+right);
////        this.targetLeft = left;
////        this.targetRight = right;
//    }

    public void stop() {
//        setSpeeds(0, 0);
        ssc32.setBank(LEFT_BANK, STOP);
        ssc32.setBank(RIGHT_BANK, STOP);
    }

    @Override
    public void shutdown() {
        stop();
        on = false;
    }
    boolean on;
    final double WHEEL_R = 0.14;
    /**
     *  revolutions per minute
     */
    final double WHEEL_MAX_RPM = 500;
    final double MAX_SPEED = Math.PI * WHEEL_R * WHEEL_MAX_RPM / 30.0;
    final int period = 20;
    final double MAX_ACC = 1.0 / period; // 1.0 = acceleration in m/s/s

    private void changeSpeedSlow() {
//
//        speedLeft = changeSpeed(speedLeft, targetLeft);
//        speedRight = changeSpeed(speedRight, targetRight);
//
//        Device.ssc32.setBank(LEFT_BANK, getByte(speedLeft));
//        Device.ssc32.setBank(RIGHT_BANK, getByte(speedRight));
//
    }

    public void run() {
//        on = true;
//        setStateInd(STATE_ON);
//        while (on) {
//
//            changeSpeedSlow();
//
//            showState(" "+speedLeft+"|-|"+speedRight+"mps");
//
//            try {
//                Thread.sleep(period);
//            } catch (InterruptedException ex) {
//                ex.printStackTrace();
//            }
//
//        }
//        stop();
//        setStateInd(STATE_OFF);
//
    }

    public double getSpeedLeft() {
        return speedLeft;
    }

    public double getSpeedRight() {
        return speedRight;
    }

    private int getByte(double speed) {

//        System.out.println("getbyte");

        double abs = Math.abs(speed);

        int i = 0;
        while (abs > speeds[i]) {
//            System.out.println(""+abs+" > "+speeds[i]+" ["+i+"]");
            i++;
        }
        int value = bytes[i];

        if (speed < 0) {
            value += 128;
        }

        return value;

    }

    private double changeSpeed(double speed, double target) {

        if (Math.abs(speed - target) < MAX_ACC) {
            return target;
        }
        if ((speed - target) > MAX_ACC) {
            return speed - MAX_ACC;
        }
        if ((target - speed) > MAX_ACC) {
            return speed + MAX_ACC;
        }

        System.err.println("Motors: change speed ! unpredictable situation speed " + speed + " target " + target);
        return speed;

    }

    private void delay() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//
////        new Motors().motorTest();
//
////        double speed = 0;
////        System.out.println(" getByte() test "+speed+" mps : "+getByte(speed));
////
////        speed = 0.1;
////        System.out.println(" getByte() test "+speed+"mps : "+getByte(speed));
////
////        speed = 0.3;
////        System.out.println(" getByte() test "+speed+"mps : "+getByte(speed));
////
////        speed = 0.7;
////        System.out.println(" getByte() test "+speed+"mps : "+getByte(speed));
////
////        speed = 1.2;
////        System.out.println(" getByte() test "+speed+"mps : "+getByte(speed));
//
//
//        System.exit(0);
//    }
}
