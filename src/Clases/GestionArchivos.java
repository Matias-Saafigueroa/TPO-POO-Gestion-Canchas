package Clases;

import interfaces.IGestionArchivos;
import java.io.*;
import java.util.ArrayList;

public class GestionArchivos implements IGestionArchivos {

    @Override
    public ArrayList<String> leerArchivo(String ruta) throws IOException {
        ArrayList<String> lineas = new ArrayList<>();
        File archivo = new File(ruta);

        // Si el archivo no existe, simplemente devuelve una lista vacía
        if (!archivo.exists()) {
            return lineas;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }
        }
        return lineas;
    }

    @Override
    public void escribirArchivo(String ruta, ArrayList<String> lineas) throws IOException {
        // 'false' en FileWriter significa que reescribe el archivo
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta, false))) {
            for (String linea : lineas) {
                bw.write(linea);
                bw.newLine();
            }
        }
    }
}