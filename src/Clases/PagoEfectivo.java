package Clases;

import java.time.LocalDate;

public class PagoEfectivo extends Pago {
    private boolean abonadoEnCaja;

    public PagoEfectivo(int idPago, int idReserva, LocalDate fechaPago, boolean abonadoEnCaja) {
        super(idPago, idReserva, fechaPago);
        this.abonadoEnCaja = abonadoEnCaja;
    }

    // ... (Getters y Setters) ...

    /**
     * [TPI A.1] Polimorfismo: Implementación específica.
     * Guarda el tipo "EFECTIVO" para diferenciarlo al leer.
     */
    @Override
    public String toCSVString() {
        return getIdPago() + ";" +
                getIdReserva() + ";" +
                getFechaPago() + ";" +
                "EFECTIVO;" +
                this.abonadoEnCaja;
    }

    @Override
    public String toString() {
        return "PagoEfectivo {" + super.toString() + ", abonadoEnCaja=" + abonadoEnCaja + '}';
    }
}