package core;

import java.util.ArrayList;
import java.util.List;

/**
 * La clase Level representa un nivel de una dimensión
 */
public class Level {
    private String name; // Nombre del nivel
    private List<Object> elements; // Lista de elementos del nivel

    /**
     * Constructor de la clase
     * 
     * @param name     Nombre del nivel
     * @param elements Lista de elementos del nivel
     */
    public Level(String name, List<Object> elements) {
        this.name = name;
        this.elements = elements;
    }

    /**
     * Constructor de la clase sin elementos
     * 
     * @param name Nombre del nivel
     */
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

    /**
     * Método para clonar el nivel
     * 
     * @return Nuevo nivel que es una copia del nivel actual
     */
    public Level clone() {
        return new Level(this.name, this.elements);
    }

    /**
     * Método para eliminar un elemento del nivel dado el elemento
     * 
     * @param element Elemento que eliminar
     */
    public void removeElement(Object element) {
        this.elements.remove(element);
    }

    /**
     * Método para eliminar un elemento del nivel dado su índice
     * 
     * @param id Índice del elemento a eliminar
     */
    public void removeElementAtId(int id) {
        this.elements.remove(id);
    }

}
