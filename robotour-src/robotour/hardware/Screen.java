/*
 * Screen.java
 *
 * Created on 14. srpen 2007, 17:57
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package robotour.hardware;

import robotour.util.Shutdownable;
import robotour.util.ShutdownManager;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Window;

/**
 *
 * @author PC
 */
public class Screen implements Shutdownable {
    
    /** Creates a new instance of Screen */
    public Screen() {
//        super("Screen");
        myScreen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        ShutdownManager.registerStutdown(this);
    }
    
    private GraphicsDevice myScreen;
    
    private Window window = null;    
   
    public void setFullScreenWindow(Window w) {
        try {
            myScreen.setFullScreenWindow(w);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * @return
     */
    public Dimension getDimension() {
        try {
            return new Dimension(myScreen.getDisplayMode().getWidth(), myScreen.getDisplayMode().getHeight());
        } catch (Exception ex) {
            return new Dimension(800, 600);
        }
    }
    
    
    @Override
    public void shutdown() {
        myScreen.setFullScreenWindow(null);
    }
    
}
