package Clases;

import interfaces.IGestorReserva;

public class GestorReserva implements IGestorReserva {

    @Override
    public boolean crearReserva(Reserva reserva) {
        return false;
    }

    @Override
    public boolean confirmarReserva(int id) {
        return false;
    }

    @Override
    public boolean cancelarReserva(int id) {
        return false;
    }

    @Override
    public boolean validarReserva(int id) {
        return false;
    }

    @Override
    public Reserva mostrarReserva(int id) {
        return null;
    }

    @Override
    public void historialReservas() {

    }
}
