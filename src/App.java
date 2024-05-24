import java.io.FileNotFoundException;
import java.io.IOException;

import api.DataParser;
import api.CsvReader;

public class App {
    public static void main(String[] args) {
        try {
            DataParser parser = new CsvReader();
            System.out.println(parser.read("data/fechas.csv"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
