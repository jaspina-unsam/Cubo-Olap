package metrics;

import java.util.Collections;
import java.util.List;

public class Min extends Measure{

    public Min() {
        super("min");
    }

    @Override
    public double calc(List<Double> values) {
        if (values == null) {
            return 0;
        }
        Collections.sort(values);
        return values.get(0);
    }
}
