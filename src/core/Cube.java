package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import metrics.Measure;

/**
 * La clase Cube representa un cubo OLAP.
 */
public class Cube {
    private Map<String, Dimension> dimensions;  // Mapa de dimensiones
    private List<Measure> measures;             // Lista de medidas del cubo
    private List<String> facts;                 // Lista de los nombres de los hechos del cubo
    private List<Cell> cells;                   // Lista de celdas del cubo
    private int selectedFact;                   // Hecho seleccionado actualmente
    private int selectedMeasure;                // Medida seleccionada actualmente

    public Cube() {
        this.dimensions = new HashMap<>();
        this.measures = new ArrayList<>();
        this.facts = new ArrayList<>();
        this.cells = new ArrayList<>();
        this.selectedFact = 0;                  // Selecciona el primer hecho por defecto
        this.selectedMeasure = 0;               // Selecciona la primera medida por defecto
    }

    /**
     * Constructor usado para devolver en caso de slice, dice, etc.
     */
    private Cube(
        Map<String, Dimension> dimensions,
        List<Measure> metrics,
        List<String> facts,
        List<Cell> cells
    ) {
        this.dimensions = dimensions;
        this.measures = metrics;
        this.facts = facts;
        this.cells = cells;
        this.selectedFact = 0;
        this.selectedMeasure = 0;
    }

    public void addDimension(Dimension dimension) {
        dimensions.put(dimension.getName(), dimension);
    }

    public void addCell(Cell cell) {
        cells.add(cell);
    }

    public void addFact(String fact) {
        facts.add(fact);
    }

    public void addMeasure(Measure metric) {
        measures.add(metric);
    }

    public void selectFact(String fact) {
        this.selectedFact = facts.indexOf(fact);
    }

    public void selectMeasure(String metric) {
        for (Measure measure : this.measures) {
            if (measure.getName().equals(metric)) {
                this.selectedMeasure = measures.indexOf(measure);
                break;
            }
        }
    }

    @Override
    public String toString() {
        return "Cube [" + cells.size() + " cells. Dimensions: " + dimensions.keySet()
                + ". Facts: " + facts.toString()
                + ". Measures: " + measures.toString() + "]";
    }

    /**
     * Devuelve la instancia de la dimensión con el nombre especificado.
     */
    public Dimension getDimension(String name) {
        if (!dimensions.containsKey(name)) {
            throw new IllegalArgumentException("Dimension not found");
        }
        return dimensions.get(name);
    }

    /**
     * Devuelve la instancia del hecho seleccionado actualmente.
     */
    public String getSelectedFact() {
        return facts.get(selectedFact);
    }

    /**
     * Devuelve la instancia de la medida seleccionada actualmente.
     */
    public Measure getSelectedMeasure() {
        return measures.get(selectedMeasure);
    }

    /**
     * Utiliza el método cellFromGroup() de Cell para agrupar los valores de las
     * celdas.
     */
    public Cell getCell() {
        return Cell.cellFromGroup(this.cells);
    }

    public Cell getCell(String dimension, String value) {
        return Cell.cellFromGroup(
            searchCells(
                dimension, dimensions.get(dimension).getIdKey(), new String[] { value }
            )
        );
    }

    public Cell getCell(String dim1, String value1, String dim2, String value2) {
        return Cell.cellFromGroup(
            searchCells(
                dim1, dimensions.get(dim1).getIdKey(), new String[] { value1 },
                dim2, dimensions.get(dim2).getIdKey(), new String[] { value2 }
            )
        );
    }

    /**
     * Reune los IDs que coinciden con los values de la dimensión.
     *
     * @param dimension Nombre de la dimensión
     * @param values    Valores a buscar
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
     * Filtra o reduce el cubo a un subcubo con los valores de corte especificados
     * para una dimensión.
     */
    private Map<String, Dimension> getNewDimensions(
        Map<String, Dimension> origDims,
        String[] values,
        String dimName
    ) {
        Map<String, Dimension> newDimensions = new HashMap<>(origDims);
        Dimension dim = newDimensions.get(dimName).diceDimension(List.of(values));
        newDimensions.put(dimName, dim);
        return newDimensions;
    }

