package robotour.pathing.simple;

class WheelCmd {

    final double leftspeed;
    final double rightspeed;
    final double leftacc;
    final double rightacc;
    final long durationms;

    public WheelCmd(double leftspeed, double rightspeed, double leftacc, double rightacc, long duration) {
        this.leftspeed = leftspeed;
        this.rightspeed = rightspeed;

        this.leftacc = leftacc;
        this.rightacc = rightacc;

        this.durationms = duration;
    }
}
