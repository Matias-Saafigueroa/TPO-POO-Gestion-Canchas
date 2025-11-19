package Clases;

import interfaces.IPersona;

/**
 * [TPI A.1] Herencia: Clase Abstracta Base obligatoria.
 * Define los atributos y comportamientos comunes de todas las personas.
 */
public abstract class Persona implements IPersona {
    private String nombre;
    private String apellido;
    private int dni;
    private int telefono;
    private String email;
    private String contraseña;

    public Persona(String nombre, String apellido, int dni, int telefono, String email, String contraseña) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.telefono = telefono;
        this.email = email;
        this.contraseña = contraseña;
    }

    // ... (Getters y Setters) ...

    @Override
    public String toString() {
        return "Persona{" +
                "nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", dni=" + dni +
                '}';
    }

    /**
     * [TPI A.1] Método Abstracto: Obliga a las subclases a definir su persistencia.
     * [TPI C.1] Base del contrato de persistencia en CSV.
     */
    public abstract String toCSVString();
}