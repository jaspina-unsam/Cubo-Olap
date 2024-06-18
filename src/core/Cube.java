package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import metrics.Measure;

/**
 * La clase Cubo representa un cubo OLAP.
 */
public class Cube {
    private Map<String, Dimension> dimensions;
    private List<Measure> measures;
    private List<String> facts;
    private List<Cell> cells;
    private int selectedFact;
    private int selectedMeasure;

    public Cube() {
        this.dimensions = new HashMap<>();
        this.measures = new ArrayList<>();
        this.facts = new ArrayList<>();
        this.cells = new ArrayList<>();
        this.selectedFact = 0;
        this.selectedMeasure = 0;
    }

    private Cube(Map<String, Dimension> dimensions, List<Measure> metrics, List<String> facts, List<Cell> cells) {
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

    public Set<String> getDimensionNames() {
        return dimensions.keySet();
    }

    public Dimension getDimension(String name) {
        if (!dimensions.containsKey(name)) {
            throw new IllegalArgumentException("Dimension not found");
        }
        return dimensions.get(name);
    }

    public boolean drillDown(String dimension) {
        Dimension dim = dimensions.get(dimension);
        if (dim.getCurrentLevel() < dim.getLevels().size() - 1) {
            dim.setCurrentLevel(dim.getCurrentLevel() + 1);
            return true;
        }
        return false;
    }

    public boolean rollUp(String dimension) {
        Dimension dim = dimensions.get(dimension);
        if (dim.getCurrentLevel() > 0) {
            dim.setCurrentLevel(dim.getCurrentLevel() - 1);
            return true;
        }
        return false;
    }

    public void addCell(Cell cell) {
        cells.add(cell);
    }

    public List<Cell> getCells() {
        return cells;
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

    public Measure getSelectedMeasure() {
        return measures.get(selectedMeasure);
    }

    private List<Integer> getIdsToSearch(String dimension, String[] values) {
        List<Integer> idsToSearch = new ArrayList<>();
        for (String value : values) {
            idsToSearch.addAll(dimensions.get(dimension).getIdList(value));
        }
        return idsToSearch;
    }

    private List<Cell> searchCells(String dimension, String key, String[] values) {
        return searchCells(this.cells, dimension, key, values);
    }

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

    private List<Cell> searchCells(String dim1, String key1, String[] values1, String dim2, String key2, String[] values2) {
        List<Cell> cellsDim1 = searchCells(dim1, key1, values1);
        List<Cell> resultCells = searchCells(cellsDim1, dim2, key2, values2);

        return resultCells;
    }

    private List<Cell> searchCells(String dim1, String key1, String[] values1, String dim2, String key2, String[] values2, String dim3, String key3, String[] values3) {
        List<Cell> cellsDim1 = searchCells(dim1, key1, values1);
        List<Cell> cellsDim2 = searchCells(cellsDim1, dim2, key2, values2);
        List<Cell> resultCells = searchCells(cellsDim2, dim3, key3, values3);

        return resultCells;
    }

    private Map<String, Dimension> getNewDimensions(Map<String, Dimension> origDims, String[] values, String dimName) {
        Map<String, Dimension> newDimensions = new HashMap<>(origDims);
        Dimension dim = newDimensions.get(dimName).diceDimension(List.of(values));
        newDimensions.put(dimName, dim);
        return newDimensions;
    }

    public Cube slice(String dimension, String value) {
        String key = dimensions.get(dimension).getIdKey();
        List<Cell> newCells = searchCells(dimension, key, new String[] { value });
        return new Cube(getNewDimensions(this.dimensions, new String[] {value}, dimension), this.measures, this.facts, newCells);
    }

    public Cube dice(String dimension, String[] values) {
        String key = dimensions.get(dimension).getIdKey();
        List<Cell> newCells = searchCells(dimension, key, values);
        return new Cube(getNewDimensions(this.dimensions, values, dimension), this.measures, this.facts, newCells);
    }

    public Cube dice(String dim1, String[] values1, String dim2, String[] values2) {
        String key1 = dimensions.get(dim1).getIdKey();
        String key2 = dimensions.get(dim2).getIdKey();
        List<Cell> newCells = searchCells(dim1, key1, values1, dim2, key2, values2);
        Map<String, Dimension> newDims1 = getNewDimensions(this.dimensions, values1, dim1);
        Map<String, Dimension> newDims = getNewDimensions(newDims1, values2, dim2);
        return new Cube(newDims, this.measures, this.facts, newCells);
    }

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
        return "Cube [Cells=" + cells.size() + ']';
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

}
