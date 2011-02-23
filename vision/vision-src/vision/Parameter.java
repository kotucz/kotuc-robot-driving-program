package vision;

/**
 *
 * @author Kotuc
 */
public class Parameter {

    private final String name;
    private final double defaultValue;
    private final double minValue;
    private final double maxValue;
    double value;

    public Parameter(String name, double defaultValue, double minValue, double maxValue) {
        this.defaultValue = defaultValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.value = defaultValue;
        this.name = name;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name + ":" + value;
    }

    public String getName() {
        return name;
    }
}
