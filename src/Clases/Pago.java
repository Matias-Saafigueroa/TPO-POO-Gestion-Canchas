package Clases;

import java.time.LocalDate;

/**
 * (A.1) Requisito TPI: Clase Abstracta Base.
 * Define la base para todas las formas de pago.
 */
public abstract class Pago {

    private int idPago;

    // --- CORRECCIÓN ---
    // (A.3) Composición: El Pago "pertenece a" una Reserva.
    // Guardamos solo el ID para evitar dependencias circulares.
    private int idReserva;

    private LocalDate fechaPago;

    /**
     * Constructor corregido para ser usado por las clases hijas.
     * @param idReserva El ID de la reserva a la que pertenece este pago.
     */
    public Pago(int idPago, int idReserva, LocalDate fechaPago) {
        this.idPago = idPago;
        this.idReserva = idReserva; // Corregido
        this.fechaPago = fechaPago;
    }

    // --- Getters y Setters ---

    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    @Override
    public String toString() {
        // Corregido para no tener recursión infinita
        return "Pago{" +
                "idPago=" + idPago +
                ", idReserva=" + idReserva + // Corregido
                ", fechaPago=" + fechaPago +
                '}';
    }

    /**
     * (A.1) Polimorfismo: (Opcional pero recomendado)
     * Las hijas (Tarjeta/Efectivo) pueden implementar esto
     * para devolver su formato CSV específico.
     */
    public abstract String toCSVString();
}