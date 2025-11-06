package interfaces;

import Clases.Persona;
import Excepciones.ClienteYaExisteException; // Importar
import java.io.IOException; // Importar

public interface IGestorRegistro {

    // (B.3) Métodos refactorizados: devuelven void, lanzan excepciones
    void registarCliente(Persona persona) throws ClienteYaExisteException, IOException;
    void eliminarCliente(int dni) throws IOException; // (Cambiado a DNI)
    void modificarCliente(Persona persona) throws IOException;
    int asignarIdCliente();
    void registarAdministrador(Persona persona) throws ClienteYaExisteException, IOException;
    void eliminarAdministrador(int dni) throws IOException; // (Cambiado a DNI)
    void modificarAdministrador(Persona persona) throws IOException;

    // Métodos de consulta (quedan igual)
    Persona iniciarSesion(int dni, String contraseña);
    Persona buscarCliente(int dni); // (Cambiado de void a Persona)
    boolean validarRegistro(Persona persona);
}