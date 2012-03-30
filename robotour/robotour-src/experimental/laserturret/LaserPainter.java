package experimental.laserturret;

import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.gui.map.LocalPath;
import robotour.navi.basic.Point;
import robotour.hardware.SSC32;
import robotour.hardware.SSC32.SSCServo;

/**
 *
 * @author Kotuc
 */
public class LaserPainter {

    private final SSC32 ssc;
    private final SSCServo servox;
    private final SSCServo servoy;
    private LocalPath path = new LocalPath();

    public LaserPainter(SSC32 ssc) {
        this.ssc = ssc;
        {
            SSCServo servo = ssc.getServo(23);
            servo.setCenter(1640);
            servo.setAmplitude(200);
//            servo.setSpeedLimit(400);
            this.servox = servo;
        }
        {
            SSCServo servo = ssc.getServo(27);
            servo.setCenter(950);
            servo.setAmplitude(200);
//            servo.setSpeedLimit(400);
            this.servoy = servo;
        }

        Point a = new Point(-0.5, 0.5);
        Point b = new Point(0.5, 0.5);
        Point c = new Point(-0.5, -0.5);
        Point d = new Point(0.5, -0.5);
        Point t = new Point(0, 1);

        path.append(c);
        path.append(a);
        path.append(b);
        path.append(t);
        path.append(a);
        path.append(d);
        path.append(b);
        path.append(c);
        path.append(d);
//        path.append(c);
    }
    Point lastPoint = new Point(0, 0);

    public void run() {

//        goToXY(1, 0);
        while (true) {
            for (Point point : path.getWaypoints()) {
//            {                Point point = new Point(Math.random() - Math.random(), Math.random() - Math.random());
                goToXY(point);
            }
        }
    }
    // center x, center y
    double cx, cy;
    // half width, half height; i.e. one side amplitude
    double hw, hh;

    void goToXY(Point dest) {

        int millis = (int) (250 * lastPoint.getDistanceTo(dest));

        servox.setPositionMillis(dest.getX(), millis);
        servoy.setPositionMillis(dest.getY(), millis);

        try {
            Thread.sleep(millis);
//                    Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(LaserPainter.class.getName()).log(Level.SEVERE, null, ex);
        }

        lastPoint = dest;

    }

    public static void main(String[] args) throws Exception {
        new LaserPainter(SSC32.getSSC32("COM7")).run();
    }
}
