package Clases;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * [SOLID SRP] Clase de Modelo pura (sin lógica de negocio).
 * [TPI A.3] Relaciones Robustas implementadas aquí.
 */
public class Reserva {
    private int idReserva;

    // [TPI A.3] Agregación: La reserva "conoce" al cliente y la cancha,
    // pero estos existen independientemente de la reserva.
    private Cliente cliente;
    private Cancha cancha;

    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private double montoTotal;

    // [TPI A.3] Composición: La reserva "posee" al pago.
    // El pago se crea específicamente para esta reserva.
    private Pago pago;

    // [TPI B.2] Uso de Enum para la máquina de estados.
    private EstadoReserva estado;

    public Reserva(int idReserva, Cliente cliente, Cancha cancha, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, double montoTotal, Pago pago) {
        this.idReserva = idReserva;
        this.cliente = cliente;
        this.cancha = cancha;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.montoTotal = montoTotal;
        this.pago = pago;
        this.estado = EstadoReserva.PENDIENTE; // Estado inicial por defecto
    }

    // ... (Getters y Setters) ...

    /**
     * [TPI C.1] Persistencia de Relaciones:
     * Guardamos los IDs de los objetos relacionados, no los objetos completos.
     * Esto mantiene la integridad referencial en el CSV.
     */
    public String toCSVString() {
        int idCliente = (this.cliente != null) ? this.cliente.getIdCliente() : 0;
        int idCancha = (this.cancha != null) ? this.cancha.getIdCancha() : 0;
        int idPago = (this.pago != null) ? this.pago.getIdPago() : 0;

        return idReserva + ";" +
                idCliente + ";" +
                idCancha + ";" +
                fecha + ";" +
                horaInicio + ";" +
                horaFin + ";" +
                montoTotal + ";" +
                idPago + ";" +
                estado;
    }

    // ... (toString) ...
}