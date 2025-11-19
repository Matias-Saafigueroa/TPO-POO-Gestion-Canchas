package Excepciones;

/**
 * [TPI B.3] Excepción de Negocio para la gestión de Reservas.
 * Se lanza ante problemas de disponibilidad, fechas inválidas o pagos fallidos.
 */
public class ReservaException extends Exception {
    public ReservaException(String message) {
        super(message);
    }
}