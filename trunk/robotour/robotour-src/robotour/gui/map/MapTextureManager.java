package robotour.gui.map;

import java.awt.Graphics2D;
import java.util.LinkedList;

/**
 *
 * @author Kotuc
 */
public class MapTextureManager implements MapLayer {

    /** Creates a new instance of MapTextureManager */
    public MapTextureManager() {
        loadTextures();
    }
    private LinkedList<MapTexture> textures = new LinkedList<MapTexture>();

    void loadTextures() {
        textures.add(MapTexture.load("./mapimgs/49-12-23.524N 16-36-30.931E.jpg", "49-12-23.524N", "16-36-30.931E", 1 * MapView.DPM));
//        textures.add(new MapTexture("textures/map_000095.gif", 0, 0, 82000000));
//        textures.add(new MapTexture("textures/map_3.png", "49o56'10.05\"N", "17o57'54.5\"E", 16.0 * MapView.DPM));
//        textures.add(new MapTexture("textures/map_4.png", "49o55'58.36\"N", "18o0'46.38\"E", 4.0 * MapView.DPM));
//        textures.add(new MapTexture("textures/map_1.png", "50o6'22.95\"N", "14o25'30.27\"E", 2.0 * MapView.DPM));
//        textures.add(new MapTexture("textures/map_2.png", "49o55'30.23\"N", "18o2'30.05\"E", MapView.DPM));
    }

    public void paint(MapView map) {
        Graphics2D g = map.getGraphics();
        for (MapTexture mtex : textures) {
            mtex.paint(g, map);
        }
    }
}
