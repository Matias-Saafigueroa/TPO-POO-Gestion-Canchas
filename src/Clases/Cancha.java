package Clases;

/**
 * [TPI A.1] Herencia: Clase Abstracta Base obligatoria.
 * Define los atributos comunes y el contrato para todas las canchas.
 */
public abstract class Cancha {
    private int idCancha;
    private TipoCancha tipoCancha; // [TPI A.3] Agregación: La cancha "tiene" un tipo.
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

    // --- Getters y Setters ---
    public int getIdCancha() { return idCancha; }
    public void setIdCancha(int idCancha) { this.idCancha = idCancha; }
    public TipoCancha getTipoCancha() { return tipoCancha; }
    public void setTipoCancha(TipoCancha tipoCancha) { this.tipoCancha = tipoCancha; }
    public String getSuperficie() { return superficie; }
    public void setSuperficie(String superficie) { this.superficie = superficie; }
    public String getNombre() { return Nombre; }
    public void setNombre(String nombre) { Nombre = nombre; }
    public double getPrecioPorHora() { return precioPorHora; }
    public void setPrecioPorHora(double precioPorHora) { this.precioPorHora = precioPorHora; }

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

    /**
     * [TPI A.1] Método Abstracto: Obliga a las subclases a definir su propia
     * forma de guardarse, garantizando el Polimorfismo en la persistencia.
     */
    public abstract String toCSVString();
}