package core;

import java.util.ArrayList;
import java.util.List;

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

    public Level clone() {
        return new Level(this.name, this.elements);
    }

    public void removeElement(Object element) {
        this.elements.remove(element);
    }

    public void removeElementAtId(int id) {
        this.elements.remove(id);
    }

}
