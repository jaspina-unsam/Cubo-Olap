package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import metrics.Measure;

/**
 * La clase Cube representa un cubo OLAP.
 */
public class Cube {
    private Map<String, Dimension> dimensions;      // Mapeo de nombres de dimensión a su dimensión correspondiente
    private List<Measure> measures;                 // Lista de medidas del cubo
    private List<String> facts;                     // Lista de los nombres de los hechos del cubo
    private List<Cell> cells;                       // Lista de celdas del cubo
    private int selectedFact;                       // Hecho seleccionado actualmente
    private int selectedMeasure;                    // Medida seleccionada actualmente

    /**
     * Constructor de la clase
     */
    public Cube() {
        this.dimensions = new HashMap<>();
        this.measures = new ArrayList<>();
        this.facts = new ArrayList<>();
        this.cells = new ArrayList<>();
        this.selectedFact = 0;
        this.selectedMeasure = 0;
    }

    /**
     * Constructor privado de la clase
     * 
     * @param dimensions Mapa de dimensiones del cubo
     * @param metrics Lista de medidas del cubo
     * @param facts Lista de hechos del cubo
     * @param cells Lista de celdas del cubo
     */
    private Cube(Map<String, Dimension> dimensions, List<Measure> metrics, List<String> facts, List<Cell> cells) {
        this.dimensions = dimensions;
        this.measures = metrics;
        this.facts = facts;
        this.cells = cells;
        this.selectedFact = 0;
        this.selectedMeasure = 0;
    }

    /**
     * Método para añadir una dimensión al cubo
     * 
     * @param dimension Dimensión a añadir
     */
    public void addDimension(Dimension dimension) {
        dimensions.put(dimension.getName(), dimension);
    }

    public Set<String> getDimensionNames() {
        return dimensions.keySet();
    }

    /**
     * Método para obtener una dimensión por su nombre
     * 
     * @param name Nombre de la dimensión
     * @return Dimensión correspondiente al nombre dado
     */
    public Dimension getDimension(String name) {
        if (!dimensions.containsKey(name)) {
            throw new IllegalArgumentException("Dimension not found");
        }
        return dimensions.get(name);
    }

    /**
     * Método para bajar un nivel de jerarquía de una dimensión dada
     * 
     * @param dimension Nombre de la dimensión
     * @return true si se pudo profundizar, false si ya se encuentra en el nivel más bajo
     */
    public boolean drillDown(String dimension) {
        Dimension dim = dimensions.get(dimension);
        if (dim.getCurrentLevel() < dim.getLevels().size() - 1) {
            dim.setCurrentLevel(dim.getCurrentLevel() + 1);
            return true;
        }
        return false;
    }

    /**
     * Método para subir un nivel de jerarquía de una dimensión dada
     * 
     * @param dimension Nombre de la dimensión
     * @return true si se pudo subir, false si ya se encuentra en el nivel más alto
     */
    public boolean rollUp(String dimension) {
        Dimension dim = dimensions.get(dimension);
        if (dim.getCurrentLevel() > 0) {
            dim.setCurrentLevel(dim.getCurrentLevel() - 1);
            return true;
        }
        return false;
    }

    /**
     * Método para añadir una celda al cubo
     * 
     * @param cell Celda para añadir
     */
    public void addCell(Cell cell) {
        cells.add(cell);
    }

    public List<Cell> getCells() {
        return cells;
    }

    /**
     * Método que añade un hecho al cubo
     * 
     * @param fact Hecho a añadir
     */
    public void addFact(String fact) {
        facts.add(fact);
    }

    /**
     * Método para agregar un tipo de medida al cubo
     * 
     * @param metric Medida que agregar
     */
    public void addMeasure(Measure metric) {
        measures.add(metric);
    }

    /**
     * Método que selecciona un hecho
     * 
     * @param fact Nombre del hecho que se quiere seleccionar
     */
    public void selectFact(String fact) {
        this.selectedFact = facts.indexOf(fact);
    }

    /**
     * Método que selecciona una medida
     * 
     * @param metric Nombre de medida que se quiere seleccionar
     */
    public void selectMeasure(String metric) {
        for (Measure measure : this.measures) {
            if (measure.getName().equals(metric)) {
                this.selectedMeasure = measures.indexOf(measure);
                break;
            }
        }
    }

