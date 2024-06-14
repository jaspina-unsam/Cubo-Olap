package config;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import api.DataParser;
import api.DataType;
import core.Cell;
import core.Cube;
import core.Dimension;
import core.Level;
import metrics.Count;
import metrics.Max;
import metrics.Min;
import metrics.Sum;

public class CubeBuilder {
    private List<Dimension> dimensions;
    private List<Cell> cells;
    private List<String> foreignKeys;
    private List<String> facts;

    public CubeBuilder() {
        this.dimensions = new ArrayList<>();
        this.cells = new ArrayList<>();
        this.foreignKeys = new ArrayList<>();
        this.facts = new ArrayList<>();
    }

    public void addDimension(String name, String idKey, List<DataType> model, DataParser parser, String path) throws IOException {
        Map<String, Level> levels = new HashMap<>();
        List<List<String>> data = parser.read(path);
        List<String> headers = data.get(0);

        // Validaciones
        if (headers.size() != model.size()) {
            throw new IOException("The model and the data do not match");
        }
        if (headers.indexOf(idKey) == -1) {
            throw new IOException("The id key does not exist in the data");
        }

        foreignKeys.add(idKey);

        for (int i = 0; i < headers.size(); i++) {
            List<Object> elements = new ArrayList<>();
            for (int j = 1; j < data.size(); j++) {
                switch (model.get(i)) {
                    case INTEGER:
                        elements.add(Integer.parseInt(data.get(j).get(i)));
                        break;
                    case STRING:
                        elements.add(data.get(j).get(i));
                        break;
                    case FLOAT:
                        elements.add(Float.parseFloat(data.get(j).get(i)));
                        break;
                    case DATE:
                        elements.add(LocalDate.parse(data.get(j).get(i)));
                        break;
                    default:
                        elements.add(data.get(j).get(i));
                        break;
                }
            }
            levels.put(headers.get(i), new Level(headers.get(i), elements));
        }

        List<String> hierarchy = new ArrayList<>(headers);
        hierarchy.remove(idKey);
        Collections.reverse(hierarchy);
        this.dimensions.add(new Dimension(name, idKey, levels, hierarchy));
    }

    public void addFacts(String name, DataParser parser, String path) throws IOException {
        List<List<String>> data = parser.read(path);
        List<String> headers = data.get(0);

        if (foreignKeys.size() == 0) {
            throw new IOException("There are no dimensions to relate the facts");
        } else if (dimensions.size() != foreignKeys.size()) {
            throw new IOException("The number of dimensions and foreign keys do not match");
        }
        if (!headers.containsAll(foreignKeys)) {
            throw new IOException("The foreign keys do not exist in the data");
        }

        this.facts.addAll(headers);
        this.facts.removeAll(foreignKeys);

        for (int i = 1; i < data.size(); i++) {
            Map<String, Integer> keys = new HashMap<>();
            Map<String, List<Double>> facts = new HashMap<>();
            for (int j = 0; j < headers.size(); j++) {
                if (foreignKeys.contains(headers.get(j))) {
                    keys.put(headers.get(j), Integer.parseInt(data.get(i).get(j)));
                } else {
                    if (facts.containsKey(headers.get(j))) {
                        facts.get(headers.get(j)).add(Double.parseDouble(data.get(i).get(j)));
                    } else {
                        facts.put(headers.get(j), new ArrayList<>(Arrays.asList(Double.parseDouble(data.get(i).get(j)))));
                    }
                }
            }
            this.cells.add(new Cell(keys, facts));
        }
    }

    public Cube buildCube() {
        Cube cube = new Cube();
        for (Dimension d : this.dimensions) {
            cube.addDimension(d);
        }
        for (Cell c : this.cells) {
            cube.addCell(c);
        }
        for (String f : this.facts) {
            cube.addFact(f);
        }
        addMeasures(cube);
        return cube;
    }

    private void addMeasures(Cube cube) {
        cube.addMeasure(new Count());
        cube.addMeasure(new Sum());
        cube.addMeasure(new Min());
        cube.addMeasure(new Max());
    }
}