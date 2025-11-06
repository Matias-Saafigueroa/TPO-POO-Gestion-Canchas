package Clases; // O donde pongas tus clases

import interfaces.IGestorRegistro;
import interfaces.IGestionArchivos;
import java.io.IOException;
import java.util.ArrayList;



public class GestorRegistro implements IGestorRegistro {

    // --- ATRIBUTOS ---
    public static final int codigoValidacion = 2060;
    private ArrayList<Persona> listaClientes;
    private ArrayList<Persona> listaAdministradores;
    private IGestionArchivos gestorDeArchivos;

    private static final String RUTA_CLIENTES = "clientes.csv";
    private static final String RUTA_ADMINS = "administradores.csv";

    // --- CONSTRUCTOR ---
    public GestorRegistro() {
        // Asumo que tu clase se llama 'GestionArchivos'
        this.gestorDeArchivos = new GestionArchivos();
        this.listaClientes = new ArrayList<>();
        this.listaAdministradores = new ArrayList<>();

        cargarClientes();
        cargarAdmins();
    }

    // --- MÉTODOS DE CLIENTE (Carga/Guardado) ---

    private void cargarClientes() {
        try {
            ArrayList<String> lineas = gestorDeArchivos.leerArchivo(RUTA_CLIENTES);
            for (String linea : lineas) {
                String[] datos = linea.split(";");
                if (datos.length == 7) { // id;nombre;apellido;dni;tel;email;pass
                    int id = Integer.parseInt(datos[0]);
                    String nombre = datos[1];
                    String apellido = datos[2];
                    int dni = Integer.parseInt(datos[3]);
                    int telefono = Integer.parseInt(datos[4]);
                    String email = datos[5];
                    String pass = datos[6];

                    // Usamos el constructor de carga de Cliente
                    Persona cliente = new Cliente(id, nombre, apellido, dni, telefono, email, pass);
                    this.listaClientes.add(cliente);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar clientes.csv: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error de formato en clientes.csv: " + e.getMessage());
        }
    }

    private void guardarClientes() {
        try {
            ArrayList<String> lineas = new ArrayList<>();
            for (Persona cliente : this.listaClientes) {
                // Asumo que Cliente tiene el método toCSVString() que implementamos antes
                lineas.add(((Cliente) cliente).toCSVString());
            }
            gestorDeArchivos.escribirArchivo(RUTA_CLIENTES, lineas);
        } catch (IOException e) {
            System.err.println("Error al guardar clientes.csv: " + e.getMessage());
        }
    }

    // --- MÉTODOS DE ADMIN (Carga/Guardado) ---

    private void cargarAdmins() {
        try {
            ArrayList<String> lineas = gestorDeArchivos.leerArchivo(RUTA_ADMINS);
            for (String linea : lineas) {
                String[] datos = linea.split(";");
                if (datos.length == 7) { // id;nombre;apellido;dni;tel;email;pass
                    int id = Integer.parseInt(datos[0]);
                    String nombre = datos[1];
                    String apellido = datos[2];
                    int dni = Integer.parseInt(datos[3]);
                    int telefono = Integer.parseInt(datos[4]);
                    String email = datos[5];
                    String pass = datos[6];

                    // Usamos el constructor de carga de Admin
                    Persona admin = new Administrador(id, nombre, apellido, dni, telefono, email, pass);
                    this.listaAdministradores.add(admin);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar administradores.csv: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error de formato en administradores.csv: " + e.getMessage());
        }
    }

    private void guardarAdmins() {
        try {
            ArrayList<String> lineas = new ArrayList<>();
            for (Persona admin : this.listaAdministradores) {
                // Asumo que Administrador tiene el método toCSVString()
                lineas.add(((Administrador) admin).toCSVString());
            }
            gestorDeArchivos.escribirArchivo(RUTA_ADMINS, lineas);
        } catch (IOException e) {
            System.err.println("Error al guardar administradores.csv: " + e.getMessage());
        }
    }

    // --- ⭐️ NUEVO MÉTODO DE INICIO DE SESIÓN ⭐️ ---

    /**
     * Valida las credenciales de un usuario (Cliente o Admin) contra las listas en memoria.
     *
     * @param dni El DNI (int) del usuario que intenta ingresar.
     * @param contraseña La contraseña (String) del usuario.
     * @return El objeto Persona (Cliente o Administrador) si las credenciales son correctas.
     * Retorna null si el DNI o la contraseña son incorrectos.
     */
    public Persona iniciarSesion(int dni, String contraseña) {

        // 1. Buscar primero en la lista de Administradores
        for (Persona admin : this.listaAdministradores) {
            // Asumo que Persona tiene getContraseña() como te indiqué
            if (admin.getDni() == dni && admin.getContraseña().equals(contraseña)) {
                return admin; // ¡Éxito! Devuelve el objeto Administrador
            }
        }

        // 2. Si no es admin, buscar en la lista de Clientes
        for (Persona cliente : this.listaClientes) {
            if (cliente.getDni() == dni && cliente.getContraseña().equals(contraseña)) {
                return cliente; // ¡Éxito! Devuelve el objeto Cliente
            }
        }

        // 3. Si no se encontró en ninguna lista, las credenciales son incorrectas
        return null;
    }


    // --- IMPLEMENTACIÓN DE MÉTODOS DE LA INTERFAZ IGestorRegistro ---

    @Override
    public boolean registarCliente(Persona persona) {
        if (validarRegistro(persona)) {
            this.listaClientes.add(persona);
            guardarClientes();
            return true; // Éxito
        }
        return false; // Falló (ya existe)
    }

    @Override
    public boolean eliminarCliente(Persona persona) {
        boolean eliminado = this.listaClientes.removeIf(c -> c.getDni() == persona.getDni());
        if (eliminado) {
            guardarClientes();
        }
        return eliminado;
    }

    @Override
    public boolean modificarCliente(Persona personaModificada) {
        for (int i = 0; i < listaClientes.size(); i++) {
            // Buscamos por DNI, que es el identificador único
            if (listaClientes.get(i).getDni() == personaModificada.getDni()) {
                listaClientes.set(i, personaModificada); // Reemplaza el objeto
                guardarClientes();
                return true;
            }
        }
        return false; // No se encontró
    }

    @Override
    public void buscarCliente(int dni) {
        // La interfaz pide 'void', así que solo imprimimos
        for (Persona p : listaClientes) {
            if (p.getDni() == dni) {
                System.out.println("Cliente encontrado: " + p.toString());
                return;
            }
        }
        System.out.println("Cliente con DNI " + dni + " no encontrado.");
    }

    // --- Métodos de Administrador ---

    @Override
    public boolean registarAdministrador(Persona persona) {
        if (validarRegistro(persona)) {
            this.listaAdministradores.add(persona);
            guardarAdmins();
            return true;
        }
        return false;
    }

    @Override
    public boolean eliminarAdministrador(Persona persona) {
        boolean eliminado = this.listaAdministradores.removeIf(a -> a.getDni() == persona.getDni());
        if (eliminado) {
            guardarAdmins();
        }
        return eliminado;
    }

    @Override
    public boolean modificarAdministrador(Persona personaModificada) {
        for (int i = 0; i < listaAdministradores.size(); i++) {
            if (listaAdministradores.get(i).getDni() == personaModificada.getDni()) {
                listaAdministradores.set(i, personaModificada);
                guardarAdmins();
                return true;
            }
        }
        return false;
    }

    @Override
    public void buscarAdministrador(int dni) {
        for (Persona p : listaAdministradores) {
            if (p.getDni() == dni) {
                System.out.println("Admin encontrado: " + p.toString());
                return;
            }
        }
        System.out.println("Admin con DNI " + dni + " no encontrado.");
    }

    @Override
    public boolean validarRegistro(Persona persona) {
        // Valida que el DNI no exista en NINGUNA lista
        boolean existeEnClientes = listaClientes.stream()
                .anyMatch(c -> c.getDni() == persona.getDni());

        boolean existeEnAdmins = listaAdministradores.stream()
                .anyMatch(a -> a.getDni() == persona.getDni());

        // Es válido (true) si NO existe en ninguna lista
        return !existeEnClientes && !existeEnAdmins;
    }
}