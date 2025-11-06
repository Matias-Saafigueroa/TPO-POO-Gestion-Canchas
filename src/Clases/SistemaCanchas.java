package Clases;

// Importamos todas las interfaces y excepciones
import interfaces.*;
import Excepciones.*;

// Imports de Java necesarios
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator; // (B.1) Requisito TPI: Para Ordenamiento
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * (A.4) CONTROLADOR (GRASP): Clase principal del sistema.
 * Maneja todos los menús (Vistas) y coordina las llamadas a los Gestores (Modelo/Servicio).
 * (B.4) GESTIÓN DE ERRORES: Usa try-catch para gestionar excepciones de negocio.
 */
public class SistemaCanchas {

    // --- 1. ATRIBUTOS DE INSTANCIA (NO ESTÁTICOS) ---
    private final Scanner sc;
    private final IGestorCancha gestorCancha;
    private final IGestorRegistro gestorRegistro;
    private final IGestorReserva gestorReserva;

    // --- 2. CONSTRUCTOR ---
    public SistemaCanchas() {
        this.sc = new Scanner(System.in);

        // (A.4 / C.3) Inyección de Dependencias (Constructor)
        this.gestorRegistro = new GestorRegistro();
        this.gestorCancha = new GestorCancha();
        // GestorReserva "recibe" a los otros gestores para poder "conectar" los IDs
        this.gestorReserva = new GestorReserva(gestorRegistro, gestorCancha);
    }

    // --- 3. MÉTODO DE ARRANQUE (PÚBLICO) ---
    public void iniciar() {
        int opcion;
        do {
            // Menú principal simplificado
            System.out.println("\n========= MENÚ DE GESTIÓN DE CANCHAS =========");
            System.out.println("1. Iniciar Sesión");
            System.out.println("2. Registrarse");
            System.out.println("3. Salir");
            System.out.println("=============================================");
            System.out.print("Seleccione una opción: ");

            opcion = leerEntero(); // (B.4) Llama al helper con try-catch

            switch (opcion) {
                    case 1 -> iniciarSesionUsuario();
                    case 2 -> registrarNuevoUsuario();
                case 3 -> System.out.println("✅ Saliendo del sistema...");
                default -> System.out.println("❌ Opción inválida.");
            }
        } while (opcion != 3);
    }

    // --- 4. MÉTODOS DE LÓGICA DE USUARIO ---

    private void registrarNuevoUsuario() {
        // (B.4) REQUISITO: Manejo de excepciones con try-catch
        try {
            System.out.print("Ingrese tipo de usuario (CLIENTE o ADMINISTRADOR): ");
            String tipo = sc.nextLine().toLowerCase();

            System.out.print("Ingrese nombre: ");
            String nombre = sc.nextLine();
            System.out.print("Ingrese apellido: ");
            String apellido = sc.nextLine();
            System.out.print("Ingrese DNI: ");
            int dni = leerEntero();
            System.out.print("Ingrese teléfono: ");
            int telefono = leerEntero();
            System.out.print("Ingrese correo: ");
            String email = sc.nextLine();
            System.out.print("Ingrese contraseña: ");
            String contrasena = sc.nextLine();

            if (tipo.equals("cliente")) {
                // (Corrección): El Gestor asigna el ID, no el constructor.
                // Usamos el constructor que SÍ definimos en la clase Cliente.
                int idCliente = gestorRegistro.asignarIdCliente(); // Asumimos que este método existe
                Cliente cliente = new Cliente(idCliente, nombre, apellido, dni, telefono, email, contrasena);

                gestorRegistro.registarCliente(cliente);
                System.out.println("✅ Cliente registrado correctamente.");

            } else if (tipo.equals("administrador")) {
                System.out.print("Ingrese el código de validación de Admin: ");
                int codigo = leerEntero();

                if (codigo == GestorRegistro.codigoValidacion) {
                    System.out.print("Ingrese un ID de Administrador (ej: 901): ");
                    int idAdmin = leerEntero();
                    Administrador admin = new Administrador(idAdmin, nombre, apellido, dni, telefono, email, contrasena);

                    gestorRegistro.registarAdministrador(admin);
                    System.out.println("✅ Administrador registrado correctamente.");
                } else {
                    System.out.println("❌ Código de validación incorrecto.");
                }
            } else {
                System.out.println("❌ Tipo de usuario no reconocido.");
            }

        } catch (ClienteYaExisteException e) {
            System.out.println("❌ ERROR DE REGISTRO: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("❌ ERROR DE ARCHIVO: No se pudo guardar el usuario. " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ ERROR INESPERADO: " + e.getMessage());
        }
    }

