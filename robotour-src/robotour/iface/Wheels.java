/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package robotour.iface;

/**
 *
 * @author kotuc
 */
public interface Wheels {


    /**
     * 1 .. max forward
     * 0 .. stop
     * -1 .. max backward
     * @param speed
     */
    void setSpeed(double speed);

    /**
     *
     * 1 .. max right
     * 0 .. straight
     * -1 .. max left
     *
     * @param steer
     */
    void setSteer(double steer);

    /**
     * 
     */
    void stop();
}
