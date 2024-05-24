package api;

import java.io.IOException;
import java.util.List;

public interface DataParser {
    public List<List<String>> read(String filepath) throws IOException;
}
