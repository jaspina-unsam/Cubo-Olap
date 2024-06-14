package metrics;

import java.util.List;

public abstract class Measure {
    private String name;

    public Measure(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public abstract double calc(List<Double> values);
}
