/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eurobot.kuba.pid;

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
/**
 *
 * @author Kotuc
 */
class Regulator {
    static final int NO_LIMIT = 0x7fffffff;
    final static int MAX_POWER = 100;
    // PID constants for move and for hold
    // Old values
    //static final float MOVE_P = 4f;
    //static final float MOVE_I = 0.04f;
    //static final float MOVE_D = 32f;
    // New values
    //static final float MOVE_P = 7f;              
    static final float MOVE_P = 6.0F;
    static final float MOVE_I = 0.04F;
    static final float MOVE_D = 22.0F;
    static final float HOLD_P = 2.0F;
    static final float HOLD_I = 0.04F;
    static final float HOLD_D = 8.0F;
    // PID variables
    float basePower = 0; //used to calculate power
    float err1 = 0; // used in smoothing
    float err2 = 0; // used in smoothing
    
    /**
     * expected velocity now
     */
    float curVelocity = 0; 
    /**
     * velocity at the begining of acceleration phase
     */
    float baseVelocity = 0; 
    /**
     * start of acceleration tachometer count
     */
    float baseCnt = 0; 
    
    /**
     *  expected current tachometer count based on settings and equations
     */
    float curCnt = 0;
    float curAcc = 0; // current acceleration
    float curTargetVelocity = 0; // end of acceleration velocity
    int curLimit = NO_LIMIT; // target tacometer count
    boolean curHold = true; // flag if should hold
    float accCnt = 0; // distance of current acceleration phase
    long baseTime = 0; // start time of acceleration phase
    long now = 0; // now
    long accTime = 0; // end time of acceleration phase
    
    // state variables
    boolean moving = false;
    boolean pending = false;
    boolean checkLimit = false;
    
    
    float newSpeed = 0;
    int newAcceleration = 0;
    int newLimit = 0;
    boolean newHold = true;
    
    //********************
    // INTERFACE VARIABLES
    // ...................
    /**
     * The variable, where encoder position is stored
     */
    int tachoCnt;
    /**
     * After update step power for the motors to be set is stored in this variable
     */
    public int power;
    
    
    
//    int mode;
//    boolean active = false;

    
    // stalling when - vars to know when to turn off motors - robot stops
    protected int stallLimit = 50;
    protected int stallCnt = 0;
    
    
    /**
     * Reset the tachometer readings
     */
    synchronized void reset() {
//        tachoCnt = MotorRegulator.getTachoCount();
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
    private synchronized void startSubMove(float speed, float acceleration, int limit, boolean hold) {
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
    public synchronized void newMove(float speed, int acceleration, int limit, boolean hold) {
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
    private synchronized void endMove(boolean stalled) {
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
//            mode = MotorRegulator.FLOAT;
//            active = false;
            //                cont.removeMotor(NXTRegulatedMotor.this);
        }
    } // end run

    /**
     * helper method for velocity regulation.
     * calculates power from error using double smoothing and PID like
     * control
     * @param error
     */
    private void calcPower(float error, float P, float I, float D) {
        // use smoothing to reduce the noise in frequrent tacho count readings
        // New values
        err1 = 0.375F * err1 + 0.625F * error; // fast smoothing
        err2 = 0.75F * err2 + 0.25F * error; // slow smoothing
        // Original values
        //err1 = 0.5f * err1 + 0.5f * error;  // fast smoothing
        //err2 = 0.8f * err2 + 0.2f * error; // slow smoothing
        float newPower = basePower + P * err1 + D * (err1 - err2);
        basePower += I * (newPower - basePower);
        //            if (basePower > MAX_POWER) {
        //                basePower = MAX_POWER;
        //            } else if (basePower < -MAX_POWER) {
        //                basePower = -MAX_POWER;
        //            }
        basePower = clipPower(basePower);
        //            power = (newPower > MAX_POWER ? MAX_POWER : newPower < -MAX_POWER ? -MAX_POWER : Math.round(newPower));
        power = Math.round(clipPower(newPower));
//        mode = (power == 0 ? MotorRegulator.STOP : MotorRegulator.FORWARD);
    }
    
    private float clipPower(float power) {
        return clipPower(power, -MAX_POWER, MAX_POWER);
    }
    
    float clipPower(float power, float min, float max) {
        if (power > max) {
            return max;
        } else if (power < min) {
            return min;
        } else {
            return power;
        }
    }
    
}
