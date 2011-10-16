package eurobot.kuba.pid;

/**
 * Abstraction for a NXT motor.
 * The basic control methods are:
 *  <code>forward, backward, reverseDirection, stop</code>
 * and <code>flt</code>. To set each motor's velocity, use {@link #setSpeed(int)
 * <code>setSpeed  </code> }.
 * The maximum velocity of the motor is limited by the battery voltage and load.
 * With no load, the maximum degrees per second is about 100 times the voltage.  <br>
 * The velocity is regulated by comparing the tacho count with velocity times elapsed
 * time, and adjusting motor power to keep these closely matched. Changes in velocity
 * will be made at the rate specified via the
 * <code> setAcceleration(int acceleration)</code> method.
 * The methods <code>rotate(int angle) </code> and <code>rotateTo(int ange)</code>
 * use the tachometer to control the position at which the motor stops, usually within 1 degree
 * or 2.<br>
 *  <br> <b> Listeners.</b>  An object implementing the {@link lejos.robotics.RegulatedMotorListener
 * <code> RegulatedMotorListener </code> } interface  may register with this class.
 * It will be informed each time the motor starts or stops.
 * <br> <b>Stall detection</b> If a stall is detected, the motor will stop, and
 * <code>isStalled()</code >  returns <b>true</b>.
 * <br>Motors will hold thier position when stopped. If this is not what you require use
 * the flt() method instead of stop().
 * <br>
 * <p>
 * Example:<p>
 * <code><pre>
 *   Motor1.A.setSpeed(720);// 2 RPM
 *   Motor1.C.setSpeed(720);
 *   Motor1.A.forward();
 *   Motor1.C.forward();
 *   Thread.sleep (1000);
 *   Motor1.A.stop();
 *   Motor1.C.stop();
 *   Motor1.A.rotateTo( 360);
 *   Motor1.A.rotate(-720,true);
 *   while(Motor1.A.isRotating() :Thread.yield();
 *   int angle = Motor1.A.getTachoCount(); // should be -360
 *   LCD.drawInt(angle,0,0);
 * </pre></code>
 * @author Roger Glassey/Andy Shaw
 * from lego Lejos nxt NXTRegulatedMotor
 */
public class MotorRegulator {

    static public final int PWM_FLOAT = 0;
    static public final int PWM_BRAKE = 1;
    public final static int FORWARD = 1;
    public final static int BACKWARD = 2;
    public final static int STOP = 3;
    public final static int FLOAT = 4;
    public final static int MAX_POWER = 100;
    
    protected static final int NO_LIMIT = 0x7fffffff;
    final Regulator reg;
//    protected TachoMotorPort tachoPort;
    protected boolean stalled = false;
//    protected RegulatedMotorListener listener = null;
    protected int speed = 360;
    protected int acceleration = 6000;
    protected int limitAngle = 0;
    protected int stallLimit = 50;
    protected int stallCnt = 0;
//    protected static final Controller cont = new Controller();



    /**
     * Use this constructor to assign a variable of type motor connected to a particular port.
     * @param port  to which this motor is connected
     */
    public MotorRegulator() {
//        tachoPort = port;
//        port.setPWMMode(TachoMotorPort.PWM_BRAKE);
        reg = new Regulator();
    }

    /**
     * @return the current tachometer count.
     * @see lejos.robotics.RegulatedMotor#getTachoCount()
     */
    public int getTachoCount() {
        // TODO connect encoder
        return 0;
    }

    /**
     * Returns the current position that the motor regulator is trying to
     * maintain. Normally this will be the actual position of the motor and will
     * be the same as the value returned by getTachoCount(). However in some
     * circumstances (activeMotors that are in the process of stalling, or activeMotors
     * that have been forced out of position), the two values may differ. Note
     * this value is not valid if regulation has been terminated.
     * @return the current position calculated by the regulator.
     */
    public int getPosition() {
        return Math.round(reg.curCnt);
    }

    /**
     * @see lejos.nxt.BasicMotor#forward()
     */
    public void forward() {
        reg.newMove(speed, acceleration, +NO_LIMIT, true);
    }

    /**
     * @see lejos.nxt.BasicMotor#backward()
     */
    public void backward() {
        reg.newMove(speed, acceleration, -NO_LIMIT, true);
    }

    /**
     * Set the motor into float mode. This will stop the motor without braking
     * and the position of the motor will not be maintained.
     */
    public void floatMode() {
        reg.newMove(0, acceleration, NO_LIMIT, false);
    }

