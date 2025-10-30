package Clases;

public class Cancha {
    private int idCancha;
    private TipoCancha tipoCancha;
    private String superficie;
    private String Nombre;

    public Cancha(int idCancha, TipoCancha tipoCancha, String superficie, String nombre) {
        this.idCancha = idCancha;
        this.tipoCancha = tipoCancha;
        this.superficie = superficie;
        Nombre = nombre;
    }

    public int getIdCancha() {
        return idCancha;
    }

    public void setIdCancha(int idCancha) {
        this.idCancha = idCancha;
    }

    public TipoCancha getTipoCancha() {
        return tipoCancha;
    }

    public void setTipoCancha(TipoCancha tipoCancha) {
        this.tipoCancha = tipoCancha;
    }

    public String getSuperficie() {
        return superficie;
    }

    public void setSuperficie(String superficie) {
        this.superficie = superficie;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
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
