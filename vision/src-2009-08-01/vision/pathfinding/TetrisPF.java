package vision.pathfinding;

import java.awt.image.BufferedImage;

/**
 * noninstantible
 * @author Tomas Kotula
 */
public final class TetrisPF {

    private TetrisPF() {
        
    }

    public static float tetrisCM(BufferedImage i3) {
                        
        BufferedImage hm = HeatMap.getHeatMapCM(i3).createRoadBnWImage();
        
        for (int x = 0; x < hm.getWidth(); x++) {
            int i = hm.getHeight()-1;
            for (int y = hm.getHeight()-1; y >= 0; y--) {
                if ((hm.getRGB(x, y)&0xFF)>200) {
                    i3.setRGB(x, i, i3.getRGB(x, y));
                    i--;
                }
            }
            for (;  i>=0; i--) {
                i3.setRGB(x, i, 0xFF000000);
            }
        }
        
        return 1;
    }
    
    
}
