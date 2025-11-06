package Clases;

import interfaces.ICliente;

import java.util.ArrayList;
import java.util.List;

public class Cliente extends Persona implements ICliente {
    private int idCliente;
    private List<Reserva> historialReservasCliente= new ArrayList<>();
    //private static int proximoId = 1;

    // Constructor para crear un cliente NUEVO (ej, desde la UI)
    public Cliente(String nombre, String apellido, int dni, int telefono, String email, String contraseña) {
        super(nombre, apellido, dni, telefono, email, contraseña);
        //this.idCliente = proximoId++; // Asigna un ID
        this.historialReservasCliente = new ArrayList<>(); // Inicializa la lista vacía
    }

    // Constructor para CARGAR desde CSV
    public Cliente(int idCliente, String nombre, String apellido, int dni, int telefono, String email, String contraseña) {
        super(nombre, apellido, dni, telefono, email, contraseña);
        this.idCliente = idCliente;
        this.historialReservasCliente = new ArrayList<>(); // Se inicializa vacía. Se llenará después.
    }

    //constructor



    //setters y getters


    public int getIdCliente() {
        return idCliente;
    }

    @Override
    public String toCSVString() {
        // Formato: idCliente;nombre;apellido;dni;telefono;email;contraseña
        return this.idCliente + ";" +
                getNombre() + ";" +
                getApellido() + ";" +
                getDni() + ";" +
                getTelefono() + ";" +
                getEmail() + ";" +
                getContraseña();
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
