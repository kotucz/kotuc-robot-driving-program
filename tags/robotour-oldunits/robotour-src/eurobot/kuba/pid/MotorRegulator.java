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

//    static public final int PWM_FLOAT = 0;
//    static public final int PWM_BRAKE = 1;
//    public final static int FORWARD = 1;
//    public final static int BACKWARD = 2;
//    public final static int STOP = 3;
//    public final static int FLOAT = 4;
//   
    
    final Regulator reg;
//    protected TachoMotorPort tachoPort;
//    protected boolean stalled = false;
//    protected RegulatedMotorListener listener = null;
    protected int speed = 360;
    protected int acceleration = 6000;
//    protected int limitAngle = 0;
//    private int stallLimit = 50;
//    private int stallCnt = 0;
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
//    public int getTachoCount() {
//        // TODO connect encoder
//        return 0;
//    }

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
        reg.newMove(speed, acceleration, +Regulator.NO_LIMIT, true);
    }

    /**
     * @see lejos.nxt.BasicMotor#backward()
     */
    public void backward() {
        reg.newMove(speed, acceleration, -Regulator.NO_LIMIT, true);
    }

    /**
     * Set the motor into float mode. This will stop the motor without braking
     * and the position of the motor will not be maintained.
     */
    public void floatMode() {
        reg.newMove(0, acceleration, Regulator.NO_LIMIT, false);
    }

    /**
     * Causes motor to stop, pretty much
     * instantaneously. In other words, the
     * motor doesn't just stop; it will resist
     * any further motion.
     * Cancels any rotate() orders in progress
     */
    public void stop() {
        reg.newMove(0, acceleration, Regulator.NO_LIMIT, true);
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
//    public int getAcceleration() {
//        return acceleration;
//    }

    /**
     * Return the angle that this Motor is rotating to.
     * @return angle in degrees
     */
//    public int getLimitAngle() {
//        return limitAngle;
//    }

    /**
     * Reset the tachometer associated with this motor. Note calling this method
     * will cause any current move operation to be halted.
     */
//    public void resetTachoCount() {
//        synchronized (reg) {
//            // Make sure we are stopped!
//            reg.newMove(0, acceleration, NO_LIMIT, true);
//            resetTachoCount();
//            reg.reset();
//        }
//    }

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
//    public int getSpeed() {
//        return Math.round(speed);
//    }

    /**
     * Return true if the motor is currently stalled.
     * @return
     */
//    public boolean isStalled() {
//        return stalled;
//    }

    /**
     * Return the current velocity.
     * @return current velocity in degrees/s
     */
//    public int getRotationSpeed() {
//        return Math.round(reg.curVelocity);
//    }

//    void controlMotor(int power, int mode) {
//        // send power to motor..and set mode??
//        throw new UnsupportedOperationException("Not yet implemented");
//    }

 

//    public float getMaxSpeed() {
//        // It is generally assumed, that the maximum accurate speed of Motor is
//        // 100 degree/second * Voltage
//        return 1.0f;
//    }
}
