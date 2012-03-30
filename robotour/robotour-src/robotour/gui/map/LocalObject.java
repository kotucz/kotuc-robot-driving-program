package robotour.gui.map;

import robotour.navi.basic.Point;

import java.awt.Color;

public class LocalObject {

    private final Point point;
    private final double size;
    private final Color color;

    public LocalObject(Point point, double size, Color color) {
        super();
        this.point = point;
        this.size = size;
        this.color = color;
    }

    public Point getPoint() {
        return point;
    }

    public double getSize() {
        return size;
    }

    public Color getColor() {
        return color;
    }
}
