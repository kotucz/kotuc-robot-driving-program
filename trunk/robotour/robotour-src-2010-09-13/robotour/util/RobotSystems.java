package robotour.util;

import robotour.iface.Sonar;
import robotour.iface.Wheels;
import robotour.iface.Compass;
import robotour.iface.Tachometer;
import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.hardware.I2CUSB;
import robotour.hardware.SSC32;
import vision.input.VideoInput;
import vision.input.VideoInputs;

/**
 *
 * @author kotuc
 */
public class RobotSystems {

    private Wheels wheels;
    private Sonars sonars;
    private Compass compass;
    private VideoInput video;
    private Tachometer encoder;
    private SSC32 ssc;

    public static RobotSystems getLocalHard() {
        RobotSystems systems = new RobotSystems();

        final boolean wheelsEnabled = false;
//        final boolean wheelsEnabled = false;
        final boolean i2cEnabled = false;
//        final boolean i2cEnabled = false;
        final boolean videoEnabled = false;
//        final boolean videoEnabled = false;

//        final String sscPort = "/dev/ttyS0"; // linux
        final String sscPort = "COM5"; // acer
//        final String i2cPort = "/dev/ttyUSB0"; // linux
        final String i2cPort = "COM3"; // win

        if (wheelsEnabled) {

            try {
//                SscDiffWheels wheels = new SscDiffWheels(SSC32.getSSC32(sscPort));
                SSC32 ssc = SSC32.getSSC32(sscPort);
                systems.ssc = ssc;
                SscWheels wheels = new SscWheels(ssc);
                systems.setWheels(wheels);
                systems.setEncoder(wheels);
            } catch (Exception ex) {
                Logger.getLogger(RobotSystems.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(1);
            }

        } else {
            System.err.println("WARNING!!! FAKE WHEELS!!!");
            FakeWheels noWheels = new FakeWheels();
            systems.setWheels(noWheels);
//            systems.setEncoder(noWheels);
//            systems.setCompass(noWheels);
        }


        if (videoEnabled) {
            systems.setVideo(VideoInputs.getVideo());

        }

        if (i2cEnabled) {
            try {
                I2CBuffer buffer = I2CBuffer.createBuffer(I2CUSB.getI2C(i2cPort));
                systems.setCompass(buffer.getCompass());
                systems.setSonars(buffer.getSonars());
                buffer.startPollingThread();
            } catch (Exception ex) {
                Logger.getLogger(RobotSystems.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(1);
            }
        } else {
            System.err.println("WARNING!!! FAKE SONARS!!!");
            systems.setSonars(new Sonars(new RandomSonar(), new RandomSonar(), new RandomSonar()));
        }

        return systems;
    }

    public SSC32 getSsc() {
        return ssc;
    }

    public void setVideo(VideoInput video) {
        this.video = video;
    }

    public static RobotSystems getDefault() {
        try {
            return getLocalHard();
//           return robotour.rmi.RobotSystemsClient.bindServer("10.0.0.9");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.exit(1);
        return null;
    }

    public Wheels getWheels() {
        return wheels;
    }

    public void setEncoder(Tachometer encoder) {
        this.encoder = encoder;
    }

    public Tachometer getEncoder() {
        return encoder;
    }

    public Sonars getSonars() {
        return sonars;
    }

    public Sonar getCenterSonar() {
        return sonars.getCenter();
    }

    public Sonar getLeftSonar() {
        return sonars.getLeft();
    }

    public Sonar getRightSonar() {
        return sonars.getRight();
    }

    public Compass getCompass() {
        return compass;
    }

    public VideoInput getVideo() {
        return this.video;
    }

    public void setCompass(Compass compass) {
        this.compass = compass;
    }

    public void setSonars(Sonars sonars) {
        this.sonars = sonars;
    }

    public void setWheels(Wheels wheels) {
        this.wheels = wheels;
    }
}