    public Measure getSelectedMeasure() {
        return measures.get(selectedMeasure);
    }

    /**
     * Método para obtener las IDs a buscar en una dimensión
     * 
     * @param dimension Nombre de la dimensión
     * @param values Valores a buscar
     * @return Lista de IDs a buscar
     */
    private List<Integer> getIdsToSearch(String dimension, String[] values) {
        List<Integer> idsToSearch = new ArrayList<>();
        for (String value : values) {
            idsToSearch.addAll(dimensions.get(dimension).getIdList(value));
        }
        return idsToSearch;
    }

    /**
     * Método que busca celdas en una dimensión con dada una clave y valores específicos
     * 
     * @param dimension Nombre de la dimensión
     * @param key Clave a buscar
     * @param values Valores a buscar
     * @return Lista de celdas que coinciden con los criterios de búsqueda
     */
    private List<Cell> searchCells(String dimension, String key, String[] values) {
        return searchCells(this.cells, dimension, key, values);
    }

    /**
     * Método para buscar celdas en una lista de celdas con criterios específicos
     * 
     * @param cellsToSearch Lista de celdas donde buscar
     * @param dimension Nombre de la dimensión
     * @param key Clave a buscar
     * @param values Valores a buscar
     * @return Lista de celdas que coinciden con los criterios de búsqueda
     */
    private List<Cell> searchCells(List<Cell> cellsToSearch, String dimension, String key, String[] values) {
        List<Cell> result = new ArrayList<>();
        List<Integer> idsToSearch = getIdsToSearch(dimension, values);
        for (int id : idsToSearch) {
            for (Cell cell : cellsToSearch) {
                if (cell.isResult(key, id)) {
                    result.add(cell);
                }
            }
        }
        return result;
    }

    /**
     * Método para buscar celdas en dos dimensiones dados criterios específicos para cada dimensión
     * 
     * @param dim1 Nombre de la primera dimensión
     * @param key1 Clave de la primera dimensión
     * @param values1 Valores de la primera dimensión
     * @param dim2 Nombre de la segunda dimensión
     * @param key2 Clave de la segunda dimensión
     * @param values2 Valores de la segunda dimensión
     * @return Lista de celdas que coinciden con los criterios de búsqueda
     */
    private List<Cell> searchCells(String dim1, String key1, String[] values1, String dim2, String key2, String[] values2) {
        List<Cell> cellsDim1 = searchCells(dim1, key1, values1);
        List<Cell> resultCells = searchCells(cellsDim1, dim2, key2, values2);
        return resultCells;
    }

    /**
     * Método para buscar celdas en tres dimensiones con criterios específicos a buscar para cada dimension
     * 
     * @param dim1 Nombre de la primera dimensión
     * @param key1 Clave de la primera dimensión
     * @param values1 Valores de la primera dimensión
     * @param dim2 Nombre de la segunda dimensión
     * @param key2 Clave de la segunda dimensión
     * @param values2 Valores de la segunda dimensión
     * @param dim3 Nombre de la tercera dimensión
     * @param key3 Clave de la tercera dimensión
     * @param values3 Valores de la tercera dimensión
     * @return Lista de celdas que coinciden con los criterios de búsqueda
     */
    private List<Cell> searchCells(String dim1, String key1, String[] values1, String dim2, String key2, String[] values2, String dim3, String key3, String[] values3) {
        List<Cell> cellsDim1 = searchCells(dim1, key1, values1);
        List<Cell> cellsDim2 = searchCells(cellsDim1, dim2, key2, values2);
        List<Cell> resultCells = searchCells(cellsDim2, dim3, key3, values3);
        return resultCells;
    }

    /**
     * Método para obtener nuevas dimensiones basadas en criterios específicos
     * 
     * @param origDims Mapa de dimensiones originales
     * @param values Valores para filtrar
     * @param dimName Nombre de la dimensión a filtrar
     * @return Mapa de nuevas dimensiones filtradas
     */
    private Map<String, Dimension> getNewDimensions(Map<String, Dimension> origDims, String[] values, String dimName) {
        Map<String, Dimension> newDimensions = new HashMap<>(origDims);
        Dimension dim = newDimensions.get(dimName).diceDimension(List.of(values));
        newDimensions.put(dimName, dim);
        return newDimensions;
    }

