package Clases;

// (B.3) Importamos tus nuevas excepciones
import Excepciones.ClienteYaExisteException;

// (B.4) Importamos IOException para el manejo de archivos
import java.io.IOException;

import interfaces.IGestorRegistro;
import interfaces.IGestionArchivos;
import java.util.ArrayList;

/**
 * (A.4) EXPERTO (GRASP): Esta clase es la experta en la lógica de negocio de usuarios.
 * (C.2) SEPARACIÓN DE PERSISTENCIA: Delega el guardado a un gestorDeArchivos.
 * (B.3) IMPLEMENTACIÓN: Implementa los métodos de la interfaz con Excepciones.
 */
public class GestorRegistro implements IGestorRegistro {

    public static final int codigoValidacion = 2060;
    private ArrayList<Persona> listaClientes;
    private ArrayList<Persona> listaAdministradores;

    // (A.4 / C.3) ABSTRACCIÓN: Depende de la interfaz (DIP)
    private IGestionArchivos gestorDeArchivos;

    private static final String RUTA_CLIENTES = "historialClientes.csv";
    private static final String RUTA_ADMINS = "administradores.csv";

    // --- CONSTRUCTOR ---
    public GestorRegistro() {
        this.gestorDeArchivos = new GestionArchivosCSV();
        this.listaClientes = new ArrayList<>();
        this.listaAdministradores = new ArrayList<>();

        try {
            // (B.4) Cargamos los datos al iniciar
            cargarClientes();
            cargarAdmins();
        } catch (IOException e) {
            System.err.println("ERROR CRÍTICO (IO): No se pudieron cargar los usuarios al iniciar: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("ERROR CRÍTICO (Formato): Los archivos CSV están corruptos: " + e.getMessage());
        }
    }

    // --- MÉTODOS DE CARGA/GUARDADO (Refactorizados para lanzar errores) ---

    private void cargarClientes() throws IOException, NumberFormatException {
        ArrayList<String> lineas = gestorDeArchivos.leerArchivo(RUTA_CLIENTES);
        for (String linea : lineas) {
            String[] datos = linea.split(";");
            if (datos.length == 7) {
                int id = Integer.parseInt(datos[0]);
                String nombre = datos[1];
                String apellido = datos[2];
                int dni = Integer.parseInt(datos[3]);
                int telefono = Integer.parseInt(datos[4]);
                String email = datos[5];
                String pass = datos[6];
                Persona cliente = new Cliente(id, nombre, apellido, dni, telefono, email, pass);
                this.listaClientes.add(cliente);
            }
        }
    }

    /**
     * (C.2) Este método ahora LANZA la excepción (throws)
     * en lugar de atraparla (catch).
     */
    private void guardarClientes() throws IOException {
        ArrayList<String> lineas = new ArrayList<>();
        for (Persona cliente : this.listaClientes) {
            lineas.add(((Cliente) cliente).toCSVString());
        }
        // (B.4) Ya no hay try-catch. Si escribirArchivo falla,
        // este método fallará y le avisará al SistemaCanchas.
        gestorDeArchivos.escribirArchivo(RUTA_CLIENTES, lineas);
    }

    private void cargarAdmins() throws IOException, NumberFormatException {
        ArrayList<String> lineas = gestorDeArchivos.leerArchivo(RUTA_ADMINS);
        for (String linea : lineas) {
            String[] datos = linea.split(";");
            if (datos.length == 7) {
                int id = Integer.parseInt(datos[0]);
                String nombre = datos[1];
                String apellido = datos[2];
                int dni = Integer.parseInt(datos[3]);
                int telefono = Integer.parseInt(datos[4]);
                String email = datos[5];
                String pass = datos[6];
                Persona admin = new Administrador(id, nombre, apellido, dni, telefono, email, pass);
                this.listaAdministradores.add(admin);
            }
        }
    }

    /**
     * (C.2) Este método ahora LANZA la excepción (throws).
     */
    private void guardarAdmins() throws IOException {
        ArrayList<String> lineas = new ArrayList<>();
        for (Persona admin : this.listaAdministradores) {
            lineas.add(((Administrador) admin).toCSVString());
        }
        gestorDeArchivos.escribirArchivo(RUTA_ADMINS, lineas);
    }

    // --- MÉTODOS DE LA INTERFAZ (Implementados con la nueva lógica) ---

    @Override
    public void registarCliente(Persona persona) throws ClienteYaExisteException, IOException {
        // (SOLUCIÓN 1) Validación separada: solo chequea clientes
        if (listaClientes.stream().anyMatch(c -> c.getDni() == persona.getDni())) {
            throw new ClienteYaExisteException("El DNI " + persona.getDni() + " ya está registrado como cliente.");
        }
        this.listaClientes.add(persona);
        guardarClientes(); // (SOLUCIÓN 2) Esto ahora lanza IOException
    }

    @Override
    public void eliminarCliente(int dni) throws IOException {
        boolean eliminado = this.listaClientes.removeIf(c -> c.getDni() == dni);
        if (eliminado) {
            guardarClientes();
        }
        // (Podrías lanzar una excepción si 'eliminado' es false)
    }

    @Override
    public void modificarCliente(Persona personaModificada) throws IOException {
        for (int i = 0; i < listaClientes.size(); i++) {
            if (listaClientes.get(i).getDni() == personaModificada.getDni()) {
                listaClientes.set(i, personaModificada);
                guardarClientes();
                return;
            }
        }
    }

    @Override
    public void registarAdministrador(Persona persona) throws ClienteYaExisteException, IOException {
        // (SOLUCIÓN 1) Validación separada: solo chequea admins
        if (listaAdministradores.stream().anyMatch(a -> a.getDni() == persona.getDni())) {
            throw new ClienteYaExisteException("El DNI " + persona.getDni() + " ya está registrado como administrador.");
        }
        this.listaAdministradores.add(persona);
        guardarAdmins(); // (SOLUCIÓN 2) Esto ahora lanza IOException
    }

    @Override
    public void eliminarAdministrador(int dni) throws IOException {
        boolean eliminado = this.listaAdministradores.removeIf(a -> a.getDni() == dni);
        if (eliminado) {
            guardarAdmins();
        }
    }

    @Override
    public void modificarAdministrador(Persona personaModificada) throws IOException {
        for (int i = 0; i < listaAdministradores.size(); i++) {
            if (listaAdministradores.get(i).getDni() == personaModificada.getDni()) {
                listaAdministradores.set(i, personaModificada);
                guardarAdmins();
                return;
            }
        }
    }

    // --- MÉTODOS DE CONSULTA ---

    @Override
    public Persona iniciarSesion(int dni, String contraseña) {
        // Esta lógica sigue funcionando bien
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
     * Este método ya no es necesario en la interfaz si la validación
     * se hace dentro de los métodos de registro.
     * Lo mantenemos por si la interfaz lo requiere.
     */
    @Override
    public boolean validarRegistro(Persona persona) {
        boolean existeEnClientes = listaClientes.stream()
                .anyMatch(c -> c.getDni() == persona.getDni());
        boolean existeEnAdmins = listaAdministradores.stream()
                .anyMatch(a -> a.getDni() == persona.getDni());
        return !existeEnClientes && !existeEnAdmins; // Lógica global
    }

    /**
     * (Requisito TPI) Asigna un ID único para un nuevo cliente.
     */
    public int asignarIdCliente() {
        if (listaClientes.isEmpty()) {
            return 1; // Si no hay clientes, empieza en 1
        }
        // Devuelve el ID más alto en la lista actual + 1
        return listaClientes.stream()
                .mapToInt(p -> ((Cliente) p).getIdCliente())
                .max()
                .orElse(0) + 1;
    }
}