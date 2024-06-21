package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dimension de un cubo OLAP.
 * Es responsable por sus niveles, la jerarquia relacionada y su primary key.
 */
public class Dimension {
    private String name;                // Nombre dado a la dimensión por el/la usuario/a.
    private String idKey;               // PK de la dimensión.
    private Map<String, Level> levels;  // Mapa de niveles de la dimensión
    private List<String> hierarchy;     // Lista que define la jerarquía de niveles
    private int currentLevel;           // Nivel actual de la dimensión. Trabaja con hierarchy.


    public Dimension(String name, String idKey, Map<String, Level> levels, List<String> hierarchy) {
        this.name = name;
        this.idKey = idKey;
        this.levels = levels;
        this.hierarchy = hierarchy;
        this.currentLevel = 0;
    }

    /**
     * Constructor privado para poder reducir/filtrar los valores presentes en los niveles.
     */
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

    public List<String> getLevels() {
        return this.hierarchy;
    }

    public String getName() {
        return this.name;
    }

    public String getIdKey() {
        return this.idKey;
    }

    public void setCurrentLevel(int level) {
        this.currentLevel = level;
    }

    public int getCurrentLevel() {
        return this.currentLevel;
    }

    public Level getActiveLevel() {
        return levels.get(hierarchy.get(currentLevel));
    }

    @Override
    public String toString() {
        return "Dimension [name =" + name + ", idKey =" + idKey
                + ", currentLevel =" + hierarchy.get(currentLevel) + "]";
    }

    /**
     * Trabaja con el método getIdsToSearch() del Cubo.
     * Dado un valor buscado, se toma el nivel activo de la jerarquía y se busca el id en la PK.
     * Devuelve todas las ids de su PK que tengan al valor buscado en el nivel activo.
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
     * Trabaja con el getNewDimensions() del cubo para reducir/filtar la dimensión.
     */
    public Dimension diceDimension(List<String> values) {
        Dimension newDimension = new Dimension(
            this.name,
            this.idKey,
            this.hierarchy,
            this.currentLevel
        ); // Clonar la dimensión actual sin el contenido de los niveles.


        Map<String, Level> newLevels = new HashMap<>();
        newLevels.put(this.idKey, new Level(this.idKey));
        for (String levelName : hierarchy) {
            newLevels.put(levelName, new Level(levelName));
        }

        String levelName = hierarchy.get(currentLevel);
        List<Object> elements = levels.get(levelName).getElements();

        /**
         * Recorre los elementos del nivel activo y guarda los elementos relacionados.
         */
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
}
