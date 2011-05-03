package robotour.behavior.impl;

import robotour.behavior.pid.BlindPilot;
import robotour.util.RobotSystems;
import robotour.iface.MeasureException;
import robotour.iface.Wheels;
import robotour.iface.Compass;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import robotour.hardware.ControllerTools;
import robotour.navi.basic.Azimuth;

/**
 *
 * @author Tomas
 */
public class OrthoXPadDriving implements Runnable {

    private final Controller gamepad;
    private final Wheels wheels;
    private final Compass cmps;
    private final BlindPilot pilot;

    public OrthoXPadDriving(RobotSystems systems) {
        this(systems, ControllerTools.getActiveGamePad());
    }

    public OrthoXPadDriving(RobotSystems systems, Controller gamepad) {
        if (systems == null) {
            throw new NullPointerException("systems");
        }
        if (gamepad == null) {
            throw new NullPointerException("gamepad");
        }
        this.gamepad = gamepad;
        this.wheels = systems.getWheels();
        this.cmps = systems.getCompass();
        this.pilot = new BlindPilot(wheels, cmps);
    }
    private Azimuth compassOffset;
    private Azimuth direction;
    private volatile Callable instruction;

    private void resetCompassOffset() {
        try {
            compassOffset = cmps.getAzimuth();
            direction = Azimuth.valueOfDegrees(0);
            System.out.println("new cmps offset: " + compassOffset);
        } catch (MeasureException ex) {
            Logger.getLogger(OrthoXPadDriving.class.getName()).log(Level.SEVERE, null, ex);
            wheels.stop();
        }
    }

