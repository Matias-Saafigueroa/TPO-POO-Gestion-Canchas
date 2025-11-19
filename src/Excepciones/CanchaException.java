package Excepciones;

/**
 * [TPI B.3] Excepción de Negocio para la gestión de Canchas.
 * Se lanza cuando se intenta crear una cancha con nombre repetido
 * o eliminar una cancha que no existe.
 */
public class CanchaException extends Exception {
    public CanchaException(String message) {
        super(message);
    }
}