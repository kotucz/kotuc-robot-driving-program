package vision;

import vision.objects.ObjectAnalysis;
import vision.objects.SuperObjectAnalysis;
import vision.pathfinding.TetrisPF;
import vision.pathfinding.HeatMap;
import vision.pathfinding.PathRecog;
import vision.ar.Augmented3DPaint;
import vision.ar.AugmentedPatternSearch;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import javax.media.j3d.Transform3D;
import javax.vecmath.Point3d;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import vision.ar.CornerDetector;
import vision.imagej.HSB;
import vision.objects.MegaSuperObjectAnalysis;
import vision.pathfinding.FloorProjection;

/**
 * noninstantible test utils not thread safe
 * @author Tomas Kotula
 */
final class Tests {

    private Tests() {
    }

    static BufferedImage heatMapCM(BufferedImage input) {
        return HeatMap.getHeatMapCM(input).createHeatImage(HeatMap.ROAD_WHITE_OTHER_BLACK);
    }

    static BufferedImage superObjectAnalysis(BufferedImage input) {
        return new SuperObjectAnalysis().findObjects(input);
    }

    static BufferedImage megaSuperObjectAnalysis(BufferedImage input) {
        return new MegaSuperObjectAnalysis().colorize(input);
    }

    static BufferedImage roundColors(BufferedImage input) {
        return objectAnalysis.roundColors(input);
    }

    static BufferedImage segmentBorders(BufferedImage input) {
        objectAnalysis.analyze(objectAnalysis.roundColors(input));
        return input;
    }

    static BufferedImage edgesConvolution(BufferedImage input) {
        return BasicOps.convolve(input, 3);
    }

    static BufferedImage putDown(BufferedImage input) {
        return FloorProjection.putDown(input);
    }

    static BufferedImage spectrograph(BufferedImage input) {
        return Spectrograph.spectrograph(input);
    }

    static BufferedImage findWhiteLine(BufferedImage input) {
        BufferedImage bi = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_INT_ARGB);
        bi.getGraphics().drawImage(input, 0, 0, null);
        Graphics g = bi.getGraphics();
        bi = new ObjectAnalysis().roundColors(bi);
        new ObjectAnalysis().findWhiteVericalLine(bi);
        return bi;
    }

    static BufferedImage findWhiteBall(BufferedImage input) {
        BufferedImage bi = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_INT_ARGB);
        bi.getGraphics().drawImage(input, 0, 0, null);
        Graphics g = bi.getGraphics();
        bi = new ObjectAnalysis().roundColors(bi);
        new ObjectAnalysis().analyze(bi);
        return bi;
    }

    static BufferedImage findBalls(BufferedImage input) {
        BufferedImage bi = BasicOps.copy(input);
//        bi = new ObjectAnalysis().roundColors(bi);
        bi = new SuperObjectAnalysis().findObjects(bi);
        return new ObjectAnalysis().findBalls(bi);
    }

    static BufferedImage getTableRB(BufferedImage input) {

        BufferedImage dest = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);

        int x = 0, y = 0, i = 0;

        for (y = 0; y < 255; y++) {
            for (x = 0; x < 255; x++) {
                int rgb = (y << 16) + (x << 8) + x;
                dest.setRGB(x, y, 0xFF000000 + rgb);
            }
        }

        return dest;

    }

    static BufferedImage getTable3(BufferedImage input) {

        BufferedImage dest = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);

        int x = 0, y = 0, i = 0;

        for (y = 0; y < 255; y++) {
            for (x = 0; x < 255; x++) {

                Vector2f vr = new Vector2f(x, y);
                Vector2f vg = new Vector2f(x - 255, y);
                Vector2f vb = new Vector2f(x, y - 255);

                Vector3f v3 = new Vector3f(255 - vr.length(), 255 - vg.length(), 255 - vb.length());
                v3.normalize();

//                Color3f cv = new Color3f(v3);

                int rgb = ((0xFF & (int) (255 * v3.x)) << 16) + ((0xFF & (int) (255 * v3.y)) << 8) + (0xFF & (int) (255 * v3.z));
                dest.setRGB(x, y, 0xFF000000 + rgb);
            }
        }

        return dest;

    }

    static BufferedImage getCM_A(BufferedImage input) {

        BufferedImage dest = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);

        int x = 0, y = 0, i = 0;

        for (y = 0; y < 255; y++) {
            for (x = 0; x < 255; x++) {

                Vector2f vr = new Vector2f(x, y);
                Vector2f vg = new Vector2f(x - 255, y);
                Vector2f vb = new Vector2f(x, y - 255);

                Vector3f v3 = new Vector3f(255 - vr.length(), 255 - vg.length(), 255 - vb.length());
                v3.normalize();

//                Color3f cv = new Color3f(v3);

                int rgb = ((0xFF & (int) (255 * v3.x)) << 16) + ((0xFF & (int) (255 * v3.y)) << 8) + (0xFF & (int) (255 * v3.z));
                dest.setRGB(x, y, 0xFF000000 + rgb);
            }
        }

        return dest;

    }

    static BufferedImage tetris(BufferedImage input) {
//        BufferedImage image = OldWomen.groundWoman1(input);
        TetrisPF.tetrisCM(input);
        return input;
    }
    private static MotionDetect motionDetector = new MotionDetect();

    static BufferedImage motion(BufferedImage input) {
//        if (motionDetector==null) {
//            motionDetector = new MotionDetect();
//        }
        return motionDetector.getMotion(input);
    }
    private static ObjectAnalysis objectAnalysis = new ObjectAnalysis();

    static BufferedImage shape(BufferedImage input) {
        BufferedImage bi = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_INT_ARGB);
        bi.getGraphics().drawImage(input, 0, 0, null);
        Graphics g = bi.getGraphics();
        objectAnalysis.roundColors(bi);
        objectAnalysis.analyze(bi);
        return bi;
    }
    private static PathRecog precog = new PathRecog();

    static BufferedImage pathRecog(BufferedImage input) {
        precog.process(BasicOps.getResizedImage(input, 320, 240));
        return precog.getResult();
    }

    static BufferedImage toHSB(BufferedImage input) {
        return HSBConvertor.rgbToHSB(input);
    }

    static BufferedImage toSphere(BufferedImage input) {
        return SphericalTransform.toSphere(input);
    }

    static BufferedImage fromSphere(BufferedImage input) {
        return SphericalTransform.fromSphere(input);
    }

    static BufferedImage grayscale(BufferedImage input) {
        BufferedImage bi = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        bi.getGraphics().drawImage(input, 0, 0, null);
        return bi;
    }
    private static AugmentedPatternSearch aps = new AugmentedPatternSearch();

    static BufferedImage augmentedCorners(BufferedImage input) {
//        BasicOps.treshold(input, 125);
        return CornerDetector.findCorners(input);
    }

