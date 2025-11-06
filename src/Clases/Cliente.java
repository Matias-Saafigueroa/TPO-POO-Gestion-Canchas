package Clases;

// (Ya no necesita 'java.util.List' ni 'ArrayList')

/**
 * (A.1) Requisito TPI: Subclase de Persona.
 * (A.4) SRP: Su única responsabilidad es modelar los datos de un Cliente.
 */
public class Cliente extends Persona {

    // Atributo específico
    private int idCliente;

    // (Este atributo 'historialReservasCliente' FUE ELIMINADO
    // para cumplir con SRP y la lógica de Gestores)

    /**
     * Constructor para crear un cliente NUEVO (usado por GestorRegistro).
     * El Gestor es responsable de generar el ID.
     */
    public Cliente(int idCliente, String nombre, String apellido, int dni, int telefono, String email, String contraseña) {
        super(nombre, apellido, dni, telefono, email, contraseña);
        this.idCliente = idCliente;
        // (La lista de reservas ya no se inicializa aquí)
    }

    /* * NOTA: Este constructor de abajo es el que usa el 'GestorRegistro'
     * para cargar desde el CSV. Si tu constructor de "nuevo cliente"
     * (el de arriba) ya recibe el ID, este se vuelve redundante.
     * * Puedes borrar este si usas el de arriba para todo:
     * * // Constructor para CARGAR desde CSV (REDUNDANTE SI EL DE ARRIBA YA PIDE ID)
     * public Cliente(int idCliente, String nombre, String apellido, int dni, int telefono, String email, String contraseña) {
     * super(nombre, apellido, dni, telefono, email, contraseña);
     * this.idCliente = idCliente;
     * }
     */


    // --- Getters y Setters ---

    public int getIdCliente() {
        return idCliente;
    }

    // --- MÉTODOS REQUERIDOS POR EL TPI ---

    /**
     * (A.1) Polimorfismo: Sobrescribe el método abstracto de Persona.
     * (C.1) Persistencia: Genera el string para guardar en historialClientes.csv.
     * Formato: idCliente;nombre;apellido;dni;telefono;email;contraseña
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