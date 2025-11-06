package interfaces;

import java.io.IOException;
import java.util.ArrayList;

public interface IGestionArchivos {
    // Devuelve un ArrayList con cada línea del archivo
    public ArrayList<String> leerArchivo(String ruta) throws IOException;

    // Escribe un ArrayList de líneas en un archivo
    public void escribirArchivo(String ruta, ArrayList<String> lineas) throws IOException;

}
