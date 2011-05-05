package robotour.gui.map;

import robotour.navi.basic.LocalPoint;
import java.awt.Image;
import java.awt.Toolkit;
import robotour.navi.basic.Azimuth;
import robotour.navi.basic.RobotPose;

/**
 *
 * @author Tomas
 */
public class RobotImgLayer implements MapLayer {

    private final RobotPose pose;
//    private final double scale = 0.01 * MapView.DPM;
    private final double scale = 1.4;

    public RobotImgLayer(RobotPose pose) {
        this.pose = pose;
    }

//    private final Odometry odometry;
//    private final Compass cmps;
//    private Azimuth azimuth;
//    private LocalPoint point;
//    public RobotImgLayer(Odometry odometry, Compass cmps) {
//        this.odometry = odometry;
//        this.cmps = cmps;
//    }
    public RobotImgLayer() {
        this.pose = new RobotPose(new LocalPoint(0, 0),
                Azimuth.valueOfDegrees(0));
    }
//    private Image robotImg = Toolkit.getDefaultToolkit().createImage("./robot.png");
    private Image robotImg = Toolkit.getDefaultToolkit().createImage("./mob-03icon.png");

    public void setAzimuth(Azimuth azimuth) {
        this.pose.setAzimuth(azimuth);
    }

    public void setPoint(LocalPoint point) {
        this.pose.setPoint(point);
    }

    public void paint(MapView map) {



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
