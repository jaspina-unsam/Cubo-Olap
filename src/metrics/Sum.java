package metrics;

import java.util.List;

public class Sum extends Measure{

    public Sum() {
        super("suma");
    }

    @Override
    public double calc(List<Double> values) {
        double sum = 0;
        if (values == null) {
            return sum;
        }
        for (double value : values) {
            sum += value;
        }
        return sum;
    }
}
