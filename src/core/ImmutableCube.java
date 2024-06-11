package core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class ImmutableCube{
    private final String cubeName;
    private final Map<String, Dimension> dimensionMap;
    private final Facts facts;
    private final List<Cell> cells;

    public ImmutableCube(String cubeName) {
        this.cubeName = cubeName;
        dimensionMap = new HashMap<>();
        facts = null;
        cells = new ArrayList<>();
    }

    public ImmutableCube(String cubeName, Map<String, Dimension> map){
        this.cubeName = cubeName;
        dimensionMap = new HashMap<>();
        map.forEach((name, dimension) -> {
            dimensionMap.put(name, dimension);
        });
        facts = null;
        cells = new ArrayList<>();
    }

    public ImmutableCube(String cubeName, Map<String, Dimension> map, Facts facts){
        this.cubeName = cubeName;
        dimensionMap = new HashMap<>();
        map.forEach((name, dimension) -> {
            dimensionMap.put(name, dimension);
        });
        for (Dimension dim : dimensionMap.values()) {
            for (String level : dim.getLevels()) {
                if (facts.getFeatureNames().contains(level)) {
                    facts.removeFeature(level);
                }
            }
        }
        this.facts = facts;
        cells = new ArrayList<>();
        int cellNumber = 0;
        for (List<String> line : facts.getRaw()) {
            Cell cell = new Cell();
            for (String fact : facts.getFeatureNames()) {
                int columnFact = facts.getFeatureColumn(fact);
                cell.addFact(fact, Double.parseDouble(line.get(columnFact)));
            }
            cells.add(cell);

            for (Dimension dimension : dimensionMap.values()) {
                int columnForeignKey = dimension.getForeignKey();
                int fk = Integer.parseInt(line.get(columnForeignKey));
                dimension.addFact(fk, cellNumber);
            }

            cellNumber++;
        }
    }

    public ImmutableCube addDimension(String name, Dimension dimension) {
        Map<String, Dimension> mapa = new HashMap<>();
        dimensionMap.forEach((nameOld, dimensionOld) -> {
            mapa.put(nameOld, dimensionOld);
        });
        mapa.put(name, dimension);
        return new ImmutableCube(cubeName, mapa);
    }

    public ImmutableCube addFacts(Facts facts) {
        Map<String, Dimension> mapa = new HashMap<>();
        dimensionMap.forEach((nameOld, dimensionOld) -> {
            mapa.put(nameOld, dimensionOld);
        });
        return new ImmutableCube(cubeName, mapa, facts);
    }

    public String getCubeName() {
        return this.cubeName;
    }

    public Set<String> getDimensionNames() {
        return dimensionMap.keySet();
    }

    public Set<String> getFeatureNames() {
        return facts.getFeatureNames();
    }

    public Dimension getDimension(String name) {
        return dimensionMap.get(name);
    }

    public Collection<Dimension> getDimensions() {
        return dimensionMap.values();
    }

    public Set<Object> getLevel(String dimensionName, String levelName) {
        return this.getDimension(dimensionName).getLevel(levelName);
    }

    private List<Cell> celdasFromIndices(Set<Integer> indices) {
        List<Cell> celdas = new ArrayList<>();
        for (Integer indice : indices) {
            celdas.add(this.cells.get(indice));
        }
        return celdas;
    }

    public Cell getCelda(Dimension dimension, String valor) {
        return Cell.group(celdasFromIndices(dimension.getCellIndices(valor)));
    }

    public Cell getCelda(Dimension dim1, String valor1, Dimension dim2, String valor2) {
        Set<Integer> indicesComunes = celdasComunes(dim1.getCellIndices(valor1), dim2.getCellIndices(valor2));
        return Cell.group(celdasFromIndices(indicesComunes));
    }

    private static Set<Integer> celdasComunes(Set<Integer> set1, Set<Integer> set2) {
        Set<Integer> nuevo = new HashSet<>(set1);
        nuevo.retainAll(set2);
        return nuevo;
    }
}