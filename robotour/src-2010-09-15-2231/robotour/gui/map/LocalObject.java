package robotour.gui.map;

import java.awt.Color;

public class LocalObject {

    private final LocalPoint point;
    private final double size;
    private final Color color;

    public LocalObject(LocalPoint point, double size, Color color) {
        super();
        this.point = point;
        this.size = size;
        this.color = color;
    }

    public LocalPoint getPoint() {
        return point;
    }

    public double getSize() {
        return size;
    }

    public Color getColor() {
        return color;
    }
}
