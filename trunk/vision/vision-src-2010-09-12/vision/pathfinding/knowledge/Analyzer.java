/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vision.pathfinding.knowledge;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 *
 * @author Kotuc
 */
public class Analyzer {
    public Info characteristics(BufferedImage image, Rectangle rect) {
        Info info = new Info();
        info.initFrom(image, rect);
        return info;
    }
}
