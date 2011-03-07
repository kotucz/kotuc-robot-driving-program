package robotour.gui.map;

import java.awt.Image;
import java.awt.Toolkit;
import robotour.navi.gps.input.GPSInput;

/**
 *
 * @author Kotuc
 */
public class GPSIconLayer implements MapLayer {

    private final GPSInput gps;
    private final Image compassIcon;
    private final TrackLayer trackLayer;

    public GPSIconLayer(GPSInput input, Image compassIcon) {
        this.gps = input;
        this.compassIcon = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/images/compass.png"));

        this.trackLayer = new TrackLayer(gps.getActiveLog());
    }  

    /**
     *
     * @param map
     */
    public void paint(MapView map) {
        //         g.setColor(Color.RED);
//                int gx = (int)map.getX(Device.gps.point);
//                int gy = (int)map.getY(Device.gps.point);

        //                g.fillArc(gy-20, gy-20, 40, 40, (int)(Device.gps.azimuth-5), (int)(10));

        trackLayer.paint(map);

        map.drawTexture(compassIcon, gps.getPosition(), map.getScale(), gps.getAzimuth());

    //                g.translate(gx, gy);
//                g.rotate(Math.toRadians(Device.gps.azimuth));
//
//                g.drawImage(compassIcon, -20, -20, null);
//
//                g.rotate(-Math.toRadians(Device.gps.azimuth));
//                g.translate(-gx, -gy);

    //                activeLog.paint(map);
    }
}
