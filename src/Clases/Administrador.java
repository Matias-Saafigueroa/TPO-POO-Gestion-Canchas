package Clases;

import interfaces.IAdministrador;

public class Administrador extends Persona implements IAdministrador {
    private int IdAdministrador;

    public Administrador(int idAdministrador, String nombre, String apellido, int dni, int telefono, String email, String contraseña) {
        super(nombre, apellido, dni, telefono, email, contraseña);
        IdAdministrador = idAdministrador;
    }

    public int getIdAdministrador() {
        return IdAdministrador;
    }

    public void setIdAdministrador(int idAdministrador) {
        IdAdministrador = idAdministrador;
    }

    @Override
    public String toCSVString() {
        // Formato: idAdmin;nombre;apellido;dni;telefono;email;contraseña
        return this.IdAdministrador + ";" +
                getNombre() + ";" +
                getApellido() + ";" +
                getDni() + ";" +
                getTelefono() + ";" +
                getEmail() + ";" +
                getContraseña();
    }

    @Override
    public boolean registrarse(Persona persona) {
        return false;
    }

    @Override
    public void modificarDatos(String opcion) {

    }

    @Override
    public void crearCancha(Cancha cancha) {

    }

    @Override
    public void eliminarCancha(Cancha cancha) {

    }

    @Override
    public void asignarPrecio(Cancha cancha) {

    }
}
