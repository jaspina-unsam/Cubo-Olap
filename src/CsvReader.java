import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvReader {
    String filename;
    BufferedReader bufferedReader;

    public CsvReader(String path_to_file) throws FileNotFoundException {
        filename = path_to_file;
        bufferedReader = new BufferedReader(new FileReader(path_to_file));
    }

    public List<List<String>> getCsvAsTable() throws IOException {
        String line;
        List<List<String>> content = new ArrayList<>();

        while ((line = bufferedReader.readLine()) != null) {
            content.add(Arrays.asList(line.split(",")));
        }

        return content;
    }

    public void closeFile() throws IOException {
        bufferedReader.close();
    }

}