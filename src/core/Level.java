package core;

import java.util.List;

public class Level {
    private String name;
    private List<Object> elements;

    public Level(String name, List<Object> elements) {
        this.name = name;
        this.elements = elements;
    }

    public String getName() {
        return this.name;
    }

    public List<Object> getElements() {
        return this.elements;
    }
}
