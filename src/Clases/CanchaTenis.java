package Clases;

/**
 * [TPI A.1] Subclase concreta 3.
 */
public class CanchaTenis extends Cancha {
    private boolean esDoble; // Atributo específico

    public CanchaTenis(int idCancha, TipoCancha tipoCancha, String superficie, String nombre, double precioPorHora, boolean esDoble) {
        super(idCancha, tipoCancha, superficie, nombre, precioPorHora);
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

    /**
     * [TPI A.1] Polimorfismo: Guarda los datos comunes + esDoble.
     */
    @Override
    public String toCSVString() {
        return getIdCancha() + ";" +
                getNombre() + ";" +
                getSuperficie() + ";" +
                getPrecioPorHora() + ";" +
                getTipoCancha() + ";" +
                this.esDoble;
    }
}