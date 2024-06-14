package api;

import java.io.IOException;
import java.util.List;

/**
 * La interfax DataParser define un metodo para leer datos de un archivo.
 */
public interface DataParser {
    /**
     * Lee datos de un archivo y los devuelve en una lista de listas de cadenas.
     * @return List<List<String>>
     */
    public List<List<String>> read(String filepath) throws IOException;
}
