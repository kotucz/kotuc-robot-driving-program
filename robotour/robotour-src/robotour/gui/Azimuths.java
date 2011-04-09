package robotour.gui;

import robotour.navi.basic.Azimuth;

/**
 *
 * @author Kotuc
 */
public enum Azimuths {
    NORTH(0),
    NE(45),
    EAST(90),
    SE(135),
    SOUTH(180),
    SW(225),
    WEST(270),
    NW(315);

    private Azimuths(int degs) {
        this.azimuth = Azimuth.valueOfDegrees(degs);
    }

    private final Azimuth azimuth;

    public Azimuth azimuth() {
        return azimuth;
    }



}
