package Clases;

public abstract class Cancha {
    private int idCancha;
    private TipoCancha tipoCancha;
    private String superficie;
    private String Nombre;
    private double precioPorHora;

    public Cancha(int idCancha, TipoCancha tipoCancha, String superficie, String nombre, double precioPorHora) {
        this.idCancha = idCancha;
        this.tipoCancha = tipoCancha;
        this.superficie = superficie;
        Nombre = nombre;
        this.precioPorHora = precioPorHora;
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

    public double getPrecioPorHora() {
        return precioPorHora;
    }

    public void setPrecioPorHora(double precioPorHora) {
        this.precioPorHora = precioPorHora;
    }

    @Override
    public String toString() {
        return "Cancha{" +
                "idCancha=" + idCancha +
                ", tipoCancha=" + tipoCancha +
                ", superficie='" + superficie + '\'' +
                ", Nombre='" + Nombre + '\'' +
                ", precioPorHora=" + precioPorHora +
                '}';
    }
    public abstract String toCSVString();
}


