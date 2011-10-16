/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eurobot.kuba.pid;

/**
 * This class provides a single thread that drives all of the motor regulation
 * process. Only active motors will be regulated. To try and keep motors
 * as closely synchronized as possible tach counts for all motors are gathered
 * as close as possible to the same time. Similarly new power levels for each
 * motor are also set at the same time.
 */
class Controller extends Thread {

    //    static {
//        // Start the single controller thread
//        cont.setPriority(Thread.MAX_PRIORITY);
//        cont.setDaemon(true);
//        cont.start();
//    }


    static final int UPDATE_PERIOD = 4;
    MotorRegulator[] activeMotors = new MotorRegulator[0];

    /**
     * Add a motor to the set of active motors.
     * @param m
     */
    synchronized void addMotor(MotorRegulator m) {
        MotorRegulator[] newMotors = new MotorRegulator[activeMotors.length + 1];
        System.arraycopy(activeMotors, 0, newMotors, 0, activeMotors.length);
        newMotors[activeMotors.length] = m;
        m.reg.reset();
        activeMotors = newMotors;
    }

    /**
     * Remove a motor from the set of active motors.
     * @param m
     */
    synchronized void removeMotor(MotorRegulator m) {
        MotorRegulator[] newMotors = new MotorRegulator[activeMotors.length - 1];
        int j = 0;
        for (int i = 0; i < activeMotors.length; i++) {
            if (activeMotors[i] != m) {
                newMotors[j++] = activeMotors[i];
            }
        }
        activeMotors = newMotors;
    }

    @Override
    public void run() {
        long now = System.currentTimeMillis();
        for (;;) {
            long delta;
            synchronized (this) {
                delta = System.currentTimeMillis() - now;
                MotorRegulator[] motors = activeMotors;
                now += delta;
                for (MotorRegulator m : motors) {
                    m.reg.tachoCnt = m.getTachoCount();
                }
                for (MotorRegulator m : motors) {
                    m.reg.regulateMotor(delta);
                }
                for (MotorRegulator m : motors) {
                    m.controlMotor(m.reg.power, m.reg.mode);
                }

            }

//                Delay.msDelay(now + UPDATE_PERIOD - System.currentTimeMillis());

        }	// end keep going loop
    }
}
