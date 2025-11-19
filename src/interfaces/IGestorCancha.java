package interfaces;

import Clases.Cancha;
import Excepciones.CanchaException;
import java.io.IOException;
import java.util.List;

/**
 * [TPI A.2] Contrato para la lógica de canchas.
 */
public interface IGestorCancha {

    void agregarCancha(Cancha cancha) throws CanchaException, IOException;
    void eliminarCancha(int idCancha) throws CanchaException, IOException;
    void asignarPrecio(int idCancha, double precio) throws CanchaException, IOException;

    Cancha buscarCancha(int idCancha);

    // [INFO] Devuelve List (la interfaz) en lugar de ArrayList (la implementación).
    // Es una buena práctica de abstracción.
    List<Cancha> obtenerCanchas();

    boolean validarDatos(String nombre);
    int asignarId();
}