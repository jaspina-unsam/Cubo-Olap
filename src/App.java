import java.io.FileNotFoundException;
import java.io.IOException;

public class App {
    public static void main(String[] args) {
        try {
            CsvReader reader = new CsvReader("data/fechas.csv");
            System.out.println(reader.getCsvAsTable());
            reader.closeFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
