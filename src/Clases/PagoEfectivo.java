package Clases;

public class PagoEfectivo {
    private boolean abonadoEnCaja;

    public PagoEfectivo(boolean abonadoEnCaja) {
        this.abonadoEnCaja = abonadoEnCaja;
    }

    public boolean isAbonadoEnCaja() {
        return abonadoEnCaja;
    }

    public void setAbonadoEnCaja(boolean abonadoEnCaja) {
        this.abonadoEnCaja = abonadoEnCaja;
    }

    @Override
    public String toString() {
        return "PagoEfectivo{" +
                "abonadoEnCaja=" + abonadoEnCaja +
                '}';
    }
}
