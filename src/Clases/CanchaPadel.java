package Clases;

public class CanchaPadel extends Cancha{
    private boolean tipoPared;


    public CanchaPadel(int idCancha, TipoCancha tipoCancha, String superficie, String nombre, boolean tipoPared) {
        super(idCancha, tipoCancha, superficie, nombre);
        this.tipoPared = tipoPared;
    }

    public boolean isTipoPared() {
        return tipoPared;
    }

    public void setTipoPared(boolean tipoPared) {
        this.tipoPared = tipoPared;
    }

    @Override
    public String toString() {
        return "CanchaPadel{" +
                "tipoPared=" + tipoPared +
                '}';
    }
}