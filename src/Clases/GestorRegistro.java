package Clases;

import Excepciones.ClienteYaExisteException;
import java.io.IOException;
import interfaces.IGestorRegistro;
import interfaces.IGestionArchivos;
import java.util.ArrayList;

/**
 * [TPI A.4] GRASP EXPERT: Experto en gestión de usuarios (Clientes y Admins).
 * [SOLID SRP] Única responsabilidad: manejar el ciclo de vida de los usuarios.
 */
public class GestorRegistro implements IGestorRegistro {

    public static final int codigoValidacion = 2060; // [TPI B.2] Constante estática de utilidad.
    private ArrayList<Persona> listaClientes;
    private ArrayList<Persona> listaAdministradores;

    // [SOLID DIP] Dependemos de la interfaz IGestionArchivos.
    private IGestionArchivos gestorDeArchivos;

    private static final String RUTA_CLIENTES = "src/archivos/historialClientes.csv";
    private static final String RUTA_ADMINS = "src/archivos/administradores.csv";

    public GestorRegistro() {
        this.gestorDeArchivos = new GestionArchivosCSV();
        this.listaClientes = new ArrayList<>();
        this.listaAdministradores = new ArrayList<>();

        try {
            cargarClientes();
            cargarAdmins();
        } catch (IOException e) {
            System.err.println("ERROR CRÍTICO (IO): No se pudieron cargar los usuarios: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("ERROR CRÍTICO (Formato): Archivos corruptos.");
        }
    }

    // ... (métodos cargarClientes, guardarClientes, cargarAdmins, guardarAdmins omitidos por brevedad) ...
    // ... (Siguen la misma lógica de persistencia y manejo de excepciones que GestorCancha) ...

    @Override
    public void registarCliente(Persona persona) throws ClienteYaExisteException, IOException {
        // [TPI B.3] Validación de Regla de Negocio: DNI Único.
        if (listaClientes.stream().anyMatch(c -> c.getDni() == persona.getDni())) {
            throw new ClienteYaExisteException("El DNI " + persona.getDni() + " ya está registrado como cliente.");
        }
        this.listaClientes.add(persona);
        guardarClientes();
    }

    // ... (eliminarCliente, modificarCliente omitidos) ...

    @Override
    public void registarAdministrador(Persona persona) throws ClienteYaExisteException, IOException {
        if (listaAdministradores.stream().anyMatch(a -> a.getDni() == persona.getDni())) {
            throw new ClienteYaExisteException("El DNI " + persona.getDni() + " ya está registrado como administrador.");
        }
        this.listaAdministradores.add(persona);
        guardarAdmins();
    }

    // ... (eliminarAdministrador, modificarAdministrador omitidos) ...

    @Override
    public Persona iniciarSesion(int dni, String contraseña) {
        // [TPI A.1] Polimorfismo: El método retorna 'Persona', pero puede ser un Cliente o un Admin.
        for (Persona admin : this.listaAdministradores) {
            if (admin.getDni() == dni && admin.getContraseña().equals(contraseña)) {
                return admin;
            }
        }
        for (Persona cliente : this.listaClientes) {
            if (cliente.getDni() == dni && cliente.getContraseña().equals(contraseña)) {
                return cliente;
            }
        }
        return null;
    }

    @Override
    public Persona buscarCliente(int dni) {
        return listaClientes.stream()
                .filter(c -> c.getDni() == dni)
                .findFirst()
                .orElse(null);
    }

    /**
     * [GRASP Expert] Método necesario para que otros gestores (Reserva) recuperen objetos.
     */
    @Override
    public Persona buscarClientePorId(int idCliente) {
        return listaClientes.stream()
                .filter(c -> c instanceof Cliente && ((Cliente) c).getIdCliente() == idCliente)
                .findFirst()
                .orElse(null);
    }

    // ... (validarRegistro, asignarIdCliente, asignarIdAdmin omitidos) ...
}