package core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class InmutableCube{
    private final Map<String, Dimension> dimensionMap;
    private final Facts facts;
    private final String cubeName;


    public InmutableCube(String cubeName) {
        this.cubeName = cubeName;
        dimensionMap = new HashMap<>();
        facts = null;
    }

    public InmutableCube(String cubeName, Map<String, Dimension> map){
        //usamos un mapa para guardar las dimensiones y acceder luego a ellas
        this.cubeName = cubeName;
        dimensionMap = new HashMap<>();
        //por cada nombre y dimension en el mapa, se agrega la dimension al cubo
        map.forEach((name, dimension) -> {
            dimensionMap.put(name, dimension);
        });
        facts = null;
    }

    public InmutableCube(String cubeName, Map<String, Dimension> map, Facts facts){
        this.cubeName = cubeName;
        dimensionMap = new HashMap<>();
        //por cada nombre y dimension en el mapa, se agrega la dimension al cubo
        map.forEach((name, dimension) -> {
            dimensionMap.put(name, dimension);
        });
        this.facts = facts;
    }

    public InmutableCube addDimension(String name, Dimension dimension) {
        Map<String, Dimension> mapa = new HashMap<>();
        //por cada nombre y dim anterior en el mapa, se agrega la dimension al cubo
        dimensionMap.forEach((nameOld, dimensionOld) -> {
            mapa.put(nameOld, dimensionOld);
        });
        //agrego nueva dimension
        mapa.put(name, dimension);
        return new InmutableCube(cubeName, mapa);
    }

    public InmutableCube addFacts(Facts facts) {
        Map<String, Dimension> mapa = new HashMap<>();
        //por cada nombre y dim anterior en el mapa, se agrega la dimension al cubo
        dimensionMap.forEach((nameOld, dimensionOld) -> {
            mapa.put(nameOld, dimensionOld);
        });
        // agrego fact tambien 
        return new InmutableCube(cubeName, mapa, facts);
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

}