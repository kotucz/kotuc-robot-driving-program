package robotour.navi.basic;

import javax.vecmath.Vector2d;

/**
 * Created by IntelliJ IDEA.
 * User: Kotuc
 * Date: 23.3.12
 * Time: 21:12
 * To change this template use File | Settings | File Templates.
 * WARNING It is not recomended to expect this class will still extend Vector2d.
 * Vector is direction independent of position. As opposed to Point, which is a point without direction.
 */
public class Vector extends Vector2d {
    public Vector(double dx, double dy) {
        x = dx;
        y = dy;
        //To change body of created methods use File | Settings | File Templates.
    }

}
