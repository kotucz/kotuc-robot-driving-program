package robotour.behavior;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import robotour.gui.ViewHUD;
import robotour.hardware.SSC32;
import robotour.iface.Compass;
import robotour.iface.Wheels;
import robotour.navi.basic.Angle;
import robotour.navi.basic.Azimuth;
import robotour.util.RobotSystems;
import robotour.util.Sonars;
import vision.ImageFrame;
import vision.input.VideoInput;
import vision.pathfinding.knowledge.KnownRecog;
import vision.pathfinding.knowledge.Info;

/**
 *
 * @author Kotuc
 */
public class Winner implements Behavior {

    private final VideoInput video;
    private final Sonars sonars;
    private final Compass compass;
    private final Wheels wheels;
    private final ImageFrame imgFrame;
    private final ViewHUD hud;
    private final KnownRecog krecog;
    private final BufferedWriter writer;
    private final SSC32 ssc;
//    private final Logger logger = Logger.getLogger("Winner");
    private Azimuth destination;

    public Winner(RobotSystems systems) {
        BufferedWriter bufferedWriter;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(new File("./logs/" + System.currentTimeMillis() + ".txt")));
        } catch (IOException ex) {
            ex.printStackTrace();
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));
        }

        this.writer = bufferedWriter;

        this.video = systems.getVideo();
        this.wheels = systems.getWheels();
        this.sonars = systems.getSonars();
        this.compass = systems.getCompass();
        this.ssc = systems.getSsc();

        this.krecog = new KnownRecog();
        this.krecog.createKnowledge(new File("./knowledge"));

        this.imgFrame = new ImageFrame("KnownRecog");
        this.imgFrame.setLocation(500, 0);
        this.hud = new ViewHUD(systems);

//                this.destination = compass.getAzimuth();
        this.destination = Azimuth.valueOfDegrees(40);

    }

    private void decide(KnownRecog krecog, double distance, double distance0, double distance1, Azimuth azimuth) {
        log("Azimuth: " + azimuth);
        try {
            System.out.println("SSC " + ssc.readVersion());
        } catch (IOException ex) {
            System.out.println("SSC OFF");
            this.destination = azimuth;
            log("Destination: " + this.destination);
        }

        hud.setAzimuth(azimuth);

        Graphics g = krecog.getResult().getGraphics();
        ((Graphics2D) g).setStroke(new BasicStroke(3f));
        g.setFont(Font.decode("Arial BOLD 16"));
        g.setColor(Color.GREEN);
        Info[] infos = krecog.infos;

        int goodcount = 0;
        int leastDiff = 200;
        Angle bestAngle = Angle.valueOfDegrees(-45);
        int bestIndex = 0;

        final int SQRS = 3;

        for (int i = 0; i < infos.length; i++) {
            Info info = infos[i];
            Point point = new Point(info.rect.x + (info.rect.width / 2), 2 * info.rect.height);
            Azimuth azimuth1 = hud.azimuth(point);

            Angle declination = azimuth1.sub(destination);
            declination = declination.shorter();

//            g.drawString("" + azimuth1, point.x, point.y);
            g.drawString("" + (int) declination.degrees(), point.x, point.y);

            if (info.isGood()) {
                goodcount++;
                if (goodcount >= SQRS) {
                    if (Math.abs(declination.degrees()) < leastDiff) {
                        leastDiff = Math.abs((int) declination.degrees());
                        bestIndex = i - SQRS / 2;
                    }
                }
            } else {
                goodcount = 0;
            }

        }

        g.setColor(Color.RED);
        g.drawRect(infos[bestIndex].rect.x, infos[bestIndex].rect.y - 2 * infos[bestIndex].rect.height, infos[bestIndex].rect.width, infos[bestIndex].rect.height);

        double speed = 0.5;
        final double maxratio = 0.4;
        double steer = speed * Math.max(-maxratio, Math.min((bestIndex - 4.5) / 4.0, maxratio));

        g.setColor(Color.RED);
        g.fillRect((int) (150 * steer) + 150, 30, 20, 20);

        log("Speed: " + speed);
        log("Steer: " + steer);

        wheels.setSpeed(speed);
        wheels.setSteer(steer);

//        throw new UnsupportedOperationException("Not yet implemented");
    }

    public boolean act() {

        log("-------------------------------");
        final long timestamp = System.currentTimeMillis();
        log("Timestamp: " + timestamp);

        try {

            BufferedImage image = video.snap();

//            System.out.println(image);
            if (image == null) {
                throw new IOException("null snap");
            }



            krecog.process(image);

            log(sonars.toString());

//            decide(krecog, sonars.getLeft().getDistance(), sonars.getCenter().getDistance(), sonars.getRight().getDistance(), compass.getAzimuth());
            decide(krecog,
                    sonars.getLeft().getDistance(), sonars.getCenter().getDistance(), sonars.getRight().getDistance(),
                    //                    Azimuths.EAST.getAzimuth()
                    compass.getAzimuth());


            imgFrame.setImage(krecog.getResult());

            try {
                File file = new File("snaps/" + timestamp + ".png");
                ImageIO.write(image, "PNG", file);
                log("Snap: " + file);
            } catch (IOException ex) {
                log(ex.getMessage());
            }

            try {
                hud.show(image);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

            // time to refresh
            Thread.sleep(10);
            return true;

        } catch (Exception e) {
            System.err.println(e.getMessage());
            wheels.stop();
            return false;
        }



    }

    private void log(String text) {
        System.out.println(text);
        try {
            writer.append(text);
            writer.newLine();
        } catch (Exception ex) {
            Logger.getLogger(Winner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        RobotSystems systems = RobotSystems.getLocalHard();
        new Arbitrator(
                new SonarCollisionAvoidance(systems.getWheels(), systems.getSonars(), 0.4),
                new Winner(systems)).start();
    }
}
