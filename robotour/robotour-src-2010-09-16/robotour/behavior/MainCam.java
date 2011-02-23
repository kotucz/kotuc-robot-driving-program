package robotour.behavior;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import robotour.gui.ViewHUD;
import robotour.iface.Wheels;
import robotour.util.RobotSystems;
import vision.ImageFrame;
import vision.input.VideoInput;
import vision.pathfinding.PathRecog;

/**
 *
 * @author Kotuc
 */
public class MainCam implements Behavior {

    private VideoInput video;
    private Wheels wheels;
    private ImageFrame imgFrame;
    private ViewHUD hud;
    private PathRecog precog = new PathRecog();

    public MainCam(RobotSystems systems) {
        this.video = systems.getVideo();
        this.wheels = systems.getWheels();

        this.imgFrame = new ImageFrame("PathRecog");
        this.imgFrame.setLocation(500, 0);
        this.hud = new ViewHUD(systems);
    }

    public boolean act() {

        try {

            BufferedImage image = video.snap();

            System.out.println(image);
            if (image == null) {
                System.out.println("null snap");
                wheels.stop();
                return false;
            } else {
                
            }

            precog.process(image);

            wheels.setSteer(precog.getGoodSteer());

            wheels.setSpeed(0.6);

            imgFrame.setImage(precog.getResult());

            try {
                ImageIO.write(image, "PNG", new File("snaps/" + System.currentTimeMillis() + ".png"));
            } catch (IOException ex) {
                Logger.getLogger(MainCam.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                hud.show(image);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

            // time to refresh
            Thread.sleep(10);

        } catch (Exception e) {
            System.err.println(e.getMessage());
            wheels.stop();
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        RobotSystems systems = RobotSystems.getLocalHard();
        new Arbitrator(
                new SonarCollisionAvoidance(systems.getWheels(), systems.getSonars(), 0.4),
                new MainCam(systems)).start();
    }
}
