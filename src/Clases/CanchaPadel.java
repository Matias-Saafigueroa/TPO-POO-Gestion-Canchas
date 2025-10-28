package Clases;

public class CanchaPadel extends Cancha{
    private boolean esTechada;

    public CanchaPadel(int idCancha, String superficie, TipoCancha tipoCancha, boolean esTechada) {
        super(idCancha, superficie, tipoCancha.PADEL);
        this.esTechada = esTechada;
    }

    public boolean isEsTechada() {
        return esTechada;
    }

    public void setEsTechada(boolean esTechada) {
        this.esTechada = esTechada;
    }

    @Override
    public String toString() {
        return "Cancha de Padel{" +
                "ID=" + super.getIdCancha() +
                "Es techada? " + esTechada +
                '}';
    }
}