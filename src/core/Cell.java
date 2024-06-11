package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cell {
    private Map<String, List<Double>> facts;
    
    public Cell() {
        facts = new HashMap<>();
    }

    public boolean isEmpty() {
        return facts.isEmpty();
    }
    
    @Override
    public String toString() {
        return "Celda [hechos=" + facts.keySet() + "]";
    }

    public List<Double> getValues(String factName) {
        if (!facts.containsKey(factName)) {
            return new ArrayList<>();
        }
        return facts.get(factName);
    }

    public void addFact(String factName, Double value) {
        if (!facts.containsKey(factName)) {
            facts.put(factName, new ArrayList<>());
        }
        facts.get(factName).add(value);
    }

    public static Cell group(List<Cell> cells) {
        Cell newCell = new Cell();
        for (Cell cell : cells) {
            for (String nombreHecho : cell.facts.keySet()) {
                for (Double valor : cell.facts.get(nombreHecho)) {
                    newCell.addFact(nombreHecho, valor);
                }
            }
        }
        return newCell;
    }
}
