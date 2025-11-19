package Clases;

import java.time.LocalDate;

/**
 * [TPI A.1] Herencia: Clase Base Abstracta.
 */
public abstract class Pago {
    private int idPago;

    // [TPI A.3] Composición Correcta:
    // Guardamos 'idReserva' en lugar del objeto 'Reserva' para evitar ciclos infinitos.
    private int idReserva;
    private LocalDate fechaPago;

    public Pago(int idPago, int idReserva, LocalDate fechaPago) {
        this.idPago = idPago;
        this.idReserva = idReserva;
        this.fechaPago = fechaPago;
    }

    // ... (Getters y Setters) ...

    @Override
    public String toString() {
        return "Pago{" +
                "idPago=" + idPago +
                ", idReserva=" + idReserva +
                ", fechaPago=" + fechaPago +
                '}';
    }

    // [TPI A.1] Polimorfismo: Método abstracto obligatorio para las hijas.
    public abstract String toCSVString();
}