package Clases;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * (A.4) SRP: Modelo de datos para una Reserva.
 * (A.3) AGREGACIÓN: "Tiene un" Cliente y "tiene una" Cancha.
 * (A.3) COMPOSICIÓN: "Posee un" Pago.
 */
public class Reserva {
    private int idReserva;
    private Cliente cliente; // (A.3) Agregación
    private Cancha cancha;   // (A.3) Agregación
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private double montoTotal;
    private Pago pago;       // (A.3) Composición
    private EstadoReserva estado; // (B.2) Uso de Enum

    /**
     * Constructor principal.
     * Asumimos que el estado inicial se setea en el Gestor.
     */
    public Reserva(int idReserva, Cliente cliente, Cancha cancha, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, double montoTotal, Pago pago) {
        this.idReserva = idReserva;
        this.cliente = cliente;
        this.cancha = cancha;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.montoTotal = montoTotal;
        this.pago = pago;
        this.estado = EstadoReserva.PENDIENTE; // Estado por defecto
    }

    // --- Getters y Setters Esenciales ---

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Cancha getCancha() {
        return cancha;
    }

    public void setCancha(Cancha cancha) {
        this.cancha = cancha;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }

    // --- (B.2) MÉTODOS DE ESTADO (CRÍTICOS PARA EL GESTOR) ---

    public EstadoReserva getEstado() {
        return estado;
    }

    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }


    // --- MÉTODOS REQUERIDOS POR EL TPI ---

    /**
     * (C.1) PERSISTENCIA: Implementación del método para guardar en CSV.
     * Guarda los IDs de los objetos agregados/compuestos, no los objetos enteros.
     *
     * Formato CSV Asumido:
     * idReserva;idCliente;idCancha;fecha;horaInicio;horaFin;montoTotal;idPago;estado
     */
    public String toCSVString() {
        // (A.3) Guardamos los IDs de las relaciones
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
                estado; // (B.2) Guardamos el Enum
    }

    /**
     * toString() corregido para evitar StackOverflowError.
     * Muestra solo los IDs de los objetos relacionados.
     */
    @Override
    public String toString() {
        return "Reserva {" +
                "idReserva=" + idReserva +
                ", idCliente=" + (cliente != null ? cliente.getIdCliente() : "N/A") +
                ", idCancha=" + (cancha != null ? cancha.getIdCancha() : "N/A") +
                ", fecha=" + fecha +
                ", horaInicio=" + horaInicio +
                ", montoTotal=" + montoTotal +
                ", idPago=" + (pago != null ? pago.getIdPago() : "N/A") +
                ", estado=" + estado +
                '}';
    }
}