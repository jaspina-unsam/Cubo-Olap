package metrics;

import java.util.List;

/**
 * La clase abstracta Measure representa una medida a calcular en un conjunto de datos
 */
public abstract class Measure {
    private String name;

    public Measure(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public abstract double calc(List<Double> values);

    @Override
    public String toString() {
        return this.name;
    }
}
