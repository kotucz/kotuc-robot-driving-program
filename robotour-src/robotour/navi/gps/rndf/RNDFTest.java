package robotour.navi.gps.rndf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import robotour.gui.map.gps.MapView;
import robotour.gui.map.gps.TrackLayer;
import robotour.navi.gps.Track;

/**
 *
 * @author Kotuc
 */
public class RNDFTest {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        MapView view = new MapView();

//TODO        view.addLayer(new MapTextureManager());
//        view.addLayer(RNDFMap.load(new File("./Luzanky.rnd")));
        RNDFMap map = new RNDFLoader().load(new File("./luzanky-ver2-rndf.txt"));
//  T      view.addLayer(map);
        Track track = map.createTrack("BQPONAR");
        view.addLayer(new TrackLayer(track));
//        view.addLayer(RNDFMap.load(new File("./stromovka-rndf.txt")));
        view.showInFrame();
    }
}
