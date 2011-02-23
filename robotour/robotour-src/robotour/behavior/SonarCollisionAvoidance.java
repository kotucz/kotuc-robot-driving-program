package robotour.behavior;

import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.hardware.SoundPlay;
import robotour.iface.MeasureException;
import robotour.iface.Sonar;
import robotour.iface.Wheels;
import robotour.util.Sonars;

/**
 *
 * @author Kotuc
 */
public class SonarCollisionAvoidance implements Behavior {

    private double treshold;
    private Sonars sonars;
    private Wheels wheels;
    private SoundPlay dingSound = new SoundPlay("sounds/ding.wav");
    private SoundPlay punchSound = new SoundPlay("sounds/punch.wav");
    private SoundPlay whistleSound = new SoundPlay("sounds/whistle.wav");
    private static final long MAX_STOP_TIME = 15000;
    private long lastGo;
    private static final long DISABLE_TIME = 5000;
    private long disabledUntil = 0;
    private Braitenberg braitenberg;

    public SonarCollisionAvoidance(Wheels wheels, Sonars sonars, double treshold) {
        this.treshold = treshold;
        this.sonars = sonars;
        this.wheels = wheels;
        this.braitenberg = new Braitenberg(wheels, sonars);
    }

    /**
     * Detected collision will
     * @return
     */
    public boolean act() {
        final long currentTime = System.currentTimeMillis();


        for (Sonar sonar : sonars.getSonars()) {
            try {
                if (sonar.getDistance() < treshold) {
                    if (currentTime > disabledUntil) {
                        if ((currentTime - lastGo) < MAX_STOP_TIME) {
                            avoid();
                            return true;
                        } else {
                            disabledUntil = currentTime + DISABLE_TIME;
                        }
                    } else {
                        braitenberg.act();
                        lastGo = currentTime;
                        return true;
                    }
                }
            } catch (MeasureException ex) {
                Logger.getLogger(SonarCollisionAvoidance.class.getName()).log(Level.SEVERE, null, ex);
                wheels.stop();
                whistleSound.play();
                return true;
            }
        }
        dingSound.play();

        lastGo = currentTime;
        return false;
    }

    private void avoid() {
        System.out.println("OBSTACLE DETECTED");
        wheels.stop();
    }
}
