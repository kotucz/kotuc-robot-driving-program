package robotour.navi.local.birdseye;

import java.awt.Color;
import java.awt.Graphics2D;
import robotour.navi.basic.LocalPoint;
import robotour.arduino.ArdOdometry;
import robotour.navi.basic.Azimuth;
import robotour.navi.basic.RobotPose;
import robotour.util.log.events.SonarEvent;
import robotour.util.log.events.Tags;
import vision.ImageFrame;
import vision.pathfinding.birdseye.FloorImage;

/**
 *
 * @author Kotuc
 */
public class SonarDensity {
    public static final double sonarcutoffdist = 0.5;

    FloorImage solids = new FloorImage();
    Graphics2D g = solids.getGraphics();
//    ArdOdometry odometry;

    public FloorImage getSolids() {
        return solids;
    }

//    public SonarDensity(ArdOdometry odometry) {
//        this.odometry = odometry;
////        g.setColor(new Color(0x77777777, true));
////        g.fillRect(0, 0, solids.getWidth(), solids.getHeight());
//    }

    public void process(RobotPose pose, SonarEvent sevent) {
        int offset;
        if (Tags.left.equals(sevent.getName())) {
            offset = -20;
        } else if (Tags.center.equals(sevent.getName())) {
            offset = 0;
        } else if (Tags.right.equals(sevent.getName())) {
            offset = 20;
        } else {
            offset = 0;
            System.err.println("sonar id not recognized: " + sevent.getName());
        }
        drawSonar(pose.getPoint(), pose.getAzimuth(), offset, sevent.getDistance());
    }

    void drawSonar(LocalPoint point, Azimuth azim, int azimoff, double distancem) {

        int centerangle = ((int) azim.degrees() + azimoff) - 90;
        int beamwidthangledeg = 30; // deg

        // no obstacle
        g.setColor(Color.BLACK);

        int radius = (int) Math.ceil(solids.getPixelsperm() * distancem);

        g.fillArc(solids.getX(point.getX()) - radius, solids.getY(point.getY()) - radius,
                2 * radius, 2 * radius, centerangle - (beamwidthangledeg / 2), beamwidthangledeg);

        if (distancem < sonarcutoffdist) {
            // probably obstacle
            g.setColor(Color.WHITE);
            radius += 1;
            g.drawArc(solids.getX(point.getX()) - radius, solids.getY(point.getY()) - radius,
                    2 * radius, 2 * radius, centerangle - (beamwidthangledeg / 2), beamwidthangledeg);
        }
    }

    public double getFieldDensity(int x, int y) {
        return (solids.getFieldRGB(x, y)&0xFF)/255.0;
    }

    public static void main(String[] args) {
        SonarDensity sonarDensity = new SonarDensity();
        sonarDensity.drawSonar(new LocalPoint(10, 10), Azimuth.valueOfDegrees(-130), 30, 10.0);
        ImageFrame imageFrame = new ImageFrame("Sonar density Test");
        imageFrame.setImage(sonarDensity.solids.getImage());
    }
}