//    static BufferedImage infFindSquare(BufferedImage input) {
//        return aps.infFindSquare(input);
//    }

    static BufferedImage paintWireCubeOnSquare(BufferedImage input) {
        return aps.paintWireCubeOnSquare(input);
    }

    static BufferedImage paintPerspectiveCube(BufferedImage input) {
        return aps.paintWireCubeOnSquarePerspective(input);
    }

    static BufferedImage paintPerspectiveCube3D(BufferedImage input) {
        aps.paintWireCubeOnSquarePerspective(input);
        augPaint.getCraft().setPos(new Point3d());
        augPaint.getCraft().setRot(aps.getTrans());
        System.out.println("transform:");
        System.out.println(aps.getTrans());
        return augPaint.getImage(input);
    }
    private static Augmented3DPaint augPaint = new Augmented3DPaint();

    /**
     * Shows rotating Craft3D.
     * @param input
     * @return
     */
    static BufferedImage paint3D(BufferedImage input) {
        augPaint.getCraft().setPos(new Point3d(0, 0, -2.5));
        Transform3D trot = new Transform3D();
        trot.rotY(System.currentTimeMillis()/1000.0);
        augPaint.getCraft().setRot(trot);
        return augPaint.getImage(input);
    }

//    static BufferedImage searchSquareAndPaint3D(BufferedImage input) {
//        input = aps.findSquare(input);
////        augPaint.setPos(new Point3d(0, 0, -1));
//        augPaint.getCraft().setPos(aps.getPos());
//        augPaint.getCraft().setRot(aps.getRott());
//        System.out.println("pos: " + aps.getPos());
//        return augPaint.getImage(input);
//    }

    static BufferedImage searchPattern(BufferedImage input) {
        return aps.findPatterns(input);
    }

    static BufferedImage camCalibration(BufferedImage input) {
        if (augPaint == null) {
            augPaint = new Augmented3DPaint();
        }
        augPaint.getCraft().setPos(new Point3d(0, 0, -2.5));
        augPaint.getCraft().setRot(new Transform3D());
        input = augPaint.getImage(input);
        Graphics g = input.getGraphics();
        g.setColor(Color.GREEN);
        Point p = CameraProportions.getCameraProportions().getProjectionImagePoint(new Point3d(0.5, 0.5, -2.49));
        g.drawLine(-1000, p.y, 1000, p.y);
        g.drawLine(p.x, -1000, p.x, 1000);
        return input;
    }

    static BufferedImage rgbAutoTreshold(BufferedImage image) {
        return HSB.rgbAutoTreshold(image);
    }
}
