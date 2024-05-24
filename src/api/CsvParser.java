package api;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CsvParser implements DataParser{

    public List<List<String>> readData(String filepath, String delimiter, boolean dropIndex) throws IOException {
        List<List<String>> data = new ArrayList<>();
        BufferedReader buffer = new BufferedReader(new FileReader(filepath));
        String line;
        while ((line = buffer.readLine()) != null) {
            List<String> values = _getSplitLine(line, delimiter);
            if (dropIndex) {
                data.add(values.subList(1, values.size()));
            } else {
                data.add(values);
            }
        }
        buffer.close();

        return data;
    }

    private List<String> _getSplitLine(String inputString, String delimiter) {
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

    @Override
    public List<List<String>> read(String filepath) throws IOException {
        return readData(filepath, ",", true);
    }

}