package vision;

/**
 *
 * @author Kotuc
 */
public class CommonParameters {

    public static final Parameter saturationTreshold = new Parameter("saturation treshold", 0.5, 0, 1);

    static {
        new ParameterFrame(saturationTreshold);
    }

}
