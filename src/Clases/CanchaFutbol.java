package Clases;

public class CanchaFutbol extends Cancha{
    private int cantidadJugadores;

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

    @Override
    public String toString() {
        return getIdCancha() + ";" +
                getNombre() + ";" +
                getSuperficie() + ";" +
                getPrecioPorHora() + ";" +
                getTipoCancha() + ";" + // (FUTBOL)
                this.cantidadJugadores;
    }

    @Override
    public String toCSVString() {
        return "";
    }
}
