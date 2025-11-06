package interfaces;

import Clases.Cancha;
import Excepciones.CanchaException;
import java.io.IOException;
import java.util.List;

public interface IGestorCancha {

    // (B.3) Refactorizados
    void agregarCancha(Cancha cancha) throws CanchaException, IOException;
    void eliminarCancha(int idCancha) throws CanchaException, IOException;
    void asignarPrecio(int idCancha, double precio) throws CanchaException, IOException;

    // (Sin cambios)
    Cancha buscarCancha(int idCancha);
    List<Cancha> obtenerCanchas();
    boolean validarDatos(String nombre);
    int asignarId();
}