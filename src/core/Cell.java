package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * La clase Cell representa una celda de un cubo OLAP.
 */
public class Cell {
    private Map<String, Integer> keys;          // Mapa de claves de la celda
    private Map<String, List<Double>> facts;    // Mapa de hechos de la celda

    /**
     * Constructor de la clase
     * 
     * @param keys Mapa de claves de la celda
     * @param facts Mapa de hechos de la celda
     */
    public Cell(Map<String, Integer> keys, Map<String, List<Double>> facts) {
        this.keys = keys;
        this.facts = facts;
    }

    /**
     * Constructor vacío de la clase
     */
    public Cell() {
        keys = new HashMap<>();
        facts = new HashMap<>();
    }

    /**
     * Constructor de la clase con una clave y un valor de la clave
     * 
     * @param idKey Clave de identificación de la celda
     * @param keyValue Valor de la clave de identificación
     */
    public Cell(String idKey, Integer keyValue) {
        keys = new HashMap<>();
        keys.put(idKey, keyValue);
        facts = new HashMap<>();
    }

    /**
     * Constructor de la clase con tres claves y tres valores correspondientes
     * 
     * @param idKey1 Primera clave de identificación de la celda
     * @param keyValue1 Valor de la primera clave de identificación
     * @param idKey2 Segunda clave de identificación de la celda
     * @param keyValue2 Valor de la segunda clave de identificación
     * @param idKey3 Tercera clave de identificación de la celda
     * @param keyValue3 Valor de la tercera clave de identificación
     */
    public Cell(String idKey1, Integer keyValue1, String idKey2, Integer keyValue2, String idKey3, Integer keyValue3) {
        facts = new HashMap<>();
        keys = new HashMap<>();
        keys.put(idKey1, keyValue1);
        keys.put(idKey2, keyValue2);
        keys.put(idKey3, keyValue3);
    }

    /**
     * Constructor de la clase con dos claves y un valor para cada una
     * 
     * @param idKey1 Primera clave de identificación de la celda
     * @param keyValue1 Valor de la primera clave de identificación
     * @param idKey2 Segunda clave de identificación de la celda
     * @param keyValue2 Valor de la segunda clave de identificación
     */
    public Cell(String idKey1, Integer keyValue1, String idKey2, Integer keyValue2) {
        facts = new HashMap<>();
        keys = new HashMap<>();
        keys.put(idKey1, keyValue1);
        keys.put(idKey2, keyValue2);
    }

    /**
     * Método para añadir una clave a la celda
     * 
     * @param key Clave a añadir
     * @param value Valor de la clave
     */
    public void addKey(String key, Integer value) {
        keys.put(key, value);
    }

    /**
     * Método para añadir un hecho a la celda
     * 
     * @param key Clave del hecho
     * @param value Valor del hecho
     */
    public void addFact(String key, Double value) {
        if (!facts.containsKey(key)) {
            List<Double> valuesList = new ArrayList<>();
            valuesList.add(value);
            facts.put(key, valuesList);
        } else {
            facts.get(key).add(value);
        }
    }

    /**
     * Método para verificar si la celda tiene una clave y valor específicos
     * 
     * @param key Clave a verificar
     * @param value Valor a verificar
     * @return true si la clave y el valor coinciden, false de lo contrario
     */
    public boolean isResult(String key, Integer value) {
        return keys.get(key).equals(value);
    }

    /**
     * Método para verificar si la celda tiene dos pares de claves y valores específicos
     * 
     * @param key1 Primera clave a verificar
     * @param value1 Valor de la primera clave a verificar
     * @param key2 Segunda clave a verificar
     * @param value2 Valor de la segunda clave a verificar
     * @return true si las claves y los valores coinciden, false de lo contrario
     */
    public boolean isResult(String key1, int value1, String key2, int value2) {
        return keys.get(key1).equals(value1) && keys.get(key2).equals(value2);
    }

    /**
     * Método para verificar si la celda tiene tres claves y valores específicos
     * 
     * @param key1 Primera clave a verificar
     * @param value1 Valor de la primera clave a verificar
     * @param key2 Segunda clave a verificar
     * @param value2 Valor de la segunda clave a verificar
     * @param key3 Tercera clave a verificar
     * @param value3 Valor de la tercera clave a verificar
     * @return true si las claves y los valores coinciden, false de lo contrario
     */
    public boolean isResult(String key1, int value1, String key2, int value2, String key3, int value3) {
        return keys.get(key1).equals(value1) && keys.get(key2).equals(value2);
    }

    /**
     * Método para crear una celda a partir de un grupo de celdas
     * 
     * @param cells Lista de celdas para agrupar
     * @return Nueva celda que agrupa los hechos de las celdas proporcionadas
     */
    public static Cell cellFromGroup(List<Cell> cells) {
        Cell result = new Cell();
        for (Cell cell : cells) {
            for (String factName : cell.facts.keySet()) {
                for (Double value : cell.facts.get(factName)) {
                    result.addFact(factName, value);
                }
            }
        }
        return result;
    }

    public List<Double> getFacts(String key) {
        return facts.get(key);
    }

    @Override
    public String toString() {
        return "Cell [keys =" + keys + ", facts =" + facts + "]";
    }
}