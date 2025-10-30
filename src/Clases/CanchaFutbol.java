package Clases;

public class CanchaFutbol extends Cancha{
    private int cantidadJugadores;

    public CanchaFutbol(int idCancha, TipoCancha tipoCancha, String superficie, String nombre, int cantidadJugadores) {
        super(idCancha, tipoCancha, superficie, nombre);
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
        return "CanchaFutbol{" +
                "ID=" + super.getIdCancha() +
                "cantidadJugadores=" + cantidadJugadores +
                '}';
    }
}
