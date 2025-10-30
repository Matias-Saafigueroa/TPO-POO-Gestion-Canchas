package interfaces;

import Clases.Reserva;

public interface IGestorReserva {
    public boolean crearReserva(Reserva reserva);
    public boolean confirmarReserva(int id, boolean pagoRealizado);
    public boolean cancelarReserva(int id);
    public boolean validarReserva(int id);
    public Reserva mostrarReserva(int id);//revisar esto
    public static void historialReservas();//revisar esto

}
