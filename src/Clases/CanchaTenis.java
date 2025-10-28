package Clases;

public class CanchaTenis extends Cancha {
    private boolean esDoble;

    public CanchaTenis(int idCancha, String superficie, TipoCancha tipoCancha, boolean esDoble) {
        super(idCancha, superficie, tipoCancha.TENIS);
        this.esDoble = esDoble;
    }

    public boolean isEsDoble() {
        return esDoble;
    }

    public void setEsDoble(boolean esDoble) {
        this.esDoble = esDoble;
    }

    @Override
    public String toString() {
        return "CanchaTenis{" +
                "ID=" + super.getIdCancha() +
                "Es doble? " + esDoble +
                '}';
    }
}
