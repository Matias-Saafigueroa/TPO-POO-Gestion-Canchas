package Clases;

public class Cancha {
    private int idCancha;
    private TipoCancha tipoCancha;
    private String superficie;

    public Cancha(int idCancha, String superficie, TipoCancha tipoCancha) {
        this.idCancha = idCancha;
        this.superficie = superficie;
        this.tipoCancha = tipoCancha;
    }

    public int getIdCancha() {
        return idCancha;
    }

    public void setIdCancha(int idCancha) {
        this.idCancha = idCancha;
    }

    public String getSuperficie() {
        return superficie;
    }

    public void setSuperficie(String superficie) {
        this.superficie = superficie;
    }

    public TipoCancha getTipoCancha() {
        return tipoCancha;
    }

    public void setTipoCancha(TipoCancha tipoCancha) {
        this.tipoCancha = tipoCancha;
    }

    @Override
    public String toString() {
        return "Cancha{" +
                "idCancha=" + idCancha +
                ", tipoCancha=" + tipoCancha +
                ", superficie='" + superficie + '\'' +
                '}';
    }

}
