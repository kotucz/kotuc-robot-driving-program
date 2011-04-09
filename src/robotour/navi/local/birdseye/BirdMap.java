/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package robotour.navi.local.birdseye;

import javax.swing.JFrame;
import cambot.FloorLayer;
import robotour.gui.map.MapView;
import vision.pathfinding.birdseye.RealFloor;

/**
 *
 * @author ACER
 */
public class BirdMap {

    public final SonarDensity density = new SonarDensity();
    public final RealFloor floor = new RealFloor();
    public final Exploring exploring = new Exploring(floor, density);
    public final MapView mapView = new MapView();

    public BirdMap() {

        mapView.addLayer(new FloorLayer(floor));
//        mapView.addLayer(new FloorLayer(density.getSolids()));


        
        //        final Exploring expl = new Exploring(birdsEye.getFloor(), density);
        mapView.addLayer(exploring);

//        System.err.println("Showing");

        JFrame showInFrame = mapView.showInFrame();
        showInFrame.setLocation(350, 0);
    }
}
