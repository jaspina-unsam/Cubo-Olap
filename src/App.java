import java.util.List;
import api.CsvParser;
import core.Dimension;
import core.Facts;
import core.ImmutableCube;

public class App {
    public static void main(String[] args) throws Exception {
        CsvParser csvParser = new CsvParser();
        ImmutableCube cube = new ImmutableCube("el cubito");
        List<List<String>> csvFechas = csvParser.read("src/data/fechas.csv");
        List<List<String>> csvProductos = csvParser.read("src/data/productos.csv");
        List<List<String>> csvPuntosVenta = csvParser.read("src/data/puntos_venta.csv");
        List<List<String>> csvVentas = csvParser.read("src/data/ventas.csv");
        
        cube = cube.addDimension("fechas", new Dimension(csvFechas, 2));
        cube = cube.addDimension("productos", new Dimension(csvProductos, 0));
        cube = cube.addDimension("puntos_venta", new Dimension(csvPuntosVenta, 1));
        cube = cube.addFacts(new Facts(csvVentas));

        System.out.println(cube.getDimensionNames());
        System.out.println(cube.getDimension("fechas").getLevels());
        System.out.println(cube.getDimension("fechas").getLevel("quarter"));
        System.out.println(cube.getLevel("fechas", "quarter"));
        System.out.println(cube.getLevel("fechas", "anio"));

        System.out.println(cube.getDimension("fechas").getIdList("quarter","3"));
        System.out.println(cube.getDimension("fechas").getIdList("fecha","2017-07-10"));
        System.out.println(cube.getFeatureNames());
        System.out.println("el cubo se llama " + cube.getCubeName());
    }
}
