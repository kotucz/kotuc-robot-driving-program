package vision.ar;

import java.util.Arrays;
import javax.media.j3d.Transform3D;
import javax.vecmath.Matrix4d;
import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

/**
 *
 * @author Kotuc
 */
public class AugmentedTransformTest {

    private AugmentedTransformTest() {
    }

    private void doTest() {
        Point3d[] realPoints = new Point3d[]{
            new Point3d(1, 0, 0),
            new Point3d(2, 0, 0),
            new Point3d(1, 1, 0),
            new Point3d(2, 1, 1),
            new Point3d(-1, 0, 1),
            new Point3d(3, 1, 3)
        };

        System.out.println("Points0 :" + Arrays.toString(realPoints));

        Transform3D trans = new Transform3D();
        trans.setTranslation(new Vector3d(0, 0, 15));
        System.out.println("Transform matrix: ");

        Matrix4d transMat = new Matrix4d();
        trans.get(transMat);
        System.out.println("Original transform:");
        System.out.println(transMat);

//        System.out.println(trans.t);
//      trot.rotZ(30);

        Point3d[] transPoints = transformPoints(trans, realPoints);
        System.out.println("transPoints :" + Arrays.toString(transPoints));

        Point2d[] pointsImg = to2DPoints(trans, transPoints);
        System.out.println("Points 2D: " + Arrays.toString(pointsImg));


        solve(realPoints, pointsImg);

    }

    /**
     *
     *
     *
     * matrix colums are like:
     *     *
     * x' =   m00*x + m01*y + m02*z + m03*w  =  Ax * tA
     * y' =   m10*x + m11*y + m12*z + m13*w  =  Ay * tA
     * z' =   m20*x + m21*y + m22*z + m23*w  =  1 * tA
     *
     *foreach point
     *  mij - coeficients for transform3d matrix
     * (x,y,z) - real
     * (x',y',z') - transformed
     * (Ax, Ay) - on image
     * tA - is scale of image point vector
     * w - is 1 for points
     *
     * row values are following:
     * 0   1   2   3                       8                 12
     * m00 m01 m02 m03   m10 m11 m12 m13   m20 m21 m22 m23   t0 t1 t2 t3 t4 t5
     *
     * @param realPoints
     * @param pointImgs
     */
    private void solve(Point3d[] realPoints, Point2d[] pointImgs) {
        double data[][] = new double[18][18];
        double w = 1;
        // createEquations
        for (int i = 0; i < realPoints.length; i++) {
            Point3d point3d = realPoints[i];
            // x' row
            data[3 * i + 0][0 + 0] = point3d.x;
            data[3 * i + 0][0 + 1] = point3d.y;
            data[3 * i + 0][0 + 2] = point3d.z;
            data[3 * i + 0][0 + 3] = w;
            data[3 * i + 0][12 + i] = -pointImgs[i].x;
            // y' row
            data[3 * i + 1][4 + 0] = point3d.x;
            data[3 * i + 1][4 + 1] = point3d.y;
            data[3 * i + 1][4 + 2] = point3d.z;
            data[3 * i + 1][4 + 3] = w;
            data[3 * i + 1][12 + i] = -pointImgs[i].y;
            // z' row
            data[3 * i + 2][8 + 0] = point3d.x;
            data[3 * i + 2][8 + 1] = point3d.y;
            data[3 * i + 2][8 + 2] = point3d.z;
            data[3 * i + 2][8 + 3] = w;
            data[3 * i + 2][12 + i] = -1;
        }

        double[] vect = new double[17];

        // set parameter t6 to 1 ;)
        double t6 = 1;
        vect[3*5+0] = t6*pointImgs[0].x;
        vect[3*5+1] = t6*pointImgs[0].y;
//        vect[3*5+2] = t6;
        // also in matrix
        data[3*5+0][17] = 0;
        data[3*5+1][17] = 0;
        data[3*5+2][17] = 0;
        // parameter set done

        // solving:

        RealMatrix matrix = new RealMatrixImpl(data);
        matrix = matrix.getSubMatrix(0, 16, 0, 16);
        System.out.println("Matrix:");
        System.out.println(matrix);
        System.out.println("Row0: " + Arrays.toString(matrix.getRow(0)));



        double[] solution = matrix.solve(vect);

        System.out.println("Solution: " + Arrays.toString(solution));

        Matrix4d transMatrix = new Matrix4d(
                solution[0], solution[1], solution[2], solution[3],
                solution[4], solution[5], solution[6], solution[7],
                solution[8], solution[9], solution[10], solution[11],
                0, 0, 0, 1);

        System.out.println("transMatrix: ");
        System.out.println(transMatrix);

        Transform3D transOut = new Transform3D();


    }

    private Point3d[] transformPoints(Transform3D trans, Point3d[] points) {
        Point3d[] transPoints = new Point3d[points.length];
        for (int i = 0; i < points.length; i++) {
            Point3d point3d = new Point3d(points[i]);
            trans.transform(point3d);
            transPoints[i] = point3d;
        }
        return transPoints;
    }

    private Point2d[] to2DPoints(Transform3D trans, Point3d[] points) {
        Point2d[] points2d = new Point2d[points.length];
        for (int i = 0; i < points.length; i++) {
            points2d[i] = new Point2d((int) (points[i].x / points[i].z), (int) (points[i].y / points[i].z));
        }
        return points2d;
    }

    public static void main(String[] args) {
        new AugmentedTransformTest().doTest();
    }
}
