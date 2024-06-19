import api.CsvParser;
import config.CubeBuilder;
import core.Cube;
import data.model.Model;
import api.TablePrinter;

public class App {
    public static void main(String[] args) throws Exception {
        // Creación del cubo
        CsvParser parser = new CsvParser(";", false);
        CubeBuilder builder = new CubeBuilder();

        // agregar dimensiones al cubo
        builder.addDimension("fechas", "id_fecha", Model.FECHA_DATA_TYPES, parser, "src/data/files/fechas.csv");
        builder.addDimension("productos", "id_producto", Model.PRODUCTO_DATA_TYPES, parser,
                "src/data/files/productos.csv");
        builder.addDimension("puntos_venta", "id_punto_venta", Model.PUNTO_VENTA_DATA_TYPES, parser,
                "src/data/files/puntos_venta.csv");
        builder.addFacts("ventas", parser, "src/data/files/ventas.csv");

        // buildear el cubo
        Cube cube = builder.buildCube();

        // print + consultas
        cube = cube.slice("fechas", "2018");
        cube.selectFact("cantidad");
        cube.selectMeasure("suma");
        TablePrinter.display(cube, "productos");

        cube.drillDown("puntos_venta");
        cube.drillDown("puntos_venta");
        cube.drillDown("fechas");
        cube.drillDown("fechas");

        TablePrinter.display(cube, "puntos_venta", "fechas");

        Cube dicedCube = cube.dice("puntos_venta", new String[] { "Alabama", "Alaska", "Arizona" });
        TablePrinter.display(dicedCube, "puntos_venta", "fechas");
    }
}