    private void iniciarSesionUsuario() {
        sc.nextLine();
        System.out.println("Ingrese el DNI:");
        int dni = leerEntero();
        System.out.println("Ingrese la contraseña:");
        String contraseña = sc.nextLine();

        Persona usuario = gestorRegistro.iniciarSesion(dni, contraseña);

        if (usuario == null) {
            System.out.println("❌ ¡DNI o contraseña incorrectos!");
        } else {
            // (A.1) POLIMORFISMO
            if (usuario instanceof Administrador) {
                System.out.println("\nBienvenido, Administrador: " + usuario.getNombre());
                menuAdministrador((Administrador) usuario);
            } else if (usuario instanceof Cliente) {
                System.out.println("\nBienvenido, Cliente: " + usuario.getNombre());
                menuCliente((Cliente) usuario);
            }
        }
    }

    // --- 5. MENÚS DE ROL (ADMIN Y CLIENTE) ---

    private void menuAdministrador(Administrador admin) {
        int opcionAdmin;
        do {
            System.out.println("\n--- MENÚ ADMINISTRADOR ---");
            System.out.println("--- Gestión de Canchas ---");
            System.out.println("1. Registrar Nueva Cancha");
            System.out.println("2. Eliminar Cancha");
            System.out.println("3. Asignar Precio a Cancha");
            System.out.println("4. Buscar Cancha");
            System.out.println("--- Gestión de Reservas ---");
            System.out.println("5. Ver Todas las Reservas");
            System.out.println("--- Gestión de Usuarios ---");
            System.out.println("6. Modificar Datos de un Cliente");
            System.out.println("7. Eliminar Cliente");
            System.out.println("8. Modificar Mis Datos de Administrador");
            System.out.println("9. Eliminar Administrador");
            System.out.println("--------------------------");
            System.out.println("10. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");

            opcionAdmin = leerEntero();

            switch (opcionAdmin) {
                case 1 -> registrarNuevaCancha();
                case 2 -> eliminarCancha();
                case 3 -> asignarPrecioCancha();
                case 4 -> buscarCancha();
                case 5 -> listarReservas();
                case 6 -> modificarClienteAdmin();
                case 7 -> eliminarClienteAdmin();
                case 8 -> modificarMisDatos(admin);
                case 9 -> eliminarAdministradorAdmin(admin);
                case 10 -> System.out.println("... Volviendo al menú principal.");
                default -> System.out.println("❌ Opción inválida.");
            }
        } while (opcionAdmin != 10);
    }

    private void menuCliente(Cliente cliente) {
        int opcionCliente;
        do {
            System.out.println("\n--- MENÚ CLIENTE ---");
            System.out.println("1. Realizar Nueva Reserva");
            System.out.println("2. Ver Mis Reservas");
            System.out.println("3. Cancelar Reserva");
            System.out.println("4. Abonar Reserva Pendiente");
            System.out.println("5. Modificar Mis Datos Personales");
            System.out.println("6. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");

            opcionCliente = leerEntero();

            switch (opcionCliente) {
                case 1 -> registrarReserva(cliente);
                case 2 -> verMisReservas(cliente);
                case 3 -> cancelarReserva(cliente);
                case 4 -> realizarPago(cliente);
                case 5 -> modificarMisDatos(cliente);
                case 6 -> System.out.println("... Volviendo al menú principal.");
                default -> System.out.println("❌ Opción inválida.");
            }
        } while (opcionCliente != 6);
    }

    // --- 6. MÉTODOS DE LÓGICA (IMPLEMENTADOS CON TRY-CATCH) ---

    // --- Lógica de Admin ---

