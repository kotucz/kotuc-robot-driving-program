package robotour.gui.map;

import robotour.navi.basic.Point;

import java.awt.*;

import robotour.navi.basic.Azimuth;
import robotour.navi.basic.Pose;

/**
 *
 * @author Tomas
 */
public class RobotImgLayer implements MapLayer {

    private final Pose pose;
//    private final double scale = 0.01 * MapView.DPM;
    private final double scale = 1.4;

    public RobotImgLayer(Pose pose) {
        this.pose = pose;
    }

//    private final Odometry odometry;
//    private final Compass cmps;
//    private Azimuth azimuth;
//    private Point point;
//    public RobotImgLayer(Odometry odometry, Compass cmps) {
//        this.odometry = odometry;
//        this.cmps = cmps;
//    }
    public RobotImgLayer() {
        this.pose = new Pose(new Point(0, 0),
                Azimuth.valueOfDegrees(0));
    }
//    private Image robotImg = Toolkit.getDefaultToolkit().createImage("./robot.png");
    private Image robotImg = Toolkit.getDefaultToolkit().createImage("./mob-03icon.png");

    public void setAzimuth(Azimuth azimuth) {
        this.pose.setAzimuth(azimuth);
    }

    public void setPoint(Point point) {
        this.pose.setPoint(point);
    }

    public void paint(Paintable map) {



        map.drawTexture(robotImg, pose.getPoint(), scale, pose.getAzimuth());
        map.getGraphics().drawString("Azimuth: " + pose.getAzimuth(), 10, 10);

//        try {
//            Azimuth azimuth = cmps.getAzimuth();
//            map.drawTexture(robot, odometry.getPoint(), 0.01 * MapView.DPM, azimuth);
//            map.getGraphics().drawString("Azimuth: " + azimuth , 10, 10);
//        } catch (MeasureException ex) {
//            Logger.getLogger(RobotImgLayer.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
}
