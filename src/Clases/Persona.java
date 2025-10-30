package Clases;

import interfaces.IPersona;

public abstract class Persona implements IPersona {
    private String nombre;
    private String apellido;
    private int dni;
    private int telefono;
    private String email;
    private String contraseña;

    //constructor

    public Persona(String nombre, String apellido, int dni, int telefono, String email, String contraseña) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.telefono = telefono;
        this.email = email;
        this.contraseña = contraseña;
    }


    //setters y getters


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Persona{" +
                "nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", dni=" + dni +
                ", telefono=" + telefono +
                ", email='" + email + '\'' +
                '}';
    }

    //metodos

    public abstract boolean registrarse(Persona persona);
    public abstract void modificarDatos(String opcion);
}
