package Clases;

public class CanchaPadel extends Cancha{
    private String tipoPared;


    public CanchaPadel(int idCancha, TipoCancha tipoCancha, String superficie, String nombre, double precioPorHora, String tipoPared) {
        super(idCancha, tipoCancha, superficie, nombre, precioPorHora);
        this.tipoPared = tipoPared;
    }

    public String getTipoPared() {
        return tipoPared;
    }

    @Override
    public String toString() {
        return "CanchaPadel{" +
                "tipoPared=" + tipoPared +
                '}';
    }

    @Override
    public String toCSVString() {
        return getIdCancha() + ";" +
                getNombre() + ";" +
                getSuperficie() + ";" +
                getPrecioPorHora() + ";" +
                getTipoCancha() + ";" + // (PADEL)
                this.tipoPared;
    }
}