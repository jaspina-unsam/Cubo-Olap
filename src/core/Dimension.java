package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * La clase Dimension representa una dimension de un cubo OLAP.
 */
public class Dimension {
    private String name;                    // Nombre de la dimension
    private String idKey;                   //
    private Map<String, Level> levels;      //
    private List<String> hierarchy;         //
    private int currentLevel;               //

    /**
     * Método constructor de la clase
     * 
     * @param name Nombre de la dimension
     * @param idKey
     * @param levels
     * @param hierarchy
     */
    public Dimension(String name, String idKey, Map<String, Level> levels, List<String> hierarchy) {
        this.name = name;
        this.idKey = idKey;
        this.levels = levels;
        this.hierarchy = hierarchy;
        this.currentLevel = 0;
    }

    private Dimension(String name, String idKey, List<String> hierarchy, int currentLevel) {
        this.name = name;
        this.idKey = idKey;
        this.levels = new HashMap<>();
        this.hierarchy = hierarchy;
        this.currentLevel = currentLevel;
    }

    public void addLevel(Level level) {
        levels.put(level.getName(), level);
    }

    public void addLevels(Map<String, Level> levels) {
        this.levels = levels;
    }

    public String getLevelNameAt(int index) {
        return hierarchy.get(index);
    }

    public List<String> getLevels() {
        return this.hierarchy;
    }

    public List<Integer> getIdList(String value) {
        List<Integer> ids = new ArrayList<>();
        String levelName = hierarchy.get(currentLevel);
        List<Object> elements = levels.get(levelName).getElements();
        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i).toString().equals(value)) {
                ids.add((Integer) levels.get(idKey).getElements().get(i));
            }
        }
        return ids;
    }

    public Dimension diceDimension(List<String> values) {
        Dimension newDimension = new Dimension(this.name, this.idKey, this.hierarchy, this.currentLevel);
        Map<String, Level> newLevels = new HashMap<>();
        newLevels.put(this.idKey, new Level(this.idKey));
        for (String levelName : hierarchy) {
            newLevels.put(levelName, new Level(levelName));
        }

        // Recorrer los elementos del active level y solamente guardar en newlevels los elementos que coincidan con los valores
        String levelName = hierarchy.get(currentLevel);
        List<Object> elements = levels.get(levelName).getElements();

        for (int i = 0; i < elements.size(); i++) {
            if (values.contains(elements.get(i).toString())) {
                newLevels.get(idKey).getElements().add(levels.get(idKey).getElements().get(i));
                for (String level : hierarchy) {
                    newLevels.get(level).getElements().add(levels.get(level).getElements().get(i));
                }
            }
        }
        newDimension.addLevels(newLevels);
        return newDimension;
    }

    public String getName() {
        return this.name;
    }

    public String getIdKey() {
        return this.idKey;
    }

    public int getCurrentLevel() {
        return this.currentLevel;
    }

    public Level getActiveLevel() {
        return levels.get(hierarchy.get(currentLevel));
    }

    public void setCurrentLevel(int level) {
        this.currentLevel = level;
    }

    @Override
    public String toString() {
        return "Dimension [name =" + name + ", idKey =" + idKey + ", currentLevel =" + hierarchy.get(currentLevel) + "]";
    }
}
