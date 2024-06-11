package core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Facts {
    private Map<String, Integer> featureMap;
    private List<List<String>> raw;

    public Facts (List<List<String>> data) {
        this.raw = data.subList(1, data.size());;
        this.featureMap = new HashMap<>();
        List<String> features = data.get(0);
        int i=0;
        for (String header : features) {
            featureMap.put(header, i);
            i++;
        }
    }

    public Set<String> getFeatureNames() {
        return featureMap.keySet();
    }

    public Integer getFeatureColumn(String name) {
        return featureMap.get(name);
    }

    public List<List<String>> getRaw() {
        return raw;
    }

    protected void removeFeature(String feature) {
        featureMap.remove(feature);
    }
}