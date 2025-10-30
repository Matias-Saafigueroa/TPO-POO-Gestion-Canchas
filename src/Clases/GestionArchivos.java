package Clases;

import interfaces.IGestionArchivos;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestionArchivos implements IGestionArchivos {

    @Override
    public List<String> leerArchivo(String ruta) throws IOException {
        File archivo = new File(ruta);
        List<String> canchas = new ArrayList<>();
        if (!archivo.exists()) {
            System.out.println(" El archivo no existe: " + ruta);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            System.out.println(" Contenido del archivo:");
            while ((linea = br.readLine()) != null) {


            }
        }
    }

    public void actualizarArchivo(String ruta, List<String> contenidoAdicional) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta, true))) {
            for (String linea : contenidoAdicional) {
                bw.write(linea);
                bw.newLine();
            }
        }
        System.out.println(" Archivo actualizado correctamente: " + ruta);
    }

    @Override
    public void borrarArchivo(String ruta) {
        File archivo = new File(ruta);
        if (!archivo.exists()) {
            System.out.println(" El archivo no existe: " + ruta);
            return;
        }

        if (archivo.delete()) {
            System.out.println(" Archivo eliminado correctamente: " + ruta);
        } else {
            System.out.println(" No se pudo eliminar el archivo: " + ruta);
        }
    }
}
