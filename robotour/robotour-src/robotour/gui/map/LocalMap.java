package robotour.gui.map;

import robotour.gui.map.gps.MapView;
import robotour.navi.basic.Point;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kotuc
 */
public class LocalMap implements MapLayer {

    private List<LocalObject> objects = new ArrayList<LocalObject>();

    public void paint(Paintable map) {


        map.setColor(Color.BLACK);

        map.drawLine(new Point(-1000, 0), new Point(1000, 0), 0.01);
        map.drawLine(new Point(0, -1000), new Point(0, 1000), 0.01);

//        g.setColor(Color.GRAY);
//        g.drawLine(-1000, 100, 1000, 100);
//        g.drawLine(100, -1000, 100, 1000);
//        g.drawLine(-1000, -100, 1000, -100);
//        g.drawLine(-100, -1000, -100, 1000);




        for (LocalObject localObject : new ArrayList<LocalObject>(objects)) {
            map.setColor(localObject.getColor());
            map.fillOval(localObject.getPoint(), localObject.getSize());
        }

        map.setColor(Color.YELLOW);
        map.drawString("HI!", new Point(0, 0));

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
