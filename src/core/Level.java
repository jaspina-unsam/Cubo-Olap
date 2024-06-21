package core;

import java.util.ArrayList;
import java.util.List;

/**
 * Se encarga de guardar todos los valores de un nivel de una dimensión.
 * En principio, la lista de objetos es la columna de un achivo de dimensión.
 */
public class Level {
    private String name;
    private List<Object> elements;

    public Level(String name, List<Object> elements) {
        this.name = name;
        this.elements = elements;
    }

    public Level(String name) {
        this.name = name;
        this.elements = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public List<Object> getElements() {
        return this.elements;
    }

}
