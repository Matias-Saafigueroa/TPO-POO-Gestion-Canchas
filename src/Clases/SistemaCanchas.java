package Clases;

import interfaces.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Clase controladora principal del sistema.
 * Maneja todos los menús (Vistas) y coordina las llamadas a los Gestores (Modelo/Servicio).
 * Sigue los principios SOLID (SRP, DIP) y GRASP (Controller, Expert).
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

        // Inyección de Dependencias (Constructor)
        // Creamos los gestores de los que depende GestorReserva
        this.gestorRegistro = new GestorRegistro();
        this.gestorCancha = new GestorCancha();
        // GestorReserva "recibe" a los otros gestores para poder conectar IDs
        this.gestorReserva = new GestorReserva(gestorRegistro, gestorCancha);
    }

    // --- 3. MÉTODO DE ARRANQUE (PÚBLICO) ---
    public void iniciar() {
        int opcion;
        do {
            System.out.println("\n========= MENÚ DE GESTIÓN DE CANCHAS =========");
            System.out.println("1. Iniciar Sesión / Registrarse");
            System.out.println("2. Listar Canchas Disponibles");
            System.out.println("3. Salir");
            System.out.println("=============================================");
            System.out.print("Seleccione una opción: ");

            opcion = leerEntero();

            switch (opcion) {
                case 1 -> menuLoginRegistro();
                case 2 -> listarCanchas();
                case 3 -> System.out.println("✅ Saliendo del sistema...");
                default -> System.out.println("❌ Opción inválida.");
            }
        } while (opcion != 3);
    }

    // --- 4. MÉTODOS DE LÓGICA DE USUARIO ---

    private void menuLoginRegistro() {
        System.out.println("Que desea realizar ('Iniciar Sesion'/'Registrarse')");
        String eleccion = sc.nextLine().toLowerCase();

        switch (eleccion) {
            case "registrarse" -> registrarNuevoUsuario();
            case "iniciar sesion" -> iniciarSesionUsuario();
            default -> System.out.println("❌ Opción no válida.");
        }
    }

    private void registrarNuevoUsuario() {
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
            // Usamos el constructor de Cliente para crear un nuevo usuario
            Cliente cliente = new Cliente(nombre, apellido, dni, telefono, email, contrasena);
            if (gestorRegistro.registarCliente(cliente)) {
                System.out.println("✅ Cliente registrado correctamente.");
            } else {
                System.out.println("❌ Error: El DNI ya existe.");
            }
        } else if (tipo.equals("administrador")) {
            System.out.print("Ingrese el código de validación de Admin: ");
            int codigo = leerEntero();

            if (codigo == GestorRegistro.codigoValidacion) {
                System.out.print("Ingrese un ID de Administrador (ej: 901): ");
                int idAdmin = leerEntero();
                Administrador admin = new Administrador(idAdmin, nombre, apellido, dni, telefono, email, contrasena);
                if (gestorRegistro.registarAdministrador(admin)) {
                    System.out.println("✅ Administrador registrado correctamente.");
                } else {
                    System.out.println("❌ Error: El DNI o ID ya existe.");
                }
            } else {
                System.out.println("❌ Código de validación incorrecto.");
            }
        } else {
            System.out.println("❌ Tipo de usuario no reconocido.");
        }
    }

    private void iniciarSesionUsuario() {
        System.out.println("Ingrese el DNI:");
        int dni = leerEntero();
        System.out.println("Ingrese la contraseña:");
        String contraseña = sc.nextLine();

        Persona usuario = gestorRegistro.iniciarSesion(dni, contraseña);

        if (usuario == null) {
            System.out.println("❌ ¡DNI o contraseña incorrectos!");
        } else {
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
                case 8 -> modificarMisDatos(admin); // Le pasamos el admin logueado
                case 9 -> eliminarAdministradorAdmin(admin); // Le pasamos el admin logueado
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
                case 1 -> registrarReserva(cliente); // Le pasamos el cliente logueado
                case 2 -> verMisReservas(cliente);
                case 3 -> cancelarReserva(cliente);
                case 4 -> realizarPago(cliente);
                case 5 -> modificarMisDatos(cliente); // Le pasamos el cliente logueado
                case 6 -> System.out.println("... Volviendo al menú principal.");
                default -> System.out.println("❌ Opción inválida.");
            }
        } while (opcionCliente != 6);
    }

    // --- 6. MÉTODOS DE LÓGICA (IMPLEMENTADOS) ---

    // --- Lógica de Admin ---

    private void eliminarCancha() {
        System.out.println("\n--- Eliminar Cancha ---");
        listarCanchas();
        System.out.print("Ingrese el ID de la cancha a eliminar: ");
        int idCancha = leerEntero();

        if (gestorCancha.eliminarCancha(idCancha)) {
            System.out.println("✅ Cancha eliminada correctamente.");
        } else {
            System.out.println("❌ Error: No se encontró la cancha con ese ID.");
        }
    }

    private void asignarPrecioCancha() {
        System.out.println("\n--- Asignar Precio ---");
        listarCanchas();
        System.out.print("Ingrese el ID de la cancha a modificar: ");
        int idCancha = leerEntero();
        System.out.print("Ingrese el nuevo precio por hora: ");
        double precio = leerDouble();

        if (gestorCancha.asignarPrecio(idCancha, precio)) {
            System.out.println("✅ Precio actualizado.");
        } else {
            System.out.println("❌ Error: No se encontró la cancha.");
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
            modificarMisDatos(cliente); // Reutilizamos el método de modificar datos
        } else {
            System.out.println("❌ No se encontró un cliente con ese DNI.");
        }
    }

    private void eliminarClienteAdmin() {
        System.out.println("\n--- Eliminar Cliente ---");
        System.out.print("Ingrese DNI del cliente a eliminar: ");
        int dni = leerEntero();

        if (gestorRegistro.eliminarCliente(dni)) {
            System.out.println("✅ Cliente eliminado.");
        } else {
            System.out.println("❌ No se encontró cliente.");
        }
    }

    private void eliminarAdministradorAdmin(Administrador adminLogueado) {
        System.out.println("\n--- Eliminar Administrador ---");
        System.out.print("Ingrese DNI del admin a eliminar: ");
        int dni = leerEntero();

        if (dni == adminLogueado.getDni()) {
            System.out.println("❌ No puedes eliminarte a ti mismo.");
        } else if (gestorRegistro.eliminarAdministrador(dni)) {
            System.out.println("✅ Administrador eliminado.");
        } else {
            System.out.println("❌ No se encontró admin.");
        }
    }

    // --- Lógica de Cliente ---

    private void registrarReserva(Cliente cliente) {
        System.out.println("\n--- Nueva Reserva ---");
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
        LocalDate fecha = LocalDate.parse(sc.nextLine()); // (Asegúrate de que la clase Reserva importe java.time.LocalDate)

        System.out.print("Ingrese hora de inicio (ej: 18:00): ");
        LocalTime horaInicio = LocalTime.parse(sc.nextLine()); // (Asegúrate de que la clase Reserva importe java.time.LocalTime)

        // Validamos con el Experto (GestorReserva)
        if (gestorReserva.validarReserva(idCancha, fecha, horaInicio)) {

            // Creamos la reserva (asumo un constructor que NO pide pago)
            Reserva reserva = new Reserva(cliente.getIdCliente(), idCancha, fecha, horaInicio);

            // ¡IMPORTANTE! El constructor de Reserva debe poner
            // automáticamente el estado en PENDIENTE_DE_PAGO.

            if(gestorReserva.registrarReserva(reserva)) { // El gestor la guarda en el CSV
                System.out.println("✅ Reserva registrada con estado 'PENDIENTE DE PAGO'.");
                System.out.println("Por favor, abone desde el menú de cliente para confirmarla.");
            } else {
                System.out.println("❌ Error al guardar la reserva.");
            }
        } else {
            System.out.println("❌ La cancha no está disponible en esa fecha/hora.");
        }
    }

    private void verMisReservas(Cliente cliente) {
        System.out.println("\n--- Mis Reservas ---");

        List<Reserva> misReservas = gestorReserva.historialReservaCliente(cliente.getIdCliente());

        if (misReservas.isEmpty()) {
            System.out.println("No tienes reservas en tu historial.");
        } else {
            for (Reserva r : misReservas) {
                // (Asumo que el toString() de Reserva es informativo)
                System.out.println("- " + r.toString());
            }
        }
    }

    private void cancelarReserva(Cliente cliente) {
        System.out.println("\n--- Cancelar Reserva ---");

        // 1. Obtener y mostrar solo las reservas PENDIENTES o CONFIRMADAS del cliente
        List<Reserva> misReservasActivas = gestorReserva.historialReservaCliente(cliente.getIdCliente()).stream()
                .filter(r -> r.getEstado() == EstadoReserva.PENDIENTE || r.getEstado() == EstadoReserva.CONFIRMADA)
                .collect(Collectors.toList());

        if (misReservasActivas.isEmpty()) {
            System.out.println("No tienes reservas activas para cancelar.");
            return;
        }

        for (int i = 0; i < misReservasActivas.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + misReservasActivas.get(i).toString());
        }

        // 2. Pedir al usuario que elija una
        System.out.print("Seleccione la reserva a CANCELAR (ej: 1) o 0 para salir: ");
        int opcion = leerEntero();

        if (opcion == 0 || opcion > misReservasActivas.size()) {
            System.out.println("Operación cancelada.");
            return;
        }

        Reserva reservaACancelar = misReservasActivas.get(opcion - 1);

        // 3. Llamar al experto
        if (gestorReserva.cancelarReserva(reservaACancelar.getIdReserva())) {
            System.out.println("✅ Reserva cancelada correctamente.");
        } else {
            System.out.println("❌ Error al cancelar la reserva.");
        }
    }

    private void realizarPago(Cliente cliente) {
        System.out.println("\n--- Abonar Reserva ---");

        // 1. Filtrar solo las mías Y que estén pendientes
        List<Reserva> misPendientes = gestorReserva.historialReservaCliente(cliente.getIdCliente()).stream()
                .filter(r -> r.getEstado() == EstadoReserva.PENDIENTE)
                .collect(Collectors.toList());

        if (misPendientes.isEmpty()) {
            System.out.println("No tienes reservas pendientes de pago.");
            return;
        }

        // 2. Mostrar al usuario las reservas que puede pagar
        System.out.println("Reservas pendientes de pago:");
        for (int i = 0; i < misPendientes.size(); i++) {
            Reserva r = misPendientes.get(i);
            System.out.printf("  %d. Cancha: %s | Fecha: %s | Monto: $%.2f\n",
                    (i + 1),
                    gestorCancha.buscarCancha(r.getIdCancha()).getNombre(), // Buscamos el nombre
                    r.getFecha(),
                    r.getMontoTotal());
        }

        // 3. Pedir al usuario que elija una
        System.out.print("Seleccione la reserva que desea abonar (ej: 1) o 0 para cancelar: ");
        int opcion = leerEntero();

        if (opcion == 0 || opcion > misPendientes.size()) {
            System.out.println("Operación cancelada.");
            return;
        }

        Reserva reservaAPagar = misPendientes.get(opcion - 1);

        // 4. Proceso de pago
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

            if (numeroTarjeta.length() >= 10) {
                System.out.println("Procesando pago con tarjeta...");
                pagoExitoso = gestorReserva.confirmarPagoTarjeta(
                        reservaAPagar, numeroTarjeta, cuotas, entidad);
            } else {
                System.out.println("❌ Número de tarjeta inválido. Pago cancelado.");
            }

        } else if (metodo.equals("efectivo")) {
            System.out.println("Pago en efectivo seleccionado. (Debe abonar en caja)");
            pagoExitoso = gestorReserva.confirmarPagoEfectivo(reservaAPagar, true);
        } else {
            System.out.println("❌ Método de pago no reconocido. Operación cancelada.");
        }

        // 5. Confirmar al usuario
        if (pagoExitoso) {
            System.out.println("✅ ¡Pago aceptado! Su reserva ha sido confirmada.");
        } else {
            System.out.println("❌ Hubo un error al procesar el pago o confirmar la reserva.");
        }
    }

    // --- Lógica Común ---

    /**
     * Método genérico para que un Admin o un Cliente modifiquen sus propios datos.
     * @param persona El objeto Persona (Cliente o Admin) que inició sesión.
     */
    private void modificarMisDatos(Persona persona) {
        System.out.println("\n--- Modificar Mis Datos ---");
        System.out.print("Ingrese nuevo teléfono (actual: " + persona.getTelefono() + "): ");
        int nuevoTel = leerEntero();
        System.out.print("Ingrese nuevo email (actual: " + persona.getEmail() + "): ");
        String nuevoEmail = sc.nextLine();

        persona.setTelefono(nuevoTel);
        persona.setEmail(nuevoEmail);

        // El gestor sabe si es Cliente o Admin y llama al método correcto
        if(persona instanceof Cliente) {
            gestorRegistro.modificarCliente(persona);
        } else if (persona instanceof Administrador) {
            gestorRegistro.modificarAdministrador(persona);
        }

        System.out.println("✅ Datos actualizados.");
    }

    private void registrarNuevaCancha() {
        // ... (Este método queda igual que en la versión anterior) ...
    }

    private void listarCanchas() {
        List<Cancha> canchas = gestorCancha.obtenerCanchas();
        if (canchas.isEmpty()) {
            System.out.println("No hay canchas registradas.");
        } else {
            System.out.println("\n🏟️ Canchas registradas:");
            for (Cancha c : canchas) {
                System.out.println("- " + c.toString()); // Usamos el toString() de Cancha
            }
        }
    }

    private void listarReservas() {
        List<Reserva> reservas = gestorReserva.obtenerReservas();
        if (reservas.isEmpty()) {
            System.out.println("No hay reservas registradas.");
        } else {
            System.out.println("\n📅 Todas las Reservas:");
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