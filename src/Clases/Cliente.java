package Clases;

import interfaces.ICliente;

import java.util.ArrayList;
import java.util.List;

public class Cliente extends Persona implements ICliente {
    private int idCliente;
    private List<Reserva> historialReservasCliente= new ArrayList<>();//

    public Cliente(String nombre, String apellido, int dni, int telefono, String email, String contraseña, int idCliente, List<Reserva> historialReservasCliente) {
        super(nombre, apellido, dni, telefono, email, contraseña);
        this.idCliente = idCliente;
        this.historialReservasCliente = historialReservasCliente;
    }

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
    public List<Reserva> historialReservaCliente(int id) {
        return List.of();
    }

    @Override
    public List<Reserva> historialReserva(int id) {
        return null;//implementar
    }





}
