package robotour.navi.basic;

/**
 * Created by IntelliJ IDEA.
 * User: Kotuc
 * Date: 27.3.12
 * Time: 19:52
 * To change this template use File | Settings | File Templates.
 *
 * TODO once we settle down which local coordinates should the vehicle use, we should use heading instead of Azimuth
 *
 */
public class Heading extends Angle {

    private Heading(double radians) {
        super(radians);
    }

    static Heading fromRadians(double radians) {
        return new Heading(radians);
    }

}
