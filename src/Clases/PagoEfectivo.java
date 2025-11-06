package Clases;

import java.time.LocalDate;

/**
 * (A.1) Requisito TPI: Subclase de Pago
 */
public class PagoEfectivo extends Pago {

    private boolean abonadoEnCaja;

    /**
     * Constructor para ser usado por GestorReserva.
     * @param idPago ID único del pago.
     * @param idReserva ID de la reserva asociada.
     * @param fechaPago Fecha del pago.
     * @param abonadoEnCaja Si se marcó como abonado en caja.
     */
    public PagoEfectivo(int idPago, int idReserva, LocalDate fechaPago, boolean abonadoEnCaja) {
        super(idPago, idReserva, fechaPago);
        this.abonadoEnCaja = abonadoEnCaja;
    }

    // --- Getters y Setters ---

    public boolean isAbonadoEnCaja() {
        return abonadoEnCaja;
    }

    public void setAbonadoEnCaja(boolean abonadoEnCaja) {
        this.abonadoEnCaja = abonadoEnCaja;
    }

    // --- MÉTODOS REQUERIDOS POR EL TPI ---

    /**
     * (A.1) Polimorfismo: Implementación del método abstracto de Pago.
     * (C.1) Persistencia: Genera el string para guardar en un (hipotético) Pagos.csv.
     *
     * Formato CSV Asumido:
     * id;idReserva;fechaPago;TIPO_PAGO;dato_especifico
     */
    @Override
    public String toCSVString() {
        return getIdPago() + ";" +
                getIdReserva() + ";" +
                getFechaPago() + ";" +
                "EFECTIVO;" + // (B.2) Usamos un 'tipo' para el polimorfismo al cargar
                this.abonadoEnCaja;
    }

    /**
     * toString() mejorado para incluir la información del padre.
     */
    @Override
    public String toString() {
        return "PagoEfectivo {" +
                "idPago=" + getIdPago() +
                ", idReserva=" + getIdReserva() +
                ", fechaPago=" + getFechaPago() +
                ", abonadoEnCaja=" + abonadoEnCaja +
                '}';
    }
}