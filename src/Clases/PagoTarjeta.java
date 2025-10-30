package Clases;

import java.time.LocalDate;

public class PagoTarjeta extends Pago{
    private String numeroTarjeta;
    private int cuotas;
    private String entidadBancaria;


    //constructor
    public PagoTarjeta(int idPago, Reserva reserva, LocalDate fechaPago, String numeroTarjeta, int cuotas, String entidadBancaria) {
        super(idPago, reserva, fechaPago);
        this.numeroTarjeta = numeroTarjeta;
        this.cuotas = cuotas;
        this.entidadBancaria = entidadBancaria;
    }

    //setter y getters

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public int getCuotas() {
        return cuotas;
    }

    public void setCuotas(int cuotas) {
        this.cuotas = cuotas;
    }

    public String getEntidadBancaria() {
        return entidadBancaria;
    }

    public void setEntidadBancaria(String entidadBancaria) {
        this.entidadBancaria = entidadBancaria;
    }

    @Override
    public String toString() {
        return "PagoTarjeta{" +
                "numeroTarjeta='" + numeroTarjeta + '\'' +
                ", cuotas=" + cuotas +
                ", entidadBancaria='" + entidadBancaria + '\'' +
                '}';
    }
    public double aplicarComision(){

    }
}