    public void run() {
        resetCompassOffset();
        boolean enabled = true;
        System.out.println("Started");
        while (enabled && gamepad.poll()) {

//            wheels.setSpeed(speed);
//            wheels.setSteer(steer);

//            Logger.getLogger("OrthoXPadDriving").log(Level.INFO, "speed: " + speed + "; steer: " + steer);


            double speed = 0.0;
            double steer = 0.0;
            for (Component component : gamepad.getComponents()) {
                final double M1POW = 0.5;
                final long M1TIMEMS = Math.round(1000 * /*RTOdometry.powerToSpeed(*/M1POW/*)*/ / 0.5);
                if ("0".equals(component.getIdentifier().getName()) && component.getPollData() > 0.5) {
                    instruction = Executors.callable(new MoveInstruction(M1POW, M1TIMEMS));
                    try {
                        instruction.call();
                    } catch (Exception ex) {
                        Logger.getLogger(OrthoXPadDriving.class.getName()).log(Level.SEVERE, null, ex);
                        instruction = null;
                    }
                }
                if ("1".equals(component.getIdentifier().getName()) && component.getPollData() > 0.5) {
                    instruction = Executors.callable(new MoveInstruction(-M1POW, M1TIMEMS));
                    try {
                        instruction.call();
                    } catch (Exception ex) {
                        Logger.getLogger(OrthoXPadDriving.class.getName()).log(Level.SEVERE, null, ex);
                        instruction = null;
                    }
                }
                if ("pov".equals(component.getIdentifier().getName())) {
                    double val = component.getPollData();
//                    System.out.println("pov: " + component.getPollData() + " right: " + right + " up: " + up);
                    if (val > 0.001) {
                        direction = Azimuth.valueOfRadians(2 * Math.PI * (val - 0.25));
                    }
//                    double right = -Math.cos(2 * Math.PI * val);
//                    double up = Math.sin(2 * Math.PI * val);
//                    if (up > 0.5) {
//
//                        instruction = Executors.callable(new MoveInstruction(1, 1000));
//                        try {
//                            instruction.call();
//                        } catch (Exception ex) {
//                            Logger.getLogger(OrthoXPadDriving.class.getName()).log(Level.SEVERE, null, ex);
//                            instruction = null;
//                        }
//                    }
//
//                    if (up < -0.5) {
//
//                        instruction = Executors.callable(new MoveInstruction(-1, 1000));
//                        try {
//                            instruction.call();
//                        } catch (Exception ex) {
//                            Logger.getLogger(OrthoXPadDriving.class.getName()).log(Level.SEVERE, null, ex);
//                            instruction = null;
//                        }
//                    }
//
//                    if (right > 0.5) {
//
//                        instruction = Executors.callable(new RotateInstruction(Azimuth.valueOfDegrees(90)));
//                        try {
//                            instruction.call();
//                        } catch (Exception ex) {
//                            Logger.getLogger(OrthoXPadDriving.class.getName()).log(Level.SEVERE, null, ex);
//                            instruction = null;
//                        }
//                    }
                }
                if ("3".equals(component.getIdentifier().getName()) && component.getPollData() > 0.5) {
                    resetCompassOffset();
                }
//                if ("x".equals(component.getIdentifier().getName())) {
//                    steer = Math.signum(speed) * component.getPollData();
//                } else if ("y".equals(component.getIdentifier().getName())) {
//                    speed = -component.getPollData();
//                }
//                if ("y".equals(component.getIdentifier().getName())) {
//                    float poll = -component.getPollData();
//                    speed = Math.signum(poll) * ((Math.abs(poll) > 0.3) ? 0.1 : 0.0);
//                }
            }
            try {
                double diff = (direction.radians() - cmps.getAzimuth().radians() + compassOffset.radians()) % (2 * Math.PI);
                while (diff > Math.PI) {
                    diff -= 2 * Math.PI;
                }
                while (diff < -Math.PI) {
                    diff += 2 * Math.PI;
                }

                wheels.setSteer(Math.max(-1, Math.min(Math.pow(diff, 1), 1)));
            } catch (MeasureException ex) {
                Logger.getLogger(OrthoXPadDriving.class.getName()).log(Level.SEVERE, null, ex);
            }
//                    wheels.setSteer(Math.signum(diff));
//            wheels.setSpeed(speed);
//            wheels.setSteer(steer);
//            Logger.getLogger("OrthoXPadDriving").log(Level.INFO, "speed: " + speed + "; steer: " + steer);
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger("XPadDriving").log(Level.SEVERE, null, ex);
            }

        }
    }

    public void start() {
        new Thread(this).start();
    }

    public static void main(String[] args) {
        new OrthoXPadDriving(RobotSystems.getDefault()).start();
    }

    private class MoveInstruction implements Runnable {

        private final double speed;
        private final long time;

        public MoveInstruction(double speed, long time) {
            this.speed = speed;
            this.time = time;
        }

        public void run() {
            System.out.println("FORWARD: " + speed + " " + time + " ms");
            wheels.setSpeed(speed);
            try {
                Thread.sleep(time);
            } catch (InterruptedException ex) {
                System.err.println("Interrupted");
            } finally {
                wheels.stop();
                instruction = null;
            }
        }
    }

    private class RotateInstruction implements Runnable {

        private final Azimuth target;

        public RotateInstruction(Azimuth target) {
            this.target = target;
        }

        public void run() {
            System.out.println("ROTATE: " + target);

            try {
                while (true) {
                    double diff = (target.radians() - cmps.getAzimuth().radians() + compassOffset.radians()) % (2 * Math.PI);

                    while (diff > Math.PI) {
                        diff -= 2 * Math.PI;
                    }
                    while (diff < -Math.PI) {
                        diff += 2 * Math.PI;
                    }

                    if (Math.abs(diff) < 0.03) {
                        break;
                    }

//                    wheels.setSteer(-1);
//                    wheels.setSteer(Math.signum(diff));
                    wheels.setSteer(Math.max(-1, Math.min(Math.pow(diff, 1), 1)));
//                    wheels.setSpeed(0.3);
                    System.out.println("diff: " + diff);
                    Thread.sleep(10);
                }
                System.out.println("ROTATE DONE");
            } catch (Exception ex) {
                Logger.getLogger(OrthoXPadDriving.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                wheels.stop();
                instruction = null;
            }

        }
    }
}
