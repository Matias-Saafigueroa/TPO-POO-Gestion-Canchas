package Clases;

import interfaces.ICliente;

import java.util.ArrayList;

public class Cliente extends Persona implements ICliente {
    private int idCliente;
    private List<Reserva> historialReservasCliente= new ArrayList<>();//

    //constructor

    //setters y getters


    @Override
    public String toString() {
        return "Cliente{" +
                "idCliente=" + idCliente +
                ", historialReservas=" + historialReservas +
                '}';
    }


    //metodos
    @Override
    public boolean registrarse(Persona persona) {
        return false;//implementar
    }

    @Override
    public void modificarDatos(String opcion) {
    //implementar
    }

    @Override
    public boolean realizarReserva(Reserva reserva) {
        return false;//implementar
    }

    @Override
    public void cancelarReserva(Reserva reserva) {
    //implementar
    }

    @Override
    public void realizarPago() {
    //implementar
    }

    @Override
    public void cancelarPago() {
        //implementar

    }

    @Override
    public List<Reserva> historialReserva(int id) {
        return null;//implementar
    }





}
