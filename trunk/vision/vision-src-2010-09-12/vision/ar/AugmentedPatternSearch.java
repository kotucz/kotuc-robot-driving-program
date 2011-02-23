package vision.ar;

import vision.objects.ObjectAnalysis;
import vision.objects.ImageObject;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import javax.media.j3d.Transform3D;
import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;
import vision.BasicOps;
import vision.CameraProportions;
import vision.objects.MegaSuperObjectAnalysis;

/**
 *
 * @author Kotuc
 */
public class AugmentedPatternSearch {

    private CameraProportions camProp = CameraProportions.getCameraProportions();
    private ObjectAnalysis objectAnalysis = new ObjectAnalysis();

    /**
     * Computes marker location using multiple equations with multiple variables.
     * Inprecise method. Faulty
     *
     * @param input
     * @return
     */
    BufferedImage infFindSquare(BufferedImage input) {
        BufferedImage bi = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.getGraphics();
        g.drawImage(input, 0, 0, null);

        bi = BasicOps.treshold(bi, 150);

        for (ImageObject obj : objectAnalysis.findObjects(bi)) {
            g.setColor(Color.RED);
            g.drawRect(obj.getMinX(), obj.getMinY(), obj.getWidth(), obj.getHeight());

            g.setColor(Color.GREEN);
            for (Point p1 : obj.getCorners()) {
                for (Point p2 : obj.getCorners()) {
                    g.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
            }

            int iwhalf = input.getWidth() / 2;
            int ihhalf = input.getHeight() / 2;
            int w = (int) (iwhalf * 0.9);

            double vectA = new Vector3d((double) obj.getCorners()[0].x - iwhalf, (double) obj.getCorners()[0].y - ihhalf, w).length();
            double vectB = new Vector3d((double) obj.getCorners()[1].x - iwhalf, (double) obj.getCorners()[1].y - ihhalf, w).length();
            double vectC = new Vector3d((double) obj.getCorners()[2].x - iwhalf, (double) obj.getCorners()[2].y - ihhalf, w).length();
            double vectD = new Vector3d((double) obj.getCorners()[3].x - iwhalf, (double) obj.getCorners()[3].y - ihhalf, w).length();

            g.drawString("A " + vectA, obj.getCorners()[0].x, obj.getCorners()[0].y);
            g.drawString("B " + vectB, obj.getCorners()[1].x, obj.getCorners()[1].y);
            g.drawString("C " + vectC, obj.getCorners()[2].x, obj.getCorners()[2].y);
            g.drawString("D " + vectD, obj.getCorners()[3].x, obj.getCorners()[3].y);

            double sizea = 0.10;
            double sizeb = sizea;
            double sizec = sizea;
            double sized = sizea;

            // right side determinant
            double detP = new Matrix4d(
                    -vectA, vectB, 0, 0,
                    0, -vectB, vectC, 0,
                    0, 0, -vectC, vectD,
                    vectA, 0, 0, -vectD).determinant();

            System.out.println("detP " + detP);

            double detA = new Matrix4d(
                    sizea, vectB, 0, 0,
                    sizeb, -vectB, vectC, 0,
                    sizec, 0, -vectC, vectD,
                    sized, 0, 0, -vectD).determinant();

            System.out.println("detA " + detA);

            double detB = new Matrix4d(
                    -vectA, sizea, 0, 0,
                    0, sizeb, vectC, 0,
                    0, sizec, -vectC, vectD,
                    vectA, sized, 0, -vectD).determinant();

            double detC = new Matrix4d(
                    -vectA, vectB, sizea, 0,
                    0, -vectB, sizeb, 0,
                    0, 0, sizec, vectD,
                    vectA, 0, sized, -vectD).determinant();


            double detD = new Matrix4d(
                    -vectA, vectB, 0, sizea,
                    0, -vectB, vectC, sizeb,
                    0, 0, -vectC, sizec,
                    vectA, 0, 0, sized).determinant();


            g.setColor(Color.BLUE);
//            g.drawString("A " + detA / detP, obj.getCorners()[0].x, obj.getCorners()[0].y);
//            g.drawString("B " + detB / detP, obj.getCorners()[1].x, obj.getCorners()[1].y);
//            g.drawString("C " + detC / detP, obj.getCorners()[2].x, obj.getCorners()[2].y);
//            g.drawString("D " + detD / detP, obj.getCorners()[3].x, obj.getCorners()[3].y);

        }
        return bi;
    }

    /**
     * Next bad attempt.
     * Computes 2D rotation of the marker.
     *
     * @param input
     * @return
     */
    BufferedImage findSquare(BufferedImage input) {
        BufferedImage bi = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.getGraphics();
        g.drawImage(input, 0, 0, null);

//        bi = BasicOps.treshold(bi, 150);

//        for (ImageObject obj : objectAnalysis.findObjects(bi)) {
        for (ImageObject obj : new MegaSuperObjectAnalysis().findObjects(bi)) {
            g.setColor(Color.RED);
            g.drawRect(obj.getMinX(), obj.getMinY(), obj.getWidth(), obj.getHeight());

            g.setColor(Color.GREEN);
            for (Point p1 : obj.getCorners()) {
                for (Point p2 : obj.getCorners()) {
                    g.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
            }

            double iwhalf = input.getWidth() / 2;
            double ihhalf = input.getHeight() / 2;
            double w = input.getWidth();// 0.9 * iwhalf;

            Vector3d vectA = new Vector3d((double) obj.getCorners()[0].x - iwhalf, (double) -obj.getCorners()[0].y + ihhalf, w);
            Vector3d vectB = new Vector3d((double) obj.getCorners()[1].x - iwhalf, (double) -obj.getCorners()[1].y + ihhalf, w);
            Vector3d vectC = new Vector3d((double) obj.getCorners()[2].x - iwhalf, (double) -obj.getCorners()[2].y + ihhalf, w);
            Vector3d vectD = new Vector3d((double) obj.getCorners()[3].x - iwhalf, (double) -obj.getCorners()[3].y + ihhalf, w);

            vectA.scale(1 / w);
            vectB.scale(1 / w);
            vectC.scale(1 / w);
            vectD.scale(1 / w);

            double vectAlength = vectA.length();
            double vectBlength = vectB.length();
            double vectClength = vectC.length();
            double vectDlength = vectD.length();

//            g.drawString("A " + vectA, obj.getCorners()[0].x, obj.getCorners()[0].y);
//            g.drawString("B " + vectB, obj.getCorners()[1].x, obj.getCorners()[1].y);
//            g.drawString("C " + vectC, obj.getCorners()[2].x, obj.getCorners()[2].y);
//            g.drawString("D " + vectD, obj.getCorners()[3].x, obj.getCorners()[3].y);

            double sizeA = 0.10;
            double sizeB = sizeA;
            double sizec = sizeA;
            double sizeD = sizeA;

            // right side determinant
            double detP = new Matrix4d(
                    -vectAlength, vectBlength, 0, 0,
                    0, -vectBlength, vectClength, 0,
                    0, 0, -vectClength, vectDlength,
                    vectAlength, 0, 0, -vectDlength).determinant();

            System.out.println("detP " + detP);

            double zD = 1.0; // 1 m - constant

            double zA = (sizeD + vectDlength * zD) / vectAlength;
            double zB = (sizeA + vectAlength * zA) / vectBlength;
            double zC = (sizeB + vectBlength * zB) / vectClength;

            // real positions
            vectA.scale(zA);
            vectB.scale(zB);
            vectC.scale(zC);
            vectD.scale(zD);

            g.setColor(Color.BLUE);

            g.drawString("A " + zA, obj.getCorners()[0].x, obj.getCorners()[0].y);
            g.drawString("B " + zB, obj.getCorners()[1].x, obj.getCorners()[1].y);
            g.drawString("C " + zC, obj.getCorners()[2].x, obj.getCorners()[2].y);
            g.drawString("D " + zD, obj.getCorners()[3].x, obj.getCorners()[3].y);

            g.fillOval(obj.getCorners()[0].x, obj.getCorners()[0].y, (int) (10.0 / zA), (int) (10.0 / zA));
            g.fillOval(obj.getCorners()[1].x, obj.getCorners()[1].y, (int) (10.0 / zB), (int) (10.0 / zB));
            g.fillOval(obj.getCorners()[2].x, obj.getCorners()[2].y, (int) (10.0 / zC), (int) (10.0 / zC));
            g.fillOval(obj.getCorners()[3].x, obj.getCorners()[3].y, (int) (10.0 / zD), (int) (10.0 / zD));

//            g.drawLine();

            Vector3d center = new Vector3d();
            center.add(vectA);
            center.add(vectB);
            center.add(vectC);
            center.add(vectD);
            center.scale(0.25);

            //center.x;
            //center.y *= 1.0;
            center.z *= -1.0;

            this.pos = new Point3d(center);


            Vector3d dtoa = new Vector3d(vectA);
            dtoa.sub(vectD);
            Vector3d dtoc = new Vector3d(vectC);
            dtoc.sub(vectD);
            Vector3d atob = new Vector3d(vectB);
            atob.sub(vectA);
            Vector3d ctob = new Vector3d(vectB);
            ctob.sub(vectC);
            Vector3d normal = new Vector3d();
            normal.cross(dtoc, dtoa);

            Transform3D td = new Transform3D();

            // lookat point
//            td.lookAt(new Point3d(), new Point3d(normal), atob);


// oriented angle
//            normal.normalize();
//            td.set(new Quat4d(normal.x, normal.y, normal.z, 0));

            this.pos = new Point3d(0, 0, -2);

            // rotZ only - ok
            td.rotZ(Math.atan2(dtoc.y, dtoc.x));

            rott = td;
        }
        return bi;
    }
    private Transform3D rott;

    public Transform3D getRott() {
        return rott;
    }
    private PatternMatcher pmatcher = new PatternMatcher();

    /**
     * Computes marker location using multiple equations with multiple variables.
     * @param input
     * @return
     */
    public BufferedImage paintWireCubeOnSquare(BufferedImage input) {
        BufferedImage bi = BasicOps.copy(input);
        Graphics g = input.getGraphics();
//        g.drawImage(input, 0, 0, null);
        //bi = BasicOps.treshold(bi, 50);

        int xoff = 0;
//        for (ImageObject obj : objectAnalysis.findObjects(bi)) {
        for (ImageObject obj : new MegaSuperObjectAnalysis().findObjects(bi)) {

//            g.setColor(Color.RED);
//            g.drawRect(obj.getMinX(), obj.getMinY(), obj.getWidth(), obj.getHeight());

//            BufferedImage sample = obj.getPattern(input);
//            sample = BasicOps.treshold(sample, 150);
//            if (pmatcher.recognize(sample) == null) {
//                continue;
//            } else {
//                g.drawImage(sample, 0, 0, null);
//                g.setColor(Color.RED);
//                g.drawRect(0, 0, 50, 50);
//            }

            Pattern sampl = obj.getPattern();
//            BasicOps.treshold(sampl, 150);
            final int psize = 30;
            g.drawImage(sampl.getImage(), xoff, 0, psize, psize, null);
            Pattern recog = pmatcher.recognize(sampl);
            if (recog != null) {
                g.drawImage(recog.getImage(), xoff, psize, psize, psize, null);
                g.setColor(Color.GREEN);
                g.drawString("" + recog.getName(), xoff, psize * 2);
            }
            xoff += psize;
            if (recog == null) {
                continue;
            }

            Vector2d[] axes = obj.getAxes2d();
            g.setColor(Color.GREEN);
            for (Point p1 : obj.getCorners()) {
                for (Point p2 : obj.getCorners()) {
                    g.drawLine(p1.x, p1.y, p2.x, p2.y);
                    g.drawLine(p1.x, p1.y, (int) axes[0].x, (int) axes[0].y);
                    g.drawLine(p1.x, p1.y, (int) axes[1].x, (int) axes[1].y);
                }
            }




            // computing z-coordinages

            Vector3d vectA = camProp.getProjectionVectorPixels(obj.getCorners()[0].x, obj.getCorners()[0].y);
            Vector3d vectB = camProp.getProjectionVectorPixels(obj.getCorners()[1].x, obj.getCorners()[1].y);
            Vector3d vectC = camProp.getProjectionVectorPixels(obj.getCorners()[2].x, obj.getCorners()[2].y);
            Vector3d vectD = camProp.getProjectionVectorPixels(obj.getCorners()[3].x, obj.getCorners()[3].y);

            double vectAlength = vectA.length();
            double vectBlength = vectB.length();
            double vectClength = vectC.length();
            double vectDlength = vectD.length();

//            g.drawString("A " + vectA, obj.getCorners()[0].x, obj.getCorners()[0].y);
//            g.drawString("B " + vectB, obj.getCorners()[1].x, obj.getCorners()[1].y);
//            g.drawString("C " + vectC, obj.getCorners()[2].x, obj.getCorners()[2].y);
//            g.drawString("D " + vectD, obj.getCorners()[3].x, obj.getCorners()[3].y);

            double sizeA = 0.10;
            double sizeB = sizeA;
            double sizec = sizeA;
            double sizeD = sizeA;

            // right side determinant
            double detP = new Matrix4d(
                    -vectAlength, vectBlength, 0, 0,
                    0, -vectBlength, vectClength, 0,
                    0, 0, -vectClength, vectDlength,
                    vectAlength, 0, 0, -vectDlength).determinant();

            System.out.println("detP " + detP);

            double zD = 1; // 1 m - constant

            double zA = (sizeD + vectDlength * zD) / vectAlength;
            double zB = (sizeA + vectAlength * zA) / vectBlength;
            double zC = (sizeB + vectBlength * zB) / vectClength;

            vectA.scale(zA);
            vectB.scale(zB);
            vectC.scale(zC);
            vectD.scale(zD);

            g.setColor(Color.BLUE);

            g.drawString("A " + zA, obj.getCorners()[0].x, obj.getCorners()[0].y);
            g.drawString("B " + zB, obj.getCorners()[1].x, obj.getCorners()[1].y);
            g.drawString("C " + zC, obj.getCorners()[2].x, obj.getCorners()[2].y);
            g.drawString("D " + zD, obj.getCorners()[3].x, obj.getCorners()[3].y);

            Vector3d center = new Vector3d();
            center.add(vectA);
            center.add(vectB);
            center.add(vectC);
            center.add(vectD);
            center.scale(0.25);
            this.pos = new Point3d(center);

            // end computing z-coords

            // draw tilts
            g.setColor(Color.RED);
            g.drawLine((int) (vectA.z), 100 + (int) (vectA.y),
                    (int) (vectC.z), 100 + (int) (vectC.y));
            g.drawLine(100 + (int) (vectD.x), (int) (vectD.z),
                    100 + (int) (vectB.x), (int) (vectB.z));


            // painting wired cube
            g.setColor(Color.BLUE);
            Vector3d dtoa = new Vector3d();
            dtoa.sub(vectA, vectD);
            Vector3d dtoc = new Vector3d();
            dtoc.sub(vectC, vectD);
            Vector3d normal = new Vector3d();
            normal.cross(dtoc, dtoa);
            normal.scale(100 / normal.length());
            // painting
            if (true) {

                final double w = 320 / 0.45;

                Vector3d normalEnd = new Vector3d(center);
                normalEnd.add(normal);

                Point centerP = camProp.getProjectionImagePoint(new Point3d(center));
                Point normalEndP = camProp.getProjectionImagePoint(new Point3d(normalEnd));
                g.fillOval(centerP.x, centerP.y, 5, 5);
                g.drawLine(centerP.x, centerP.y, normalEndP.x, normalEndP.y);

                Vector3d cornerA = new Vector3d(vectA);
                cornerA.add(normal);
                Point cornerAP = camProp.getProjectionImagePoint(new Point3d(cornerA));
                Point vectAP = camProp.getProjectionImagePoint(new Point3d(vectA));
                g.drawLine(vectAP.x, vectAP.y, cornerAP.x, cornerAP.y);

                Vector3d cornerB = new Vector3d(vectB);
                cornerB.add(normal);
                Point cornerBP = camProp.getProjectionImagePoint(new Point3d(cornerB));
                Point vectBP = camProp.getProjectionImagePoint(new Point3d(vectB));
                g.drawLine(vectBP.x, vectBP.y, cornerBP.x, cornerBP.y);


                Vector3d cornerC = new Vector3d(vectC);
                cornerC.add(normal);
                Point cornerCP = camProp.getProjectionImagePoint(new Point3d(cornerC));
                Point vectCP = camProp.getProjectionImagePoint(new Point3d(vectC));
                g.drawLine(vectCP.x, vectCP.y, cornerCP.x, cornerCP.y);



                Vector3d cornerD = new Vector3d(vectD);
                cornerD.add(normal);
                Point cornerDP = camProp.getProjectionImagePoint(new Point3d(cornerD));
                Point vectDP = camProp.getProjectionImagePoint(new Point3d(vectD));
                g.drawLine(vectDP.x, vectDP.y, cornerDP.x, cornerDP.y);

                g.drawLine(cornerAP.x, cornerAP.y, cornerDP.x, cornerDP.y);
                g.drawLine(cornerAP.x, cornerAP.y, cornerBP.x, cornerBP.y);
                g.drawLine(cornerCP.x, cornerCP.y, cornerDP.x, cornerDP.y);
                g.drawLine(cornerCP.x, cornerCP.y, cornerBP.x, cornerBP.y);

                System.out.println("Center: " + center + " normal: " + normal);
            }

        }
//        return bi;
        return input;
    }
    private Transform3D cubeTrans = new Transform3D();

    public Transform3D getTrans() {
        return cubeTrans;
    }

    /**
     * Computes exact 3D location of marker using perpective model based
     * on parallels.
     * The marker opposite sides are parallel and the intersection
     * of their projection shows the projection vector in the image, which has
     * known direction. X and Y axis are computed from the marker sides,
     * Z axis is their cross product.
     *
     * @param input
     * @return
     */
    public BufferedImage paintWireCubeOnSquarePerspective(BufferedImage input) {

        BufferedImage bi = BasicOps.copy(input);
        Graphics g = input.getGraphics();
//        g.drawImage(input, 0, 0, null);
//        bi = BasicOps.treshold(bi, 100);

        int xoff = 0;
//        for (ImageObject obj : objectAnalysis.findObjects(bi)) {
        for (ImageObject obj : new MegaSuperObjectAnalysis().findObjects(bi)) {

//            BufferedImage sampl = obj.getPattern(input);
//            BasicOps.treshold(sampl, 100);

            Pattern sampl = obj.getPattern();

            final int psize = 30;
            g.drawImage(sampl.getImage(), xoff, 0, psize, psize, null);
            Pattern recog = pmatcher.recognize(sampl);
            if (recog != null) {
                g.drawImage(recog.getImage(), xoff, psize, psize, psize, null);
                g.setColor(Color.GREEN);
                g.drawString("" + recog.getName(), xoff, psize * 2);
            }
            xoff += psize;
            if (recog == null) {
                continue;
            }

            obj.focusCorners(input);

            Vector2d[] axes = obj.getAxes2d();

            Vector3d[] axes3d = obj.getAxes3d(camProp);

            this.cubeTrans = obj.getTransform(camProp);

            Vector3d transX = axes3d[0];
            Vector3d transY = axes3d[1];
            Vector3d transZ = axes3d[2];

//            Vector3d transX = camProp.getProjectionVector(axes[0].x, axes[0].y);
//            if (transX.x < 0) {
//                transX.scale(-1);
//            }
//            Vector3d transY = camProp.getProjectionVector(axes[1].x, axes[1].y);
//            if (transX.y < 0) {
//                transX.scale(-1);
//            }
//            Vector3d transZ = new Vector3d();
//            transZ.cross(transX, transY);

            for (Point p1 : obj.getCorners()) {
                for (Point p2 : obj.getCorners()) {
                    g.setColor(Color.GREEN);
                    g.drawLine(p1.x, p1.y, p2.x, p2.y);
                    g.setColor(Color.BLUE); // X axis
                    g.drawLine(p1.x, p1.y, (int) axes[0].x, (int) axes[0].y);
                    g.setColor(Color.RED); // Y axis
                    g.drawLine(p1.x, p1.y, (int) axes[1].x, (int) axes[1].y);
                }
            }

            // computing z-coordinages
            Vector3d vectA = camProp.getProjectionVectorPixels(obj.getCorners()[0].x, obj.getCorners()[0].y);
            Vector3d vectB = camProp.getProjectionVectorPixels(obj.getCorners()[1].x, obj.getCorners()[1].y);
            Vector3d vectC = camProp.getProjectionVectorPixels(obj.getCorners()[2].x, obj.getCorners()[2].y);
            Vector3d vectD = camProp.getProjectionVectorPixels(obj.getCorners()[3].x, obj.getCorners()[3].y);

//            double sizeA = 0.10;
//            double sizeB = sizeA;
//            double sizec = sizeA;
//            double sizeD = sizeA;

//            double zD = 1; // 1 m - constant
//            double zA = 1;
//            double zB = 1;
//            double zC = 1;
//
//            vectA.scale(zA);
//            vectB.scale(zB);
//            vectC.scale(zC);
//            vectD.scale(zD);

            Vector3d center = new Vector3d();
            center.add(vectA);
            center.add(vectB);
            center.add(vectC);
            center.add(vectD);
//            center.scale(0.25);
            center.normalize();
//            this.pos = new Point3d(center);

            // end computing z-coords

            // draw tilts
            g.setColor(Color.RED);
            g.drawLine(0, 100,
                    (int) (transY.z), 100 + (int) (transY.y));
//            g.drawLine(100 + (int) (vectD.x), (int) (vectD.z),
//                    100 + (int) (vectB.x), (int) (vectB.z));


            if (true) {
// painting wired cube
                // painting


                Vector3d normal = transZ;
                normal.normalize();
                Vector3d normalEnd = new Vector3d();
                normalEnd.scaleAdd(50, normal, center);

                transX.normalize();
                Vector3d axisXEnd = new Vector3d();
                axisXEnd.scaleAdd(50, transX, center);
                Point axisXEndP = camProp.getProjectionImagePoint(new Point3d(axisXEnd));

                transY.normalize();
                Vector3d axisYEnd = new Vector3d();
                axisYEnd.scaleAdd(50, transY, center);
                Point axisYEndP = camProp.getProjectionImagePoint(new Point3d(axisYEnd));

                Point centerP = camProp.getProjectionImagePoint(new Point3d(center));
                Point normalEndP = camProp.getProjectionImagePoint(new Point3d(normalEnd));
                g.fillOval(centerP.x, centerP.y, 5, 5);
                g.setColor(Color.BLUE); // x
                g.drawLine(centerP.x, centerP.y, axisXEndP.x, axisXEndP.y);
                g.setColor(Color.RED); // y
                g.drawLine(centerP.x, centerP.y, axisYEndP.x, axisYEndP.y);
                g.setColor(Color.GREEN); // z
                g.drawLine(centerP.x, centerP.y, normalEndP.x, normalEndP.y);

                g.setColor(Color.BLUE);
                Vector3d cornerA = new Vector3d();
                cornerA.scaleAdd(50, normal, vectA);
                Point cornerAP = camProp.getProjectionImagePoint(new Point3d(cornerA));
                Point vectAP = camProp.getProjectionImagePoint(new Point3d(vectA));
                g.drawLine(vectAP.x, vectAP.y, cornerAP.x, cornerAP.y);

                Vector3d cornerB = new Vector3d();
                cornerB.scaleAdd(50, normal, vectB);
                Point cornerBP = camProp.getProjectionImagePoint(new Point3d(cornerB));
                Point vectBP = camProp.getProjectionImagePoint(new Point3d(vectB));
                g.drawLine(vectBP.x, vectBP.y, cornerBP.x, cornerBP.y);


                Vector3d cornerC = new Vector3d();
                cornerC.scaleAdd(50, normal, vectC);
                Point cornerCP = camProp.getProjectionImagePoint(new Point3d(cornerC));
                Point vectCP = camProp.getProjectionImagePoint(new Point3d(vectC));
                g.drawLine(vectCP.x, vectCP.y, cornerCP.x, cornerCP.y);


                Vector3d cornerD = new Vector3d();
                cornerD.scaleAdd(50, normal, vectD);
                Point cornerDP = camProp.getProjectionImagePoint(new Point3d(cornerD));
                Point vectDP = camProp.getProjectionImagePoint(new Point3d(vectD));
                g.drawLine(vectDP.x, vectDP.y, cornerDP.x, cornerDP.y);

                g.drawLine(cornerAP.x, cornerAP.y, cornerDP.x, cornerDP.y);
                g.drawLine(cornerAP.x, cornerAP.y, cornerBP.x, cornerBP.y);
                g.drawLine(cornerCP.x, cornerCP.y, cornerDP.x, cornerDP.y);
                g.drawLine(cornerCP.x, cornerCP.y, cornerBP.x, cornerBP.y);


//                center.scale(w / center.z);
//                g.fillOval((int) center.x + iwhalf, (int) center.y + ihhalf, 5, 5);
//                normalEnd.scale(w / normalEnd.z);
//                g.drawLine((int) center.x + iwhalf, (int) center.y + ihhalf, (int) normalEnd.x + iwhalf, (int) normalEnd.y + ihhalf);
//
//                Vector3d cornerA = new Vector3d(vectA);
//                cornerA.add(normal);
//                cornerA.scale(w / cornerA.z);
//                vectA.scale(w / vectA.z);
//                g.drawLine((int) vectA.x + iwhalf, (int) vectA.y + ihhalf, (int) cornerA.x + iwhalf, (int) cornerA.y + ihhalf);
//
//                Vector3d cornerB = new Vector3d(vectB);
//                cornerB.add(normal);
//                cornerB.scale(w / cornerB.z);
//                vectB.scale(w / vectB.z);
//                g.drawLine((int) vectB.x + iwhalf, (int) vectB.y + ihhalf, (int) cornerB.x + iwhalf, (int) cornerB.y + ihhalf);
//
//
//                Vector3d cornerC = new Vector3d(vectC);
//                cornerC.add(normal);
//                cornerC.scale(w / cornerC.z);
//                vectC.scale(w / vectC.z);
//                g.drawLine((int) vectC.x + iwhalf, (int) vectC.y + ihhalf, (int) cornerC.x + iwhalf, (int) cornerC.y + ihhalf);
//
//                Vector3d cornerD = new Vector3d(vectD);
//                cornerD.add(normal);
//                cornerD.scale(w / cornerD.z);
//                vectD.scale(w / vectD.z);
//                g.drawLine((int) vectD.x + iwhalf, (int) vectD.y + ihhalf, (int) cornerD.x + iwhalf, (int) cornerD.y + ihhalf);
//
//                g.drawLine((int) cornerA.x + iwhalf, (int) cornerA.y + ihhalf, (int) cornerD.x + iwhalf, (int) cornerD.y + ihhalf);
//                g.drawLine((int) cornerA.x + iwhalf, (int) cornerA.y + ihhalf, (int) cornerB.x + iwhalf, (int) cornerB.y + ihhalf);
//                g.drawLine((int) cornerC.x + iwhalf, (int) cornerC.y + ihhalf, (int) cornerD.x + iwhalf, (int) cornerD.y + ihhalf);
//                g.drawLine((int) cornerC.x + iwhalf, (int) cornerC.y + ihhalf, (int) cornerB.x + iwhalf, (int) cornerB.y + ihhalf);

//                System.out.println("Center: " + center + " normal: " + normal);
            }

        }
//        return bi;
        return input;
    }

    public BufferedImage findPatterns(BufferedImage input) {
        BufferedImage output = BasicOps.copy(input);
        Graphics g = output.getGraphics();
////        g.drawImage(input, 0, 0, null);

//        output = BasicOps.treshold(output, 150);

        int xoff = 0;
//        for (ImageObject obj : objectAnalysis.findObjects(output)) {
        for (ImageObject obj : new MegaSuperObjectAnalysis().findObjects(output)) {

//            obj.focusCorners(input);

            g.setColor(Color.YELLOW);
            g.drawRect(obj.getMinX(), obj.getMinY(), obj.getWidth(), obj.getHeight());

            g.setColor(Color.RED);
            for (Point p1 : obj.getCorners()) {
                g.drawRect(p1.x - 1, p1.y - 1, 3, 3);
            }

            g.setColor(Color.GREEN);
            for (Point p1 : obj.getCorners()) {
                for (Point p2 : obj.getCorners()) {
                    g.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
            }


//            BufferedImage sampl = obj.getPattern(input);
//            BasicOps.treshold(sampl, 150);
            Pattern sampl = obj.getPattern();
            final int psize = 30;
            g.drawImage(sampl.getImage(), xoff, 0, psize, psize, null);
            Pattern recog = pmatcher.recognize(sampl);
            if (recog != null) {
                g.drawImage(recog.getImage(), xoff, psize, psize, psize, null);
                g.setColor(Color.GREEN);
                g.drawString("" + recog.getName(), xoff, psize * 2);
            }
            xoff += psize;

        }
        return output;
    }
    private Point3d pos;

    public Point3d getPos() {
        return pos;
    }
}
