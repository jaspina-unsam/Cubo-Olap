package api;

import java.util.Set;
import java.util.TreeSet;

import core.Cell;
import core.Cube;
import core.Level;
import metrics.Measure;

/**
 * La clase TablePrinter imprime en consola los datos de un cubo.
 * El método display() está sobrecargado; imprime los datos en forma de tabla por CLI.
 */
public class TablePrinter {

    /**
     * Impresión en pantalla con todas las dimensiones reducidas a una sola celda.
     */
    public static void display(Cube cube) {
        String selectedFact = cube.getSelectedFact();
        Measure measure = cube.getSelectedMeasure();
        String[][] tableData = new String[2][1];
        tableData[0][0] = String.format("%s (%s)", selectedFact, measure.getName());

        Cell cell = cube.getCell();
        tableData[1][0] = String.format("%.2f", measure.calc(cell.getFacts(selectedFact)));
        print(tableData);
    }

    /**
     * Impresión en pantalla de una tabla de N+1 filas y 2 columnas.
     * Las N filas corresponden a los elementos únicos del nivel activo de la dimensión.
     * La primera columna son las etiquetas, la segunda los valores.
     */
    public static void display(Cube cube, String dimension) {
        Level lvl = cube.getDimension(dimension).getActiveLevel();
        Set<Object> levelElements = new TreeSet<>(lvl.getElements());
        String selectedFact = cube.getSelectedFact();
        Measure measure = cube.getSelectedMeasure();
        String[][] tableData = new String[levelElements.size() + 1][2];
        tableData[0][0] = String.format("%s (%s)", lvl.getName(), dimension);
        tableData[0][1] = String.format("%s (%s)", selectedFact, measure.getName());

        for (int i = 0; i < levelElements.size(); i++) {
            String element = levelElements.toArray()[i].toString();
            Cell cell = cube.getCell(dimension, element.toString());
            String value = String.format("%.2f", measure.calc(cell.getFacts(selectedFact)));
            tableData[i + 1][0] = element;
            tableData[i + 1][1] = value;
        }
        print(tableData);
    }

    /**
     * Impresión en pantalla de una tabla de N+1 x M+1.
     * Las N filas corresponden a los elementos únicos del nivel activo de la dimensión rowDim.
     * Las M columnas corresponden a los elementos únicos del nivel activo de la dimensión colDim.
     * A diferencia del display con una sola dimensión, se agrega un mensaje adicional,
     * que informa las dimensiones y medidas seleccionadas.
     */
    public static void display(Cube cube, String rowDim, String colDim) {
        Level rowLevel = cube.getDimension(rowDim).getActiveLevel();
        Level colLevel = cube.getDimension(colDim).getActiveLevel();
        Set<Object> rowElements = new TreeSet<>(rowLevel.getElements());
        Set<Object> colElements = new TreeSet<>(colLevel.getElements());
        String selectedFact = cube.getSelectedFact();
        Measure measure = cube.getSelectedMeasure();
        String[][] tableData = new String[rowElements.size() + 1][colElements.size() + 1];
        tableData[0][0] = String.format("%s (%s)", rowLevel.getName(), rowDim);

        for (int i = 0; i < colElements.size(); i++) {
            tableData[0][i + 1] = colElements.toArray()[i].toString();
        }

        int row = 1;
        for (Object rowElement : rowElements) {
            tableData[row][0] = rowElement.toString();
            int col = 1;
            for (Object colElement : colElements) {
                Cell cell = cube.getCell(rowDim, rowElement.toString(), colDim, colElement.toString());
                String value = String.format("%.2f", measure.calc(cell.getFacts(selectedFact)));
                tableData[row][col] = value;
                col++;
            }
            row++;
        }

        System.out.println(String.format("%s (%s) vs %s (%s) [%s (%s)]", rowLevel.getName(), rowDim,
                colLevel.getName(), colDim, selectedFact, measure.getName()));
        print(tableData);
    }

    /**
     * Usa la tableData y la imprime en pantalla.
     */
    private static void print(String[][] tableData) {
        if (tableData == null || tableData.length == 0) {
            System.out.println("No data to display.");
            return;
        }

        int[] columnWidths = getColumnWidths(tableData);

        printSeparator(columnWidths);
        for (String[] row : tableData) {
            printRow(row, columnWidths);
            printSeparator(columnWidths);
        }
        System.out.println("\n");
    }

    /**
     * Método auxiliar de print() que calcula los tamaños.
     */
    private static int[] getColumnWidths(String[][] tableData) {
        int columns = tableData[0].length;
        int[] columnWidths = new int[columns];

        for (String[] row : tableData) {
            for (int i = 0; i < row.length; i++) {
                if (row[i].length() > columnWidths[i]) {
                    columnWidths[i] = row[i].length();
                }
            }
        }
        return columnWidths;
    }

    /**
     * Método auxiliar de print() que imprime las líneas divisorias horizontales.
     */
    private static void printSeparator(int[] columnWidths) {
        for (int width : columnWidths) {
            System.out.print("+");
            for (int i = 0; i < width + 2; i++) {
                System.out.print("-");
            }
        }
        System.out.println("+");
    }

    /**
     * Método auxiliar de print() que imprime las filas de la tabla.
     */
    private static void printRow(String[] row, int[] columnWidths) {
        for (int i = 0; i < row.length; i++) {
            System.out.print("| ");
            System.out.print(row[i]);
            for (int j = row[i].length(); j < columnWidths[i]; j++) {
                System.out.print(" ");
            }
            System.out.print(" ");
        }
        System.out.println("|");
    }
}
