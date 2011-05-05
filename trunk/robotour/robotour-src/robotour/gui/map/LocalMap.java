package robotour.gui.map;

import robotour.navi.basic.LocalPoint;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kotuc
 */
public class LocalMap implements MapLayer {

    private List<LocalObject> objects = new ArrayList<LocalObject>();

    public void paint(MapView map) {

        Graphics2D g = map.getGraphics();

        g.setColor(Color.BLACK);

        map.drawLine(new LocalPoint(-1000, 0), new LocalPoint(1000, 0), 0.01);
        map.drawLine(new LocalPoint(0, -1000), new LocalPoint(0, 1000), 0.01);

//        g.setColor(Color.GRAY);
//        g.drawLine(-1000, 100, 1000, 100);
//        g.drawLine(100, -1000, 100, 1000);
//        g.drawLine(-1000, -100, 1000, -100);
//        g.drawLine(-100, -1000, -100, 1000);




        for (LocalObject localObject : new ArrayList<LocalObject>(objects)) {
            g.setColor(localObject.getColor());
            map.drawDot(localObject.getPoint(), localObject.getSize());
        }

        g.setColor(Color.YELLOW);
        g.drawString("HI!", 0, 0);

    }

    public void addObject(LocalObject object) {
        objects.add(object);
    }

    public void showInFrame() {
        MapView view = new MapView();
        view.addLayer(this);
        view.showInFrame();
    }

    public static void main(String[] args) {
        new LocalMap().showInFrame();
    }
}
