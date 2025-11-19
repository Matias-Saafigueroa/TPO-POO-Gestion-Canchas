package interfaces;

import Clases.Persona;
import Excepciones.ClienteYaExisteException;
import java.io.IOException;

/**
 * [TPI A.2] Contrato para la lógica de usuarios.
 * [PATRÓN] Indirección: Permite que la GUI hable con una abstracción en lugar de la clase concreta.
 */
public interface IGestorRegistro {

    // --- Métodos de Acción (Modifican datos) ---

    // [INFO] 'throws' avisa que este método es peligroso y puede fallar
    // por una regla de negocio (ClienteYaExiste) o por el disco (IOException).
    void registarCliente(Persona persona) throws ClienteYaExisteException, IOException;
    void eliminarCliente(int dni) throws IOException;
    void modificarCliente(Persona persona) throws IOException;

    void registarAdministrador(Persona persona) throws ClienteYaExisteException, IOException;
    void eliminarAdministrador(int dni) throws IOException;
    void modificarAdministrador(Persona persona) throws IOException;

    // --- Métodos de Consulta (Lectura) ---

    Persona iniciarSesion(int dni, String contraseña);
    Persona buscarCliente(int dni);

    // [INFO] Necesario para que el GestorReserva pueda buscar clientes
    // usando el ID que leyó del archivo de reservas.
    Persona buscarClientePorId(int idCliente);

    boolean validarRegistro(Persona persona);

    // --- Métodos Auxiliares ---

    int asignarIdCliente();
    int asignarIdAdmin();
}