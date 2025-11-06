package Clases;

public class Administrador extends Persona {
    private int IdAdministrador;

    public Administrador(int idAdministrador, String nombre, String apellido, int dni, int telefono, String email, String contraseña) {
        super(nombre, apellido, dni, telefono, email, contraseña);
        IdAdministrador = idAdministrador;
    }

    public int getIdAdministrador() {
        return IdAdministrador;
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
}


