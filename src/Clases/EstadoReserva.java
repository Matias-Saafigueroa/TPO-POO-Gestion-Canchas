package Clases;

/**
 * [TPI B.2] Estructuras de Utilidad: Uso justificado de un Enum.
 * Controla la máquina de estados de una reserva en la lógica de negocio.
 */
public enum EstadoReserva {
    PENDIENTE,  // Creada, esperando pago
    CONFIRMADA, // Pagada
    CANCELADA   // Anulada
}