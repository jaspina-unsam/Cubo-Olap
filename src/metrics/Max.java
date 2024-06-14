package metrics;

import java.util.Collections;
import java.util.List;

public class Max extends Measure{

    public Max() {
        super("max");
    }

    @Override
    public double calc(List<Double> values) {
        if (values == null) {
            return 0;
        }
        Collections.sort(values);
        Collections.reverse(values);
        return values.get(0);
    }
}
