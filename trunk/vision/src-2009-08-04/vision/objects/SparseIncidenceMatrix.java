package vision.objects;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Kotuc
 */
public class SparseIncidenceMatrix {

    Map<Point, Integer> incidences = new HashMap<Point, Integer>();

    private Point toIndex(int index1, int index2) {
        return (index1 < index2) ? new Point(index1, index2) : new Point(index2, index1);
    }

    public void addIncidence(int index1, int index2) {
        Point coord = toIndex(index1, index2);
        incidences.put(coord, getIncidences(index1, index2) + 1);
    }

    public void mergeIndices(int index1, int index2) {
        // TODO
    }

    public int getIncidences(int index1, int index2) {
        Point coord = toIndex(index1, index2);
        if (!incidences.containsKey(coord)) {
            return 0;
        }
        return incidences.get(coord);
    }
}
