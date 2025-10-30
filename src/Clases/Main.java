package Clases;

import interfaces.*;
import java.util.*;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final IGestorCancha gestorCancha = new GestorCancha();
    private static final IGestorReserva gestorReserva = new GestorReserva();
    private static final List<ICliente> clientes = new ArrayList<>();

    public static void main(String[] args) {
        int opcion;
        do {
            mostrarMenu();
            System.out.print("Seleccione una opción: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1 -> registrarCliente();
                case 2 -> registrarCancha();
                case 3 -> registrarReserva();
                case 4 -> listarCanchas();
                case 5 -> listarReservas();
                case 6 -> System.out.println(" Saliendo del sistema...");
                default -> System.out.println("❌ Opción inválida.");
            }
            System.out.println();
        } while (opcion != 6);
    }

    private static void mostrarMenu() {
        System.out.println("========= MENÚ DE GESTIÓN DE CANCHAS =========");
        System.out.println("1. ---Registrar Cliente-----");
        System.out.println("2. ---Registrar Cancha------");
        System.out.println("3. ---Registrar Reserva-----");
        System.out.println("4. -----Listar Canchas------");
        System.out.println("5. -----Listar Reservas-----");
        System.out.println("6. ----------Salir----------");
        System.out.println("=============================================");
    }

    private static void registrarCliente() {
        System.out.print("Ingrese nombre del cliente: ");
        String nombre = sc.nextLine();
        System.out.print("Ingrese DNI: ");
        String dni = sc.nextLine();
        System.out.print("Ingrese teléfono: ");
        String telefono = sc.nextLine();

        Cliente cliente = new Cliente(nombre, dni, telefono);
        clientes.add(cliente);

        System.out.println("✅ Cliente registrado correctamente.");
    }

    private static void registrarCancha() {
        System.out.print("Ingrese nombre de la cancha: ");
        String nombre = sc.nextLine();

        System.out.println("Seleccione tipo de cancha:");
        System.out.println("1.⚽ Fútbol");
        System.out.println("2.\uD83C\uDFBE Pádel");
        System.out.println("3.\uD83E\uDD4E Tenis");
        int tipo = leerEntero();

        Cancha cancha;
        switch (tipo) {
            case 1 -> cancha = new CanchaFutbol(nombre);
            case 2 -> cancha = new CanchaPadel(nombre);
            case 3 -> cancha = new CanchaTenis(nombre);
            default -> {
                System.out.println("Tipo inválido, se crea como Cancha genérica.");
                cancha = new Cancha(nombre, TipoCancha.FUTBOL); // valor por defecto
            }
        }

        gestorCancha.agregarCancha(cancha);
        System.out.println("✅ Cancha registrada correctamente.");
    }

    private static void registrarReserva() {
        if (clientes.isEmpty() || gestorCancha.obtenerCanchas().isEmpty()) {
            System.out.println("⚠️ Debe haber al menos un cliente y una cancha registrada.");
            return;
        }

        System.out.println("Clientes disponibles:");
        for (int i = 0; i < clientes.size(); i++) {
            System.out.println((i + 1) + ". " + clientes.get(i).getNombre());
        }
        System.out.print("Seleccione cliente: ");
        int cliIdx = leerEntero() - 1;
        ICliente cliente = clientes.get(cliIdx);

        System.out.println("Canchas disponibles:");
        List<Cancha> canchas = gestorCancha.obtenerCanchas();
        for (int i = 0; i < canchas.size(); i++) {
            System.out.println((i + 1) + ". " + canchas.get(i).getNombre());
        }
        System.out.print("Seleccione cancha: ");
        int canIdx = leerEntero() - 1;
        Cancha cancha = canchas.get(canIdx);

        System.out.print("Ingrese fecha de la reserva (dd/mm/aaaa): ");
        String fecha = sc.nextLine();

        Reserva reserva = new Reserva(cliente, cancha, fecha);
        gestorReserva.registrarReserva(reserva);

        System.out.println("✅ Reserva creada correctamente.");
    }

    private static void listarCanchas() {
        List<Cancha> canchas = gestorCancha.obtenerCanchas();
        if (canchas.isEmpty()) {
            System.out.println("No hay canchas registradas.");
        } else {
            System.out.println("🏟️ Canchas registradas:");
            for (Cancha c : canchas) {
                System.out.println("- " + c.getNombre() + " (" + c.getTipo() + ")");
            }
        }
    }

    private static void listarReservas() {
        List<Reserva> reservas = gestorReserva.obtenerReservas();
        if (reservas.isEmpty()) {
            System.out.println("No hay reservas registradas.");
        } else {
            System.out.println("📅 Reservas:");
            for (Reserva r : reservas) {
                System.out.println("- " + r.getFecha() +
                        " | Cliente: " + r.getCliente().getNombre() + " | Cancha: " +
                        r.getCancha().getNombre());
            }
        }
    }

    private static int leerEntero() {
        while (true) {
            try {
                int n = Integer.parseInt(sc.nextLine());
                return n;
            } catch (NumberFormatException e) {
                System.out.print("Ingrese un número válido: ");
            }
        }
    }
}
