package Clases;

import interfaces.IGestionArchivos;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * (C.2) CLASE DE PERSISTENCIA (DAO)
 * Implementa la interfaz IGestionArchivos.
 * Es la ÚNICA clase en el sistema que sabe cómo leer y escribir
 * en archivos CSV.
 */
public class GestionArchivosCSV implements IGestionArchivos {

    /**
     * Lee todas las líneas de un archivo de texto.
     * (B.4) Utiliza try-with-resources para manejar el buffer.
     * @param ruta La ruta del archivo a leer.
     * @return Un ArrayList de Strings, donde cada String es una línea.
     * @throws IOException Si ocurre un error de E/S.
     */
    @Override
    public ArrayList<String> leerArchivo(String ruta) throws IOException {
        ArrayList<String> lineas = new ArrayList<>();

        // (B.4) Bloque try-with-resources
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }
        } catch (FileNotFoundException e) {
            // Si el archivo no existe, no es un error,
            // simplemente devolvemos la lista vacía.
            // (La primera vez que corras el programa, los CSV no existirán)
        }
        return lineas;
    }

    /**
     * Escribe (sobrescribe) una lista de Strings en un archivo de texto.
     * (B.4) Utiliza try-with-resources para manejar el buffer.
     * @param ruta La ruta del archivo a escribir.
     * @param lineas Las líneas a guardar en el archivo.
     * @throws IOException Si ocurre un error de E/S.
     */
    @Override
    public void escribirArchivo(String ruta, ArrayList<String> lineas) throws IOException {

        // (B.4) Bloque try-with-resources
        // 'false' en FileWriter significa que sobrescribe el archivo
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta, false))) {
            for (String linea : lineas) {
                bw.write(linea);
                bw.newLine(); // Agrega el salto de línea
            }
        }
        // (El 'catch' lo maneja el método que llamó a este,
        // gracias al 'throws IOException')
    }
}