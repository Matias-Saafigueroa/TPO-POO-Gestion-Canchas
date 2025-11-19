package Clases;

/**
 * [TPI A.1] Subclase concreta 2.
 */
public class CanchaPadel extends Cancha {

    private String tipoPared; // Atributo específico

    public CanchaPadel(int idCancha, TipoCancha tipoCancha, String superficie, String nombre, double precioPorHora, String tipoPared) {
        super(idCancha, tipoCancha, superficie, nombre, precioPorHora);
        this.tipoPared = tipoPared;
    }

    public String getTipoPared() {
        return tipoPared;
    }

    public void setTipoPared(String tipoPared) {
        this.tipoPared = tipoPared;
    }

    /**
     * [TPI A.1] Polimorfismo: Guarda los datos comunes + tipoPared.
     */
    @Override
    public String toCSVString() {
        return getIdCancha() + ";" +
                getNombre() + ";" +
                getSuperficie() + ";" +
                getPrecioPorHora() + ";" +
                getTipoCancha() + ";" + // (PADEL)
                this.tipoPared;
    }

    @Override
    public String toString() {
        return super.toString() +
                " (Pared: " + this.tipoPared + ")";
    }
}