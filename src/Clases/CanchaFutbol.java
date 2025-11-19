package Clases;

/**
 * [TPI A.1] Subclase concreta 1.
 */
public class CanchaFutbol extends Cancha {

    private int cantidadJugadores; // Atributo específico

    public CanchaFutbol(int idCancha, TipoCancha tipoCancha, String superficie, String nombre, double precioPorHora, int cantidadJugadores) {
        super(idCancha, tipoCancha, superficie, nombre, precioPorHora);
        this.cantidadJugadores = cantidadJugadores;
    }

    public int getCantidadJugadores() {
        return cantidadJugadores;
    }

    public void setCantidadJugadores(int cantidadJugadores) {
        this.cantidadJugadores = cantidadJugadores;
    }

    /**
     * [TPI A.1] Polimorfismo: Guarda los datos comunes + cantidadJugadores.
     */
    @Override
    public String toCSVString() {
        return getIdCancha() + ";" +
                getNombre() + ";" +
                getSuperficie() + ";" +
                getPrecioPorHora() + ";" +
                getTipoCancha() + ";" +
                this.cantidadJugadores;
    }

    @Override
    public String toString() {
        return super.toString() +
                " (Jugadores: " + this.cantidadJugadores + ")";
    }
}