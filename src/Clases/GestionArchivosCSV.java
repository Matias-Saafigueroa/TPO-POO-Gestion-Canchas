package Clases;

import interfaces.IGestionArchivos;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * [TPI C.2] Separación de Persistencia: Esta es la ÚNICA clase del sistema
 * que interactúa directamente con el disco (java.io).
 * [TPI C.3] Cumple con la abstracción al implementar la interfaz IGestionArchivos (DAO).
 */
public class GestionArchivosCSV implements IGestionArchivos {

    /**
     * [TPI B.4] Gestión de Errores: El método declara 'throws IOException'
     * para obligar a las capas superiores (Gestores) a manejar los fallos.
     */
    @Override
    public ArrayList<String> leerArchivo(String ruta) throws IOException {
        ArrayList<String> lineas = new ArrayList<>();

        // [TPI B.4] Uso de try-with-resources: Asegura que el BufferedReader se cierre
        // automáticamente, evitando fugas de memoria o bloqueos de archivo.
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }
        }
        // Nota: Si el archivo no existe, lanzará IOException (FileNotFoundException)
        // que será manejada por el constructor del Gestor.

        return lineas;
    }

    @Override
    public void escribirArchivo(String ruta, ArrayList<String> lineas) throws IOException {
        // [TPI B.4] Uso de try-with-resources para escritura segura.
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta, false))) { // false = sobrescribir
            for (String linea : lineas) {
                bw.write(linea);
                bw.newLine();
            }
        }
    }
}