    /**
     * Método que genera un subcubo filtrando los hechos correspondientes a la dimensión y valor de corte
     * 
     * @param dimension Nombre de la dimensión
     * @param value Valor a filtrar
     * @return Nuevo cubo resultado de la operación slice
     */
    public Cube slice(String dimension, String value) {
        String key = dimensions.get(dimension).getIdKey();
        List<Cell> newCells = searchCells(dimension, key, new String[] { value });
        return new Cube(getNewDimensions(this.dimensions, new String[] {value}, dimension), this.measures, this.facts, newCells);
    }

    /**
     * Método que permite seleccionar varias dimensiones de corte y un conjunto de valores a filtrar en cada una
     * 
     * @param dimension Nombre de la dimensión
     * @param values Valores a filtrar
     * @return Nuevo cubo resultado de la operación dice
     */
    public Cube dice(String dimension, String[] values) {
        String key = dimensions.get(dimension).getIdKey();
        List<Cell> newCells = searchCells(dimension, key, values);
        return new Cube(getNewDimensions(this.dimensions, values, dimension), this.measures, this.facts, newCells);
    }

    /**
     * Método que permite seleccionar varias dimensiones de corte y un conjunto de valores a filtrar en cada una
     * 
     * @param dim1 Nombre de la primera dimensión
     * @param values1 Valores de la primera dimensión
     * @param dim2 Nombre de la segunda dimensión
     * @param values2 Valores de la segunda dimensión
     * @return Nuevo cubo resultado de la operación dice
     */
    public Cube dice(String dim1, String[] values1, String dim2, String[] values2) {
        String key1 = dimensions.get(dim1).getIdKey();
        String key2 = dimensions.get(dim2).getIdKey();
        List<Cell> newCells = searchCells(dim1, key1, values1, dim2, key2, values2);
        Map<String, Dimension> newDims1 = getNewDimensions(this.dimensions, values1, dim1);
        Map<String, Dimension> newDims = getNewDimensions(newDims1, values2, dim2);
        return new Cube(newDims, this.measures, this.facts, newCells);
    }

    /**
     * Método que permite seleccionar varias dimensiones de corte y un conjunto de valores a filtrar en cada una
     * 
     * @param dim1 Nombre de la primera dimensión
     * @param values1 Valores de la primera dimensión
     * @param dim2 Nombre de la segunda dimensión
     * @param values2 Valores de la segunda dimensión
     * @param dim3 Nombre de la tercera dimensión
     * @param values3 Valores de la tercera dimensión
     * @return Nuevo cubo resultado de la operación dice
     */
    public Cube dice(String dim1, String[] values1, String dim2, String[] values2, String dim3, String[] values3) {
        String key1 = dimensions.get(dim1).getIdKey();
        String key2 = dimensions.get(dim2).getIdKey();
        String key3 = dimensions.get(dim3).getIdKey();
        List<Cell> newCells = searchCells(dim1, key1, values1, dim2, key2, values2, dim3, key3, values3);
        Map<String, Dimension> newDims1 = getNewDimensions(this.dimensions, values1, dim1);
        Map<String, Dimension> newDims2 = getNewDimensions(newDims1, values2, dim2);
        Map<String, Dimension> newDims = getNewDimensions(newDims2, values3, dim3);
        return new Cube(newDims, this.measures, this.facts, newCells);
    }

    @Override
    public String toString() {
        return "Cube [cells=" + cells.size() + ']';
    }

    public Cell getCell() {
        return Cell.cellFromGroup(this.cells);
    }

    public Cell getCell(String dimension, String value) {
        return Cell.cellFromGroup(searchCells(dimension, dimensions.get(dimension).getIdKey(), new String[] { value }));
    }

    public Cell getCell(String dim1, String value1, String dim2, String value2) {
        return Cell.cellFromGroup(searchCells(dim1, dimensions.get(dim1).getIdKey(), new String[] { value1 }, dim2, dimensions.get(dim2).getIdKey(), new String[] { value2 }));
    }

    public String getSelectedFact() {
        return facts.get(selectedFact);
    }

    /**
     * Método que clona el cubo
     * 
     * @return Nuevo cubo que es una copia del cubo actual
     */
    public Cube cloneCube() {
        return new Cube(this.dimensions, this.measures, this.facts, this.cells);
    }

}
