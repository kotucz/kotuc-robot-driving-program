package robotour.navi.basic;

/**
 * Is mutable.
 * @author Kotuc
 */
public class Pose {

    protected Point point;
//    protected Azimuth azimuth;
    protected Heading heading;

    public Pose(Pose pose) {
        this.setTo(pose);
    }

    public Pose(Point point, Azimuth azimuth) {
        this.point = point;
        this.heading = azimuth.toHeading();
    }

    /**
       @deprecated
     */
    public void setAzimuth(Azimuth azimuth) {
//        this.azimuth = azimuth;
        this.heading = azimuth.toHeading();

    }

    public void setPoint(Point point) {
        this.point = point;
    }

    /**
       @deprecated
     */
    public Azimuth getAzimuth() {
        return Azimuth.fromHeading(this.heading);
    }

    public Heading getHeading() {
        return heading;
    }

    public void setHeading(Heading heading) {
        this.heading = heading;
    }

    public Point getPoint() {
        return point;
    }

    public final void setTo(Pose pose) {
        this.setPoint(pose.getPoint());
        this.setAzimuth(pose.getAzimuth());
    }

    /**
     * Moves along arc (part of circle) specified by the angle and cutline (tetiva) distance.
     * @param dist
     * @param radscw
     */
    public void doMoveFwRight(double dist, double radscw) {
        this.point = point.move(this.heading.rotatedCounterClockWise(-0.5*radscw), dist);
        this.heading = this.heading.rotatedCounterClockWise(-radscw);
        //            Vec2 fw = new Vec2((float) Math.cos(0.5 * rotangleccw + Math.PI / 2), (float) Math.sin(rotangleccw + Math.PI / 2));
        //        fw.normalize();
        //        moveRelFw(getWorldVector(fw.mul((float) )));
        //        rotateCCW(rotangleccw);
        //        shift(fwx, fwy, rotangleccw);
    }

    /**
     * Moves the point of this pose.
     * @param azimuth
     * @param dist
     * @see Point move
     */
    public void move(Azimuth azimuth, double dist) {
        point.move(azimuth, dist);
    }

//    public Angle angleTo(Point t) {
//        return new Angle(this.point.getAzimuthTo(t).radians()-this.azimuth.radians()).shorter();
//    }


}
