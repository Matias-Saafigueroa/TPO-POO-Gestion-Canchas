package interfaces;

import Clases.Persona;

public interface IGestorRegistro {
    public boolean registarCliente(Persona persona);
    public boolean eliminarCliente(Persona persona);
    public boolean modificarCliente(Persona persona);
    public void buscarCliente(int id);
    public boolean registarAdministrador(Persona persona);
    public boolean eliminarAdministrador(Persona persona);
    public boolean modificarAdministrador(Persona persona);
    public void buscarAdministrador(int id);
    public boolean validarRegistro(Persona persona);
}
