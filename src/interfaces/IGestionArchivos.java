package interfaces;

import java.io.IOException;
import java.util.ArrayList;

/**
 * [TPI C.3] Abstracción de DB / Persistencia (DAO Interface).
 * Define el contrato genérico para guardar datos, sin importar si es CSV, SQL o JSON.
 *
 * [PATRÓN] Low Coupling (Bajo Acoplamiento): Los gestores usan esta interfaz
 * para no "casarse" con una implementación específica de archivos.
 */
public interface IGestionArchivos {

    // [TPI B.4] Gestión de Errores: Declaramos 'throws IOException' para obligar
    // a manejar errores de entrada/salida (disco lleno, sin permisos, archivo bloqueado).

    /**
     * Lee un archivo y devuelve sus líneas.
     */
    public ArrayList<String> leerArchivo(String ruta) throws IOException;

    /**
     * Guarda una lista de strings en un archivo.
     */
    public void escribirArchivo(String ruta, ArrayList<String> lineas) throws IOException;
}