    /**
     * Causes motor to stop, pretty much
     * instantaneously. In other words, the
     * motor doesn't just stop; it will resist
     * any further motion.
     * Cancels any rotate() orders in progress
     */
    public void stop() {
        reg.newMove(0, acceleration, NO_LIMIT, true);
    }

    /**
     * This method returns <b>true </b> if the motor is attempting to rotate.
     * The return value may not correspond to the actual motor movement.<br>
     * For example,  If the motor is stalled, isMoving()  will return <b> true. </b><br>
     * After flt() is called, this method will return  <b>false</b> even though the motor
     * axle may continue to rotate by inertia.
     * If the motor is stalled, isMoving()  will return <b> true. </b> . A stall can
     * be detected  by calling {@link #isStalled()};
     * @return true iff the motor if the motor is attempting to rotate.<br>
     */
    public boolean isMoving() {
        return reg.moving;
    }

    public void rotateTo(int limitAngle) {
        reg.newMove(speed, acceleration, limitAngle, true);
    }

    /**
     * Sets desired motor speed , in degrees per second;
     * The maximum reliably sustainable velocity is  100 x battery voltage under
     * moderate load, such as a direct drive robot on the level.
     * If the parameter is larger than that, the maximum sustainable value will
     * be used instead.
     * @param speed value in degrees/sec
     */
    public void setSpeed(int speed) {
        this.speed = speed;
        reg.adjustSpeed(speed);
    }

    /**
     * sets the acceleration rate of this motor in degrees/sec/sec <br>
     * The default value is 1000; Smaller values will make speeding up. or stopping
     * at the end of a rotate() task, smoother;
     * @param acceleration
     */
    public void setAcceleration(int acceleration) {
        this.acceleration = Math.abs(acceleration);
        reg.adjustAcceleration(this.acceleration);
    }

    /**
     * returns acceleration in degrees/second/second
     * @return the value of acceleration
     */
    public int getAcceleration() {
        return acceleration;
    }

    /**
     * Return the angle that this Motor is rotating to.
     * @return angle in degrees
     */
    public int getLimitAngle() {
        return limitAngle;
    }

    /**
     * Reset the tachometer associated with this motor. Note calling this method
     * will cause any current move operation to be halted.
     */
    public void resetTachoCount() {
        synchronized (reg) {
            // Make sure we are stopped!
            reg.newMove(0, acceleration, NO_LIMIT, true);
            resetTachoCount();
            reg.reset();
        }
    }

//    /**
//     * Add a motor listener. Move operations will be reported to this object.
//     * @param listener
//     */
//    public void addListener(RegulatedMotorListener listener)
//    {
//        this.listener = listener;
//    }
    /**
     * Rotate by the request number of degrees.
     * @param angle number of degrees to rotate relative to the current position
     * @param immediateReturn if true do not wait for the move to complete
     */
    public void rotate(int angle) {
        synchronized (reg) {
            rotateTo(Math.round(reg.curCnt) + angle);
        }
    }

    /**
     * Return the current target speed.
     * @return the current target speed.
     */
    public int getSpeed() {
        return Math.round(speed);
    }

    /**
     * Return true if the motor is currently stalled.
     * @return
     */
    public boolean isStalled() {
        return stalled;
    }

    /**
     * Return the current velocity.
     * @return current velocity in degrees/s
     */
    public int getRotationSpeed() {
        return Math.round(reg.curVelocity);
    }