    private void registrarNuevaCancha() {
        System.out.println("\n--- Registro de Nueva Cancha ---");
        try {
            System.out.println("Seleccione tipo de cancha (FUTBOL, PADEL, TENIS):");
            TipoCancha tipo = TipoCancha.valueOf(sc.nextLine().toUpperCase()); // (B.2) Uso de Enum

            System.out.print("Ingrese el nombre de la cancha: ");
            String nombreC = sc.nextLine();
            System.out.print("Ingrese la superficie: ");
            String superficie = sc.nextLine();
            System.out.print("Ingrese el precio por hora: ");
            double precio = leerDouble();

            Cancha cancha = null;
            int id = gestorCancha.asignarId(); // Obtenemos un ID único

            // (A.1) Polimorfismo: Decidimos qué clase instanciar
            switch (tipo) {
                case FUTBOL:
                    System.out.print("Ingrese la cantidad de jugadores: ");
                    int cant = leerEntero();
                    cancha = new CanchaFutbol(id,tipo, superficie, nombreC, precio , cant);
                    break;
                case PADEL:
                    System.out.print("Ingrese el tipo de pared (Blindex, Cemento): ");
                    String pared = sc.nextLine();
                    cancha = new CanchaPadel(id,tipo, superficie, nombreC, precio , pared);
                    break;
                case TENIS:
                    System.out.print("¿La cancha es para dobles? (true/false): ");
                    boolean esDoble = Boolean.parseBoolean(sc.nextLine());
                    cancha = new CanchaTenis(id,tipo, superficie, nombreC, precio , esDoble);
                    break;
            }

            gestorCancha.agregarCancha(cancha);
            System.out.println("✅ Cancha '" + nombreC + "' creada con éxito.");

        } catch (CanchaException e) {
            System.out.println("❌ ERROR DE CANCHA: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("❌ ERROR DE ARCHIVO: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("❌ ERROR: Tipo de cancha no válido. Use FUTBOL, PADEL o TENIS.");
        }
    }

    private void eliminarCancha() {
        System.out.println("\n--- Eliminar Cancha ---");
        listarCanchas();
        System.out.print("Ingrese el ID de la cancha a eliminar: ");
        int idCancha = leerEntero();

        try {
            gestorCancha.eliminarCancha(idCancha);
            System.out.println("✅ Cancha eliminada correctamente.");
        } catch (CanchaException e) {
            System.out.println("❌ ERROR: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("❌ ERROR DE ARCHIVO: " + e.getMessage());
        }
    }

    private void asignarPrecioCancha() {
        System.out.println("\n--- Asignar Precio ---");
        listarCanchas();
        System.out.print("Ingrese el ID de la cancha a modificar: ");
        int idCancha = leerEntero();
        System.out.print("Ingrese el nuevo precio por hora: ");
        double precio = leerDouble();

        try {
            gestorCancha.asignarPrecio(idCancha, precio);
            System.out.println("✅ Precio actualizado.");
        } catch (CanchaException e) {
            System.out.println("❌ ERROR: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("❌ ERROR DE ARCHIVO: " + e.getMessage());
        }
    }

    private void buscarCancha() {
        System.out.println("\n--- Buscar Cancha ---");
        System.out.print("Ingrese el ID de la cancha a buscar: ");
        int idCancha = leerEntero();

        Cancha cancha = gestorCancha.buscarCancha(idCancha);
        if (cancha != null) {
            System.out.println("Encontrada: " + cancha.toString());
        } else {
            System.out.println("❌ No se encontró la cancha con ese ID.");
        }
    }

    private void modificarClienteAdmin() {
        System.out.println("\n--- Modificar Cliente ---");
        System.out.print("Ingrese DNI del cliente a modificar: ");
        int dni = leerEntero();

        Persona cliente = gestorRegistro.buscarCliente(dni);
        if (cliente != null && cliente instanceof Cliente) {
            modificarMisDatos(cliente); // Reutilizamos el método
        } else {
            System.out.println("❌ No se encontró un cliente con ese DNI.");
        }
    }

    private void eliminarClienteAdmin() {
        System.out.println("\n--- Eliminar Cliente ---");
        System.out.print("Ingrese DNI del cliente a eliminar: ");
        int dni = leerEntero();

        try {
            gestorRegistro.eliminarCliente(dni);
            System.out.println("✅ Cliente eliminado.");
        } catch (IOException e) {
            System.out.println("❌ ERROR DE ARCHIVO: " + e.getMessage());
        }
    }

    private void eliminarAdministradorAdmin(Administrador adminLogueado) {
        System.out.println("\n--- Eliminar Administrador ---");
        System.out.print("Ingrese DNI del admin a eliminar: ");
        int dni = leerEntero();

        if (dni == adminLogueado.getDni()) {
            System.out.println("❌ No puedes eliminarte a ti mismo.");
            return;
        }

        try {
            gestorRegistro.eliminarAdministrador(dni);
            System.out.println("✅ Administrador eliminado.");
        } catch (IOException e) {
            System.out.println("❌ ERROR DE ARCHIVO: " + e.getMessage());
        }
    }

    // --- Lógica de Cliente ---

    private void registrarReserva(Cliente cliente) {
        System.out.println("\n--- Nueva Reserva ---");
        try {
            if (gestorCancha.obtenerCanchas().isEmpty()) {
                System.out.println("⚠️ No hay canchas registradas para reservar.");
                return;
            }

            listarCanchas();
            System.out.print("Seleccione cancha (ingrese ID): ");
            int idCancha = leerEntero();
            Cancha cancha = gestorCancha.buscarCancha(idCancha);

            if(cancha == null) {
                System.out.println("❌ ID de cancha no válido.");
                return;
            }

            System.out.print("Ingrese fecha de la reserva (ej: 2025-11-28): ");
            LocalDate fecha = LocalDate.parse(sc.nextLine());

            System.out.print("Ingrese hora de inicio (ej: 18:00): ");
            LocalTime horaInicio = LocalTime.parse(sc.nextLine());

            System.out.print("Ingrese hora de fin (ej: 19:00): ");
            LocalTime horaFin = LocalTime.parse(sc.nextLine());

            if (gestorReserva.validarDisponibilidad(idCancha, fecha, horaInicio)) {

                // --- CORRECCIÓN LÓGICA ---
                // El Gestor debe asignar el ID de la reserva, no la UI.
                int idReserva = gestorReserva.asignarIdReserva();

                // Usamos el constructor de Reserva que definimos
                Reserva reserva = new Reserva(idReserva, cliente, cancha, fecha, horaInicio, horaFin, cancha.getPrecioPorHora(), null);

                gestorReserva.registrarReserva(reserva);

                System.out.println("✅ Reserva registrada con estado 'PENDIENTE DE PAGO'.");
                System.out.println("Por favor, abone desde el menú de cliente para confirmarla.");
            } else {
                System.out.println("❌ La cancha no está disponible en esa fecha/hora.");
            }
        } catch (DateTimeParseException e) {
            System.out.println("❌ ERROR: Formato de fecha u hora incorrecto. Use AAAA-MM-DD y HH:MM.");
        } catch (ReservaException e) {
            System.out.println("❌ ERROR DE RESERVA: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("❌ ERROR DE ARCHIVO: " + e.getMessage());
        }
    }

    private void verMisReservas(Cliente cliente) {
        System.out.println("\n--- Mis Reservas ---");

        List<Reserva> misReservas = gestorReserva.historialReservaCliente(cliente.getIdCliente());

        if (misReservas.isEmpty()) {
            System.out.println("No tienes reservas en tu historial.");
        } else {
            for (Reserva r : misReservas) {
                // --- CORRECCIÓN LÓGICA ---
                // Obtenemos la cancha directamente del objeto Reserva
                Cancha c = r.getCancha();
                String nombreCancha = (c != null) ? c.getNombre() : "Cancha Eliminada";

                System.out.printf("- ID: %d | Cancha: %s | Fecha: %s | Hora: %s | Estado: %s | Total: $%.2f\n",
                        r.getIdReserva(),
                        nombreCancha,
                        r.getFecha(),
                        r.getHoraInicio(),
                        r.getEstado(),
                        r.getMontoTotal()
                );
            }
        }
    }

    private void cancelarReserva(Cliente cliente) {
        System.out.println("\n--- Cancelar Reserva ---");
        try {
            List<Reserva> misReservasActivas = gestorReserva.historialReservaCliente(cliente.getIdCliente()).stream()
                    // --- CORRECCIÓN TIPEO ---
                    .filter(r -> r.getEstado() == EstadoReserva.PENDIENTE || r.getEstado() == EstadoReserva.CONFIRMADA)
                    .collect(Collectors.toList());

            if (misReservasActivas.isEmpty()) {
                System.out.println("No tienes reservas activas para cancelar.");
                return;
            }

            for (int i = 0; i < misReservasActivas.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + misReservasActivas.get(i).toString());
            }

            System.out.print("Seleccione la reserva a CANCELAR (ej: 1) o 0 para salir: ");
            int opcion = leerEntero();

            if (opcion == 0 || opcion > misReservasActivas.size()) {
                System.out.println("Operación cancelada.");
                return;
            }
            Reserva reservaACancelar = misReservasActivas.get(opcion - 1);

            gestorReserva.cancelarReserva(reservaACancelar.getIdReserva());
            System.out.println("✅ Reserva cancelada correctamente.");

        } catch (ReservaException e) {
            System.out.println("❌ ERROR: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("❌ ERROR DE ARCHIVO: " + e.getMessage());
        }
    }

    private void realizarPago(Cliente cliente) {
        System.out.println("\n--- Abonar Reserva ---");
        try {
            List<Reserva> misPendientes = gestorReserva.historialReservaCliente(cliente.getIdCliente()).stream()
                    .filter(r -> r.getEstado() == EstadoReserva.PENDIENTE)
                    .collect(Collectors.toList());

            if (misPendientes.isEmpty()) {
                System.out.println("No tienes reservas pendientes de pago.");
                return;
            }

            System.out.println("Reservas pendientes de pago:");
            for (int i = 0; i < misPendientes.size(); i++) {
                Reserva r = misPendientes.get(i);

                // --- CORRECCIÓN LÓGICA ---
                Cancha c = r.getCancha();
                String nombreCancha = (c != null) ? c.getNombre() : "Cancha Eliminada";

                System.out.printf("  %d. Cancha: %s | Fecha: %s | Monto: $%.2f\n",
                        (i + 1), nombreCancha, r.getFecha(), r.getMontoTotal());
            }

            System.out.print("Seleccione la reserva que desea abonar (ej: 1) o 0 para cancelar: ");
            int opcion = leerEntero();
            if (opcion == 0 || opcion > misPendientes.size()) {
                System.out.println("Operación cancelada.");
                return;
            }
            Reserva reservaAPagar = misPendientes.get(opcion - 1);

            System.out.println("Método de pago ('EFECTIVO' o 'TARJETA'):");
            String metodo = sc.nextLine().toLowerCase();

            boolean pagoExitoso = false;

            if (metodo.equals("tarjeta")) {
                System.out.print("Ingrese Nro. de Tarjeta (ej: 16 dígitos): ");
                String numeroTarjeta = sc.nextLine();
                System.out.print("Ingrese entidad bancaria (ej: VISA): ");
                String entidad = sc.nextLine();
                System.out.print("Ingrese cantidad de cuotas (1, 3, 6): ");
                int cuotas = leerEntero();

                if (numeroTarjeta.length() < 10) {
                    System.out.println("❌ Número de tarjeta inválido. Pago cancelado.");
                    return;
                }

                gestorReserva.confirmarPagoTarjeta(reservaAPagar, numeroTarjeta, cuotas, entidad);
                pagoExitoso = true;

            } else if (metodo.equals("efectivo")) {
                gestorReserva.confirmarPagoEfectivo(reservaAPagar, true);
                pagoExitoso = true;
            } else {
                System.out.println("❌ Método de pago no reconocido. Operación cancelada.");
                return;
            }

            if (pagoExitoso) {
                System.out.println("✅ ¡Pago aceptado! Su reserva ha sido confirmada.");
            }

        } catch (ReservaException e) {
            System.out.println("❌ ERROR DE PAGO: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("❌ ERROR DE ARCHIVO: " + e.getMessage());
        }
    }

    // --- Lógica Común ---

    private void modificarMisDatos(Persona persona) {
        System.out.println("\n--- Modificar Mis Datos ---");
        try {
            System.out.print("Ingrese nuevo teléfono (actual: " + persona.getTelefono() + "): ");
            int nuevoTel = leerEntero();
            System.out.print("Ingrese nuevo email (actual: " + persona.getEmail() + "): ");
            String nuevoEmail = sc.nextLine();

            persona.setTelefono(nuevoTel);
            persona.setEmail(nuevoEmail);

            if(persona instanceof Cliente) {
                gestorRegistro.modificarCliente(persona);
            } else if (persona instanceof Administrador) {
                gestorRegistro.modificarAdministrador(persona);
            }

            System.out.println("✅ Datos actualizados.");
        } catch (IOException e) {
            System.out.println("❌ ERROR DE ARCHIVO: No se pudieron guardar los datos.");
        }
    }

    private void listarCanchas() {
        List<Cancha> canchas = gestorCancha.obtenerCanchas();
        if (canchas.isEmpty()) {
            System.out.println("No hay canchas registradas.");
        } else {
            // (B.1) REQUISITO TPI: ORDENAMIENTO
            canchas.sort(Comparator.comparing(Cancha::getNombre));

            System.out.println("\n🏟️ Canchas registradas (Ordenadas por nombre):");
            for (Cancha c : canchas) {
                System.out.println("- " + c.toString());
            }
        }
    }

    private void listarReservas() {
        List<Reserva> reservas = gestorReserva.obtenerReservas();
        if (reservas.isEmpty()) {
            System.out.println("No hay reservas registradas.");
        } else {
            System.out.println("\n📅 Todas las Reservas del Sistema:");
            for (Reserva r : reservas) {
                System.out.println("- " + r.toString());
            }
        }
    }

    // --- 7. MÉTODOS HELPER (PARA ENTRADA SEGURA) ---

    private int leerEntero() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("❌ Ingrese un número válido: ");
            }
        }
    }

    private double leerDouble() {
        while (true) {
            try {
                return Double.parseDouble(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("❌ Ingrese un número válido (ej: 1500.50): ");
            }
        }
    }
}