    /**
     * Agrupa las celdas que coinciden con los criterios de búsqueda.
     * Es escencial para las operaciones del cubo y la impresión por pantalla
     */
    private List<Cell> searchCells(
        List<Cell> cellsToSearch,
        String dimension,
        String key,
        String[] values
    ) {
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
     * Sobrecarga para searchCells(), usando las celdas que la instancia del cubo
     * posee.
     */
    private List<Cell> searchCells(String dimension, String key, String[] values) {
        return searchCells(this.cells, dimension, key, values);
    }

    /**
     * Sobrecarga para seachCells(), pero para dos dimensiones.
     */
    private List<Cell> searchCells(
        String dim1, String key1, String[] values1,
        String dim2, String key2, String[] values2
    ) {
        List<Cell> cellsDim1 = searchCells(dim1, key1, values1);
        List<Cell> resultCells = searchCells(cellsDim1, dim2, key2, values2);
        return resultCells;
    }

    /**
     * Sobrecarga para searchCells(), pero para tres dimensiones.
     */
    private List<Cell> searchCells(
        String dim1, String key1, String[] values1,
        String dim2, String key2, String[] values2,
        String dim3, String key3, String[] values3
    ) {
        List<Cell> cellsDim1 = searchCells(dim1, key1, values1);
        List<Cell> cellsDim2 = searchCells(cellsDim1, dim2, key2, values2);
        List<Cell> resultCells = searchCells(cellsDim2, dim3, key3, values3);
        return resultCells;
    }

    /**
     * Drill-down.
     * Reduce la dimensión a un nivel inferior en la dimensión especificada.
     *
     * @return true si la operación fue exitosa.
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
     * Roll-up.
     * Aumenta la dimensión a un nivel inferior en la dimensión especificada.
     *
     * @return true si la operación fue exitosa.
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
     * Slice.
     * Devuelve un subcubo con el valor de corte especificado para una dimensión.
     *
     * @param dimension Nombre de la dimensión
     * @param value     Valor a filtrar
     * @return Nuevo cubo resultado de la operación slice
     */
    public Cube slice(String dimension, String value) {
        String key = dimensions.get(dimension).getIdKey();
        List<Cell> newCells = searchCells(dimension, key, new String[] { value });
        return new Cube(
            getNewDimensions(this.dimensions,new String[] { value }, dimension),
            this.measures,
            this.facts,
            newCells
        );
    }

    /**
     * Dice.
     * Devuelve un subcubo con los valores de corte especificados para una
     * dimensión.
     *
     * @param dimension Nombre de la dimensión
     * @param values    Valores a filtrar
     * @return Nuevo cubo resultado de la operación dice
     */
    public Cube dice(String dimension, String[] values) {
        String key = dimensions.get(dimension).getIdKey();
        List<Cell> newCells = searchCells(dimension, key, values);
        return new Cube(
            getNewDimensions(this.dimensions, values, dimension),
            this.measures,
            this.facts,
            newCells
        );
    }

    /**
     * Sobrecarga de dice() para dos dimensiones.
     */
    public Cube dice(
        String dim1, String[] values1,
        String dim2, String[] values2
    ) {
        String key1 = dimensions.get(dim1).getIdKey();
        String key2 = dimensions.get(dim2).getIdKey();
        List<Cell> newCells = searchCells(dim1, key1, values1, dim2, key2, values2);
        Map<String, Dimension> newDims1 = getNewDimensions(this.dimensions, values1, dim1);
        Map<String, Dimension> newDims = getNewDimensions(newDims1, values2, dim2);
        return new Cube(newDims, this.measures, this.facts, newCells);
    }

    /**
     * Sobrecarga de dice() para tres dimensiones.
     */
    public Cube dice(
        String dim1, String[] values1,
        String dim2, String[] values2,
        String dim3, String[] values3
    ) {
        String key1 = dimensions.get(dim1).getIdKey();
        String key2 = dimensions.get(dim2).getIdKey();
        String key3 = dimensions.get(dim3).getIdKey();
        List<Cell> newCells = searchCells(
            dim1, key1, values1,
            dim2, key2, values2,
            dim3, key3, values3
        );
        Map<String, Dimension> newDims1 = getNewDimensions(this.dimensions, values1, dim1);
        Map<String, Dimension> newDims2 = getNewDimensions(newDims1, values2, dim2);
        Map<String, Dimension> newDims = getNewDimensions(newDims2, values3, dim3);
        return new Cube(newDims, this.measures, this.facts, newCells);
    }

}
