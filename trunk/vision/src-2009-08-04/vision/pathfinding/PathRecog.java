package vision.pathfinding;

import java.awt.image.BufferedImage;

/**
 * Searches for left and right road corner. Computes best way though the center of the road.
 * @author Kotuc
 */
public class PathRecog {

    private int iw,  ih;
    private BufferedImage image;

    public BufferedImage getResult() {
        return image;
    }
    private int average = -1;

    public int getAverage() {
        if (average == -1) {
            throw new IllegalStateException("nothing processed");
        }
        return average;
    }

    /**
     *
     * @return
     */
    public double getGoodSteer() {
        return ((2.0 * getAverage()) / (double) iw) - 1.0;
    }

    public void process(BufferedImage src) {

        iw = src.getWidth();
        ih = src.getHeight();

//        HeatMap hm = HeatMap.getHeatMapCM(src);
//        HeatMap hm = HeatMap.getHeatMap2_2(src);
//        HeatMap hm = HeatMap.createHeatMap(src, HeatMap.SATURATION_HEAT_FUNCTION);
        HeatMap hm = HeatMap.createHeatMap(src, HeatMap.HUE_HEAT_FUNCTION);

        image = hm.createRoadBnWImage();

        int leftBorder, rightBorder, center = iw / 2;

        int centerSum = 0;

        for (int y = ih - 1; y >= 0; y--) {
            rightBorder = center;
            while (rightBorder < iw - 1 && hm.isRoad(rightBorder, y)) {
                rightBorder++;
            }
            leftBorder = center;
            while (leftBorder > 0 && hm.isRoad(leftBorder, y)) {
                leftBorder--;
            }
            center = (leftBorder + rightBorder) / 2;
            centerSum += center;
            image.setRGB(leftBorder, y, 0xFFFF0000);
            image.setRGB(rightBorder, y, 0xFFFF0000);
            image.setRGB(center, y, 0xFF0000FF);
        }
        this.average = centerSum / ih;
        System.out.println("Avg: " + getAverage() +
                "\tSteer: " + getGoodSteer());

    }
}
