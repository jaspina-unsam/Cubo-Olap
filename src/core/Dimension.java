package core;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import api.DataType;

public class Dimension {
    private Map<String, List<Object>> levelMap;
    private List<DataType> types;
    private List<String> levels;

    public Dimension(List<List<String>> data, List<DataType> types) {
        this.types = types;
        buildLevelMap(data);
    }

    private void buildLevelMap(List<List<String>> data) {
        Map<String, List<Object>> result = new HashMap<>();
        levels = data.get(0);
        for (String header : levels) {
            result.put(header, new ArrayList<>());
        }
        for (List<String> row : data.subList(1, data.size())) {
            for (int i = 0; i < levels.size(); i++) {
                switch (types.get(i)) {
                    case INTEGER:
                        result.get(levels.get(i)).add(Integer.valueOf(row.get(i)));
                        break;
                    case FLOAT:
                        result.get(levels.get(i)).add(Float.valueOf(row.get(i)));
                        break;
                    case STRING:
                        result.get(levels.get(i)).add(String.valueOf(row.get(i)));
                        break;
                    case DATE:
                        result.get(levels.get(i)).add(LocalDate.parse(row.get(i)));
                        break;
                    default:
                        result.get(levels.get(i)).add(String.valueOf(row.get(i)));
                        break;
                }
            }
        }
        levelMap = result;
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

}
