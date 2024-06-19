package api;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * La clase CsvParser implementa la interfaz DataParser y lee datos de un archivo CSV.
 * @see DataParser
 */
public class CsvParser implements DataParser {
    private String delimiter;           // Delimitador que usa para determinar que carácter separa las columnas
    private boolean dropIndex;          // Booleano que determina si ignora el índice o no

    /**
     * Constructor por defecto de la clase
     */
    public CsvParser() {
        this.delimiter = ";";
        this.dropIndex = false;
    }

    /**
     * Constructor de la clase
     * 
     * @param delimeter Delimitador que usa para determinar que carácter separa las columnas
     * @param dropIndex Booleano que determina si ignora el índice o no
     */
    public CsvParser(String delimiter, boolean dropIndex) {
        this.delimiter = delimiter;
        this.dropIndex = dropIndex;
    }

    @Override
    public List<List<String>> read(String filepath) throws IOException {
        List<List<String>> data = new ArrayList<>();
        BufferedReader buffer = new BufferedReader(new FileReader(filepath));
        String line;
        while ((line = buffer.readLine()) != null) {
            List<String> values = splitLine(line);
            if (dropIndex) {
                data.add(values.subList(1, values.size()));
            } else {
                data.add(values);
            }
        }
        buffer.close();

        return data;
    }

    /**
     * Método para dividir un string en una lista de strings.
     * @param inputString String a dividir
     * @return List<String> lista de strings 
     */
    private List<String> splitLine(String inputString) {
        List<String> splitList = new ArrayList<>();
        String outputString = inputString;
        String auxDelimiter = "+";

        Pattern p = Pattern.compile("(\"[^\"]*\")");
        Matcher m = p.matcher(inputString);
        if (m.find()) {
            String target = m.group(1);
            String replaced = target.replace(delimiter, auxDelimiter).replace("\"", "");
            outputString = inputString.replace(target, replaced);
            for (String v : outputString.split(delimiter)) {
                splitList.add(v.replace(auxDelimiter, delimiter));
            }
        }
        else {
            splitList = Arrays.asList(outputString.split(delimiter));
        }

        return splitList;
    }
}
