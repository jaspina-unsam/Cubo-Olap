package core;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import api.DataType;

public class Facts {
    private Map<String, List<Object>> featureMap;
    private List<DataType> types;
    private List<String> foreignKeys;
    private List<String> features;

    public Facts (List<List<String>> data, List<DataType> types, List<String> foreignKeys) {
        this.types = types;
        this.foreignKeys = foreignKeys;
        buildFeatureMap(data);
    }

    private void buildFeatureMap(List<List<String>> data) {
        Map<String, List<Object>> result = new HashMap<>();
        features = data.get(0);
        for (String header : features) {
            result.put(header, new ArrayList<>());
        }
        for (List<String> row : data.subList(1, data.size())) {
            for (int i = 0; i < features.size(); i++) {
                switch (types.get(i)) {
                    case INTEGER:
                        result.get(features.get(i)).add(Integer.valueOf(row.get(i)));
                        break;
                    case FLOAT:
                        result.get(features.get(i)).add(Float.valueOf(row.get(i)));
                        break;
                    case STRING:
                        result.get(features.get(i)).add(String.valueOf(row.get(i)));
                        break;
                    case DATE:
                        result.get(features.get(i)).add(LocalDate.parse(row.get(i)));
                        break;
                    default:
                        result.get(features.get(i)).add(String.valueOf(row.get(i)));
                        break;
                }
            }
        }
        featureMap = result;
    }

    public Set<String> getFeatureNames() {
        return new HashSet<>(featureMap.keySet());
    }
}
