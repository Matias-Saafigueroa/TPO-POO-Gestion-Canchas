package Clases;

import interfaces.IGestorRegistro;

public class GestorRegistro implements IGestorRegistro {


    @Override
    public static boolean registarCliente(Persona persona) {
        return false;
    }

    @Override
    public boolean eliminarCliente(Persona persona) {
        return false;
    }

    @Override
    public boolean modificarCliente(Persona persona) {
        return false;
    }

    @Override
    public void buscarCliente(int id) {

    }

    @Override
    public boolean registarAdministrador(Persona persona) {
        return false;
    }

    @Override
    public boolean eliminarAdministrador(Persona persona) {
        return false;
    }

    @Override
    public boolean modificarAdministrador(Persona persona) {
        return false;
    }

    @Override
    public void buscarAdministrador(int id) {

    }

    @Override
    public boolean validarRegistro(Persona persona) {
        return false;
    }
}
