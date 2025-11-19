package Clases;

/**
 * [TPI A.1] Herencia: Subclase concreta de Persona.
 * [SOLID SRP] Modelo de datos puro. Se eliminó la lista de reservas interna
 * para cumplir con SRP; el historial ahora lo gestiona el GestorReserva.
 */
public class Cliente extends Persona {
    private int idCliente;

    // Constructor utilizado por el GestorRegistro al cargar archivos o crear nuevos usuarios.
    public Cliente(int idCliente, String nombre, String apellido, int dni, int telefono, String email, String contraseña) {
        super(nombre, apellido, dni, telefono, email, contraseña);
        this.idCliente = idCliente;
    }

    public int getIdCliente() {
        return idCliente;
    }

    /**
     * [TPI A.1] Polimorfismo: Implementación específica para Cliente.
     */
    @Override
    public String toCSVString() {
        return this.idCliente + ";" +
                getNombre() + ";" +
                getApellido() + ";" +
                getDni() + ";" +
                getTelefono() + ";" +
                getEmail() + ";" +
                getContraseña();
    }
}