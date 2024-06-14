package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cell {
    private Map<String, Integer> keys;
    private Map<String, List<Double>> facts;

    public Cell(Map<String, Integer> keys, Map<String, List<Double>> facts) {
        this.keys = keys;
        this.facts = facts;
    }

    public Cell() {
        keys = new HashMap<>();
        facts = new HashMap<>();
    }

    public Cell(String idKey, Integer keyValue) {
        keys = new HashMap<>();
        keys.put(idKey, keyValue);
        facts = new HashMap<>();
    }

    public Cell(String idKey1, Integer keyValue1, String idKey2, Integer keyValue2, String idKey3, Integer keyValue3) {
        facts = new HashMap<>();
        keys = new HashMap<>();
        keys.put(idKey1, keyValue1);
        keys.put(idKey2, keyValue2);
        keys.put(idKey3, keyValue3);
    }

    public Cell(String idKey1, Integer keyValue1, String idKey2, Integer keyValue2) {
        facts = new HashMap<>();
        keys = new HashMap<>();
        keys.put(idKey1, keyValue1);
        keys.put(idKey2, keyValue2);
    }

    public void addKey(String key, Integer value) {
        keys.put(key, value);
    }

    public void addFact(String key, Double value) {
        if (!facts.containsKey(key)) {
            List<Double> valuesList = new ArrayList<>();
            valuesList.add(value);
            facts.put(key, valuesList);
        } else {
            facts.get(key).add(value);
        }
    }

    public boolean isResult(String key, Integer value) {
        return keys.get(key).equals(value);
    }

    public boolean isResult(String key1, int value1, String key2, int value2) {
        return keys.get(key1).equals(value1) && keys.get(key2).equals(value2);
    }

    public boolean isResult(String key1, int value1, String key2, int value2, String key3, int value3) {
        return keys.get(key1).equals(value1) && keys.get(key2).equals(value2);
    }

    public static Cell cellFromGroup(List<Cell> cells) {
        Cell result = new Cell();
        for (Cell cell : cells) {
            for (String factName : cell.facts.keySet()) {
                for (Double value : cell.facts.get(factName)) {
                    result.addFact(factName, value);
                }
            }
        }
        return result;
    }

    public List<Double> getFacts(String key) {
        return facts.get(key);
    }

    @Override
    public String toString() {
        return "Cell[keys=" + keys + ", facts=" + facts + "]";
    }
}