    void controlMotor(int power, int mode) {
        // send power to motor..and set mode??
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Inner class to regulate velocity; also stop motor at desired rotation angle.
     * This class uses a very simple movement model based on simple linear
     * acceleration. This model is used to generate ideal target positions which
     * are then used to generate error terms between the actual and target position
     * this error term is then used to drive a PID style motor controller to
     * regulate the power supplied to the motor.
     *
     * If new command are issued while a move is in progress, the new command
     * is blended with the current one to provide smooth movement.
     *
     * If the requested speed is not possible then the controller will simply
     * drop move cycles until the motor catches up with the ideal position. If
     * too many consecutive dropped moves are required then the motor is viewed
     * to have stalled and the move is terminated.
     *
     * Once the motor stops, the final position is held using the same PID control
     * mechanism (with slightly different parameters), as that used for movement.
     **/
    protected class Regulator {
        // PID constants for move and for hold
        // Old values
        //static final float MOVE_P = 4f;
        //static final float MOVE_I = 0.04f;
        //static final float MOVE_D = 32f;
        // New values
        //static final float MOVE_P = 7f;

        static final float MOVE_P = 6f;
        static final float MOVE_I = 0.04f;
        static final float MOVE_D = 22f;
        static final float HOLD_P = 2f;
        static final float HOLD_I = 0.04f;
        static final float HOLD_D = 8f;
        float basePower = 0; //used to calculate power
        float err1 = 0; // used in smoothing
        float err2 = 0; // used in smoothing
        float curVelocity = 0; // expected velocity now
        float baseVelocity = 0; // velocity at the begining of acceleration phase
        float baseCnt = 0; // start of acceleration tachometer count
        float curCnt = 0; // expected current tachometer count
        float curAcc = 0; // current acceleration
        float curTargetVelocity = 0; // end of acceleration velocity
        int curLimit = NO_LIMIT; // target tacometer count
        boolean curHold = true; // flag if should hold
        float accCnt = 0; // distance of current acceleration phase
        long baseTime = 0; // start time of acceleration phase
        long now = 0; // now
        long accTime = 0; // end of acceleration phase
        boolean moving = false;
        boolean pending = false;
        boolean checkLimit = false;
        float newSpeed = 0;
        int newAcceleration = 0;
        int newLimit = 0;
        boolean newHold = true;
        int tachoCnt;
        public int power;
        int mode;
        boolean active = false;

        /**
         * Reset the tachometer readings
         */
        synchronized void reset() {
            tachoCnt = getTachoCount();
            curCnt = tachoCnt;
            now = System.currentTimeMillis();
        }

        /**
         * Helper method. Start a sub move operation. A sub move consists
         * of acceleration/deceleration to a set velocity and then holding that
         * velocity up to an optional limit point. If a limit point is set this
         * method will be called again to initiate a controlled deceleration
         * to that point
         * @param speed
         * @param acceleration
         * @param limit
         * @param hold
         */
        synchronized private void startSubMove(float speed, float acceleration, int limit, boolean hold) {
            float absAcc = Math.abs(acceleration);
            checkLimit = Math.abs(limit) != NO_LIMIT;
            baseTime = now;
            curTargetVelocity = (limit - curCnt >= 0 ? speed : -speed);
            curAcc = curTargetVelocity - curVelocity >= 0 ? absAcc : -absAcc;
            accTime = Math.round(((curTargetVelocity - curVelocity) / curAcc) * 1000);
            accCnt = (curVelocity + curTargetVelocity) * accTime / (2 * 1000);
            baseCnt = curCnt;
            baseVelocity = curVelocity;
            curHold = hold;
            curLimit = limit;
            moving = curTargetVelocity != 0 || baseVelocity != 0;
        }

        /**
         * Initiate a new move and optionally wait for it to complete.
         * If some other move is currently executing then ensure that this move
         * is terminated correctly and then start the new move operation.
         * @param speed
         * @param acceleration
         * @param limit
         * @param hold
         * @param waitComplete
         */
        synchronized public void newMove(float speed, int acceleration, int limit, boolean hold) {
            // ditch any existing pending command
            pending = false;
            // Stop moves always happen now
            if (speed == 0) {
                startSubMove(0, acceleration, NO_LIMIT, hold);
            } else if (!moving) {
                // not moving so we start a new move
                startSubMove(speed, acceleration, limit, hold);
//                updateState(Math.round(curTargetVelocity), hold, false);
            } else {
                // we already have a move in progress can we modify it to match
                // the new request? We must ensure that the new move is in the
                // same direction and that any stop will not exceed the current
                // acceleration request.
                float moveLen = limit - curCnt;
                float acc = (curVelocity * curVelocity) / (2 * (moveLen));
                if (moveLen * curVelocity >= 0 && Math.abs(acc) <= acceleration) {
                    startSubMove(speed, acceleration, limit, hold);
                } else {
                    // Save the requested move
                    newSpeed = speed;
                    newAcceleration = acceleration;
                    newLimit = limit;
                    newHold = hold;
                    pending = true;
                    // stop the current move
                    startSubMove(0, acceleration, NO_LIMIT, true);
                    // If we need to wait for the existing command to end
                   
                }
            }
    
        }

        /**
         * The target speed has been changed. Reflect this change in the
         * regulator.
         * @param newSpeed new target speed.
         */
        synchronized void adjustSpeed(float newSpeed) {
            if (curTargetVelocity != 0) {
                startSubMove(newSpeed, curAcc, curLimit, curHold);
            }
            if (pending) {
                this.newSpeed = newSpeed;
            }
        }

        /**
         * The target acceleration has been changed. Updated the regulator.
         * @param newAcc
         */
        synchronized void adjustAcceleration(int newAcc) {
            if (curTargetVelocity != 0) {
                startSubMove(Math.abs(curTargetVelocity), newAcc, curLimit, curHold);
            }
            if (pending) {
                newAcceleration = newAcc;
            }
        }

        /**
         * The move has completed either by the motor stopping or by it stalling
         * @param stalled
         */
        synchronized private void endMove(boolean stalled) {
            moving = pending;
//            updateState(0, curHold, stalled);
            if (stalled) {
                // stalled try and maintain current position
                reset();
                curVelocity = 0;
                stallCnt = 0;
                startSubMove(0, 0, NO_LIMIT, curHold);
            }
            // if we have a new move, go start it
            if (pending) {
                pending = false;
                startSubMove(newSpeed, newAcceleration, newLimit, newHold);
//                updateState(Math.round(curTargetVelocity), curHold, false);
            }
            notifyAll();
        }

        /**
         * Monitors time and tachoCount to regulate velocity and stop motor rotation at limit angle
         */
        synchronized void regulateMotor(long delta) {
            float error;
            now += delta;
            long elapsed = now - baseTime;
            if (moving) {
                if (elapsed < accTime) {
                    // We are still acclerating, calculate new position
                    curVelocity = baseVelocity + curAcc * elapsed / (1000);
                    curCnt = baseCnt + (baseVelocity + curVelocity) * elapsed / (2 * 1000);
                    error = curCnt - tachoCnt;
                } else {
                    // no longer acclerating, calculate new psotion
                    curVelocity = curTargetVelocity;
                    curCnt = baseCnt + accCnt + curVelocity * (elapsed - accTime) / 1000;
                    error = curCnt - tachoCnt;
                    // Check to see if the move is complete
                    if (curTargetVelocity == 0 && (pending || (Math.abs(error) < 2 && elapsed > accTime + 100) || elapsed > accTime + 500)) {
                        endMove(false);
                    }
                }
                // check for stall
                if (Math.abs(error) > stallLimit) {
                    baseTime += delta;
                    if (stallCnt++ > 1000) {
                        endMove(true);
                    }
                } else {
                    stallCnt /= 2;
                }
                calcPower(error, MOVE_P, MOVE_I, MOVE_D);
                // If we have a move limit, check for time to start the deceleration stage
                if (checkLimit) {
                    float acc = (curVelocity * curVelocity) / (2 * (curLimit - curCnt));
                    if (curAcc / acc < 1.0) {
                        startSubMove(0, acc, NO_LIMIT, curHold);
                    }
                }
            } else if (curHold) {
                // not moving, hold position
                error = curCnt - tachoCnt;
                calcPower(error, HOLD_P, HOLD_I, HOLD_D);
            } else {
                // Allow the motor to move freely
                curCnt = tachoCnt;
                power = 0;
                mode = FLOAT;
                active = false;
//                cont.removeMotor(NXTRegulatedMotor.this);
            }
        }// end run

        /**
         * helper method for velocity regulation.
         * calculates power from error using double smoothing and PID like
         * control
         * @param error
         */
        private void calcPower(float error, float P, float I, float D) {
            // use smoothing to reduce the noise in frequrent tacho count readings
            // New values
            err1 = 0.375f * err1 + 0.625f * error;  // fast smoothing
            err2 = 0.75f * err2 + 0.25f * error; // slow smoothing
            // Original values
            //err1 = 0.5f * err1 + 0.5f * error;  // fast smoothing
            //err2 = 0.8f * err2 + 0.2f * error; // slow smoothing
            float newPower = basePower + P * err1 + D * (err1 - err2);
            basePower = basePower + I * (newPower - basePower);
            if (basePower > MAX_POWER) {
                basePower = MAX_POWER;
            } else if (basePower < -MAX_POWER) {
                basePower = -MAX_POWER;
            }
            power = (newPower > MAX_POWER ? MAX_POWER : newPower < -MAX_POWER ? -MAX_POWER : Math.round(newPower));

            mode = (power == 0 ? STOP : FORWARD);
        }
    }

  

    public float getMaxSpeed() {
        // It is generally assumed, that the maximum accurate speed of Motor is
        // 100 degree/second * Voltage
        return 1.0f;
    }
}
