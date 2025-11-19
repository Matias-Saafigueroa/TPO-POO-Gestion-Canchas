package Clases;

import java.time.LocalDate;

public class PagoTarjeta extends Pago {
    private String numeroTarjeta;
    private int cuotas;
    private String entidadBancaria;

    public PagoTarjeta(int idPago, int idReserva, LocalDate fechaPago, String numeroTarjeta, int cuotas, String entidadBancaria) {
        super(idPago, idReserva, fechaPago);
        this.numeroTarjeta = numeroTarjeta;
        this.cuotas = cuotas;
        this.entidadBancaria = entidadBancaria;
    }

    // ... (Getters y Setters) ...

    @Override
    public String toCSVString() {
        return getIdPago() + ";" +
                getIdReserva() + ";" +
                getFechaPago() + ";" +
                "TARJETA;" +
                this.numeroTarjeta + ";" +
                this.cuotas + ";" +
                this.entidadBancaria;
    }

    /**
     * [GRASP Expert] Esta clase es la experta en saber cuánto recargo aplicar
     * porque conoce la cantidad de cuotas.
     */
    public double aplicarComision() {
        if (this.cuotas == 3) return 0.10;
        if (this.cuotas == 6) return 0.20;
        return 0.0;
    }

    @Override
    public String toString() {
        return "PagoTarjeta {" + super.toString() + ", cuotas=" + cuotas + ", entidad='" + entidadBancaria + "'}";
    }
}