import config.CubeBuilder;
import core.Cube;
import data.model.Model;
import api.CsvParser;
import api.TablePrinter;

public class App {
    public static void main(String[] args) throws Exception {
        // Creación del cubo
        CsvParser parser = new CsvParser(";", false);
        CubeBuilder builder = new CubeBuilder();

        // agregar dimensiones al cubo
        builder.addDimension("fechas", "id_fecha", Model.FECHA_DATA_TYPES, parser, "src/data/files/fechas.csv");
        builder.addDimension("productos", "id_producto", Model.PRODUCTO_DATA_TYPES, parser, "src/data/files/productos.csv");
        builder.addDimension("puntos_venta", "id_punto_venta", Model.PUNTO_VENTA_DATA_TYPES, parser, "src/data/files/puntos_venta.csv");
        builder.addFacts("ventas", parser, "src/data/files/ventas.csv");

        // buildear el cubo
        Cube cube = builder.buildCube();
        System.out.println(cube.toString());
        Cube cube2 = builder.buildCube();

        cube.selectFact("cantidad");
        cube.selectMeasure("contar");

        TablePrinter.display(cube);

        // print + consultas
        cube = cube.slice("fechas", "2018");
        cube.selectFact("costo");
        cube.selectMeasure("suma");
        TablePrinter.display(cube, "fechas");

        cube.drillDown("puntos_venta");
        cube.drillDown("puntos_venta");
        cube.drillDown("fechas");
        cube.drillDown("fechas");

        TablePrinter.display(cube, "puntos_venta", "fechas");

        Cube dicedCube = cube2.dice("puntos_venta", new String[] { "California", "Alabama", "Arizona" });
        dicedCube.selectMeasure("min");
        dicedCube.selectFact("valor_total");
        TablePrinter.display(dicedCube, "puntos_venta", "fechas");

        Cube cube3 = builder.buildCube();
        cube3 = cube3.dice("fechas", new String[] { "2019", "2020" }, "puntos_venta", new String[] { "California" });
        cube3.selectMeasure("max");
        TablePrinter.display(cube3, "fechas", "puntos_venta");
        cube3.selectMeasure("contar");
        TablePrinter.display(cube3);

    }
}
