package Clases;

public class CanchaFutbol extends Cancha{
    private int cantidadJugadores;

    public CanchaFutbol(int idCancha, String superficie, TipoCancha tipoCancha, int cantidadJugadores) {
        super(idCancha, superficie, tipoCancha.FUTBOL);
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
