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
 *
 * @see DataParser
 */
public class CsvParser implements DataParser {
    private String delim;
    private boolean dropIndex; // Dropea la primera columna del CSV si es verdadero

    public CsvParser() {
        this.delim = ";";
        this.dropIndex = false;
    }

    /**
     * Constructor de la clase
     *
     * @param delim     Delimitador de los campos del CSV
     * @param dropIndex Indica si se debe dropear la primera columna del CSV
     */
    public CsvParser(String delimiter, boolean dropIndex) {
        this.delim = delimiter;
        this.dropIndex = dropIndex;
    }

    @Override
    public List<List<String>> read(String filepath) throws IOException {
        List<List<String>> data = new ArrayList<>();
        BufferedReader buffer = new BufferedReader(new FileReader(filepath));
        String line;
        while ((line = buffer.readLine()) != null) {
            List<String> values = splitLine(line);
            if (this.dropIndex) {
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
     *
     * @param inputString String a dividir
     * @return List<String> lista de strings
     */
    private List<String> splitLine(String inputString) {
        List<String> splitList = new ArrayList<>();
        String outputString = inputString;
        String aux = "+";

        Pattern p = Pattern.compile("(\"[^\"]*\")");
        Matcher m = p.matcher(inputString);
        if (m.find()) {
            /**
             * Si el string contiene comillas,
             * se reemplazan los delimitadores internos por un caracter auxiliar
             */
            String target = m.group(1);
            String replaced = target.replace(this.delim, aux).replace("\"", "");
            outputString = inputString.replace(target, replaced);
            for (String v : outputString.split(this.delim)) {
                splitList.add(v.replace(aux, this.delim));
            }
        } else {
            /**
             * En cualquier otro caso, se divide usando el delimitador normal
             */
            splitList = Arrays.asList(outputString.split(this.delim));
        }

        return splitList;
    }
}
