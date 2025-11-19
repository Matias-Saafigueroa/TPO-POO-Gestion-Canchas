package Clases;

/**
 * [TPI A.1] Herencia: Subclase concreta que extiende de Persona.
 * [SOLID SRP] Principio de Responsabilidad Única: Esta clase es un "Modelo Anémico",
 * su única responsabilidad es almacenar los datos. Toda la lógica de negocio (crear canchas, etc.)
 * se ha movido a los Gestores (GRASP Expert) para desacoplar el código.
 */
public class Administrador extends Persona {
    private int IdAdministrador;

    // [TPI C.1] Constructor necesario para la carga de datos desde persistencia.
    public Administrador(int idAdministrador, String nombre, String apellido, int dni, int telefono, String email, String contraseña) {
        super(nombre, apellido, dni, telefono, email, contraseña);
        IdAdministrador = idAdministrador;
    }

    public int getIdAdministrador() {
        return IdAdministrador;
    }

    /**
     * [TPI A.1] Polimorfismo: Sobrescritura (@Override) del método abstracto.
     * [TPI C.1] Persistencia: Define el formato específico CSV para administradores.
     */
    @Override
    public String toCSVString() {
        return this.IdAdministrador + ";" +
                getNombre() + ";" +
                getApellido() + ";" +
                getDni() + ";" +
                getTelefono() + ";" +
                getEmail() + ";" +
                getContraseña();
    }
}