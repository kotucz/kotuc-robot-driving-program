package robotour.navi.local;

/**
 *
 * @author Kotuc
 */
public class PID {

    private final double proportional;
    private final double integral;
    private final double derivative;

    private final double integralDumping;

    private double integralSum = 0;
    private double previousError = 0;

    public PID(double proportional, double integral, double derivative, double integralDumping) {
        this.proportional = proportional;
        this.integral = integral;
        this.derivative = derivative;
        this.integralDumping = integralDumping;
    }

//    private final double proportional = 0.8;
//    private final double integral = 0.3;
//    private double integralSum = 0;
//    private final double derivative = 0.5;
//    private double previousError = 0;

    double pidPeriodical(final double error) {
        integralSum = integralDumping * (integralSum + error);

        double response = error * proportional +
                        integralSum * integral +
                        (error - previousError) * derivative;

        this.previousError = error;

        return response;
    }

//    double pidWithTime(final double error, final long dtmillis) {
//        // TODO
//        integralSum = 0.9 * (integralSum + error);
//
//        double response = error * proportional +
//                        integralSum * integral +
//                        (error - previousError) * derivative;
//
//        this.previousError = error;
//
//        return response;
//    }


}
