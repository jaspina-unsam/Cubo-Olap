package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * La clase Dimension representa una dimension de un cubo OLAP.
 */
public class Dimension {
    private String name;                    // Nombre de la dimensión
    private String idKey;                   // Nombre de clave de identificación que conecta la dimensión a los hechos
    private Map<String, Level> levels;      // Mapa de niveles de la dimensión
    private List<String> hierarchy;         // Lista que define la jerarquía de niveles
    private int currentLevel;               // Nivel actual de la dimensión

    /**
     * Constructor de la clase
     * 
     * @param name Nombre de la dimensión
     * @param idKey Nombre de clave de identificación que conecta la dimensión a los hechos
     * @param levels Mapa de niveles de la dimensión
     * @param hierarchy Lista que define la jerarquía de niveles
     */
    public Dimension(String name, String idKey, Map<String, Level> levels, List<String> hierarchy) {
        this.name = name;
        this.idKey = idKey;
        this.levels = levels;
        this.hierarchy = hierarchy;
        this.currentLevel = 0;
    }

    /**
     * Constructor privado de la clase sin niveles pero con nivel actual
     * 
     * @param name Nombre de la dimension
     * @param idKey Nombre de clave de identificación que conecta a la dimensión a los hechos
     * @param hierarchy Lista que define la jerarquía de niveles
     * @param currentLevel Nivel actual de la dimensión
     */
    private Dimension(String name, String idKey, List<String> hierarchy, int currentLevel) {
        this.name = name;
        this.idKey = idKey;
        this.levels = new HashMap<>();
        this.hierarchy = hierarchy;
        this.currentLevel = currentLevel;
    }

    /**
     * Método para añadir un nivel a la dimensión
     * 
     * @param level Nivel a añadir
     */
    public void addLevel(Level level) {
        levels.put(level.getName(), level);
    }

    /**
     * Método que le da los niveles a la dimensión
     * 
     * @param levels Mapa de niveles a añadir
     */
    public void addLevels(Map<String, Level> levels) {
        this.levels = levels;
    }

    public String getLevelNameAt(int index) {
        return hierarchy.get(index);
    }

    public List<String> getLevels() {
        return this.hierarchy;
    }

    /**
     * Método que devuelve una lista de IDs basados en el valor especificado en el nivel actual
     * 
     * @param value Valor para buscar los IDs correspondientes
     * @return Lista de IDs que corresponden al valor especificado
     */
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

    /**
     * Método que filtra la dimensión basada en una lista de valores y retorna una nueva dimensión
     * 
     * @param values Lista de valores para filtrar los elementos de la dimensión
     * @return Nueva dimensión filtrada basada en los valores especificados
     */
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
