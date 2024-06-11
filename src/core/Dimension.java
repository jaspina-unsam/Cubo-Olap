package core;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import api.CsvParser;

public class Dimension {
    private Map<String, List<Object>> levelMap;
    private List<String> levels;
    private Map<Object, Set<Integer>> valueToCells;
    private int foreignKey;

    public Dimension(List<List<String>> data, int foreignKey) {
        buildLevelMap(data);
        this.foreignKey = foreignKey;
    }

    public Dimension(String filePath, int foreignKey) throws IOException {
        CsvParser csvParser = new CsvParser();
        List<List<String>> data = csvParser.read(filePath);
        buildLevelMap(data);
        this.foreignKey = foreignKey;
    }

    private void buildLevelMap(List<List<String>> data) {
        levelMap = new HashMap<>();
        valueToCells = new HashMap<>();
        levels = data.get(0);
        for (String header : levels) {
            levelMap.put(header, new ArrayList<>());
        }
        for (int rowIndex = 1; rowIndex < data.size(); rowIndex++) {
            List<String> row = data.get(rowIndex);
            for (int i = 0; i < levels.size(); i++) {
                Object value = parseValue(row.get(i));
                levelMap.get(levels.get(i)).add(value);
                
                valueToCells.putIfAbsent(value, new HashSet<>());
                valueToCells.get(value).add(rowIndex - 1);
            }
        }
        
    }

    private Object parseValue(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e1) {
            try {
                return Float.parseFloat(value);
            } catch (NumberFormatException e2) {
                try {
                    return LocalDate.parse(value);
                } catch (DateTimeParseException e3) {
                    return value;
                }
            }
        }
    }

    public Set<String> getLevels() {
        return levelMap.keySet();
    }

    public Set<Object> getLevel(String name) {
        return new TreeSet<>(levelMap.get(name));
    }

    public List<Object> getIdList(String levelName, String value) {
        List<Object> ids = new ArrayList<>();
        List<Object> elements = levelMap.get(levelName);
        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i).toString().equals(value)){
                ids.add(levelMap.get(levels.get(0)).get(i));
            }
        }
        return ids;
    }

    public String[] getValues() {
        return levelMap.keySet().toArray(new String[0]);
    }

    public int getForeignKey() {
        return foreignKey;
    }

    public Set<Integer> getCellIndices(String valor) {
        Set<Integer> indices = new HashSet<>();
        for (Map.Entry<String, List<Object>> entry : levelMap.entrySet()) {
            List<Object> values = entry.getValue();
            for (int i = 0; i < values.size(); i++) {
                if (values.get(i).toString().equals(valor)) {
                    indices.add(i);
                }
            }
        }
        return indices;
    }

    public void addFact(int idValue, int cellIndex) {
        if (!valueToCells.containsKey(idValue)) {
            throw new IllegalArgumentException("El id " + idValue + " del valor no existe en la dimensión.");
        }
        valueToCells.get(idValue).add(cellIndex);
    }
}
