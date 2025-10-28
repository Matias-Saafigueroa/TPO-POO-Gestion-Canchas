package Clases;

import java.time.LocalDate;

public abstract class Pago {
    private int idPago;
    private Reserva reserva;
    private LocalDate fechaPago;


    //constructor
    public Pago(int idPago, Reserva reserva, LocalDate fechaPago) {
        this.idPago = idPago;
        this.reserva = reserva;
        this.fechaPago = fechaPago;
    }

    //setters y getters


    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    @Override
    public String toString() {
        return "Pago{" +
                "idPago=" + idPago +
                ", reserva=" + reserva +
                ", fechaPago=" + fechaPago +
                '}';
    }
}
