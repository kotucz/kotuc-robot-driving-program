package robotour.util;

import robotour.iface.MeasureException;
import robotour.iface.Sonar;
import robotour.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author kotuc
 */
public class Sonars {

    private final Sonar center,  left,  right;

    private final List<Sonar> sonars;
    public Sonars(Sonar left, Sonar center, Sonar right) {
        this.center = center;
        this.left = left;
        this.right = right;
        List<Sonar> slist = new ArrayList<Sonar>();
        slist.add(left);
        slist.add(center);
        slist.add(right);
        sonars = Collections.unmodifiableList(slist);
    }

    public Sonar getCenter() {
        return center;
    }

    public Sonar getLeft() {
        return left;
    }

    public Sonar getRight() {
        return right;
    }

    public Collection<Sonar> getSonars() {
        return sonars;
    }

//    public double getLeftDistance() throws MeasureException {
//        return left.getDistance();
//    }
//
//    public double getCenterDistance() throws MeasureException {
//        return center.getDistance();
//    }
//
//    public double getRightDistance() throws MeasureException {
//        return right.getDistance();
//    }

    static String distToString(Sonar sonar) {
        try {
            return "" + (int) (sonar.getDistance() * 100);
        } catch (MeasureException ex) {
            return "ERR";
        }
    }

    @Override
    public String toString() {
        return "Sonars:\t" + distToString(left) + "\t" + distToString(center) + "\t" + distToString(right) + "";
    }
}
