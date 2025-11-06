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
            System.out.print("Seleccione una opción: ");
                System.out.println("========= MENÚ DE GESTIÓN DE CANCHAS =========");
                System.out.println("1. ---Registrar Usuario/Iniciar Sesion-----");
                System.out.println("2. ---Registrar Cancha------");
                System.out.println("3. ---Registrar Reserva-----");
                System.out.println("4. -----Listar Canchas------");
                System.out.println("5. -----Listar Reservas-----");
                System.out.println("7. -----Cancelar Reserva-----");
                System.out.println("8. -----Realizar Pago-----");
                System.out.println("2. ----------Salir----------");
                System.out.println("=============================================");
            opcion = leerEntero();


            switch (opcion) {
                case 1 -> {
                    GestorRegistro gestorUsuarios = new GestorRegistro();
                    System.out.println("Que desea realizar ('Iniciar Sesion'/'Registrarse')");
                    String eleccion = sc.nextLine().toLowerCase();
                    switch (eleccion) {
                        case "registrarse" -> {
                            System.out.print("Ingrese tipo de usuario (CLIENTE o ADMINSTRADOR)");
                            String tipo = sc.nextLine().toLowerCase();
                            System.out.print("Ingrese nombre  ");
                            String nombre = sc.nextLine().toLowerCase();
                            System.out.print("Ingrese DNI: ");
                            String dni = sc.nextLine().toLowerCase();
                            System.out.print("Ingrese teléfono: ");
                            String telefono = sc.nextLine().toLowerCase();
                            System.out.print("Ingrese correo: ");
                            String correo = sc.nextLine().toLowerCase();
                            if (tipo.equals("cliente")) {
                                Cliente cliente = new Cliente(nombre, dni, telefono);
                                cliente.registrarse(cliente);
                                System.out.println("✅ Cliente registrado correctamente.");
                            }
                            if (tipo.equals("administrador")) {
                                System.out.println("Ingrese el IdAdministrador");
                                String idAdmin = sc.nextLine().toLowerCase();


                            }
                        }
                        case "iniciar sesion"->{
                            System.out.println("Ingrese el DNI");
                            int dni = sc.nextInt();
                            System.out.println("Ingrese la contraseña");
                            String contraseña= sc.nextLine().toLowerCase();
                            Persona usuario = gestorUsuarios.iniciarSesion(dni, contraseña);


                    }


                    }
                }
                case 2 -> {
                    System.out.println("Seleccione tipo de cancha:");
                    System.out.println("1.⚽ Fútbol");
                    System.out.println("2.\uD83C\uDFBE Pádel");
                    System.out.println("3.\uD83E\uDD4E Tenis");
                    int tipo = sc.nextInt();

                    Cancha cancha;
                    switch (tipo) {
                        case 1 -> {
                            while (true) {
                                System.out.print("Ingrese el nombre de la cancha: ");
                                String nombreC = sc.nextLine();
                                boolean nombreEsValido = GestorCancha.validarDatos(nombreC);
                                if (nombreEsValido) {
                                    int id = GestorCancha.asignarId();
                                    System.out.print("Ingrese la superficie: ");
                                    String superficie = sc.nextLine();
                                    TipoCancha tipoCancha = TipoCancha.FUTBOL;
                                    System.out.print("Ingrese la cantidad de jugadores: ");
                                    int cantidadJugadores = sc.nextInt();
                                    sc.nextLine();
                                    Cancha cancha = new CanchaFutbol(id, tipoCancha, superficie, nombreC, cantidadJugadores);
                                    System.out.println("Cancha '" + nombreC + "' creada con éxito.");
                                    GestorCancha.agregarCancha(cancha);
                                    break;
                                } else {
                                    System.out.println("El nombre de la cancha '" + nombreC + "' ya existe. Intente con otro.");
                                }
                            }
                        }


                        case 2 -> {
                            while (true) {
                                System.out.print("Ingrese el nombre de la cancha de pádel: ");
                                String nombreC = sc.nextLine();

                                boolean nombreEsValido = GestorCancha.validarDatos(nombreC);

                                if (nombreEsValido) {
                                    int id = GestorCancha.asignarId();

                                    System.out.print("Ingrese la superficie (ej: Césped sintético): ");
                                    String superficie = sc.nextLine();

                                    // Atributo específico para una cancha de pádel
                                    System.out.print("Ingrese el tipo de pared (ej: Blindex, Cemento): ");
                                    String tipoDePared = sc.nextLine();

                                    // Asignamos el tipo de cancha correspondiente
                                    TipoCancha tipoCancha = TipoCancha.PADEL;

                                    // Creamos una instancia de CanchaPadel
                                    // Nota: El constructor puede variar según tu diseño
                                    Cancha cancha2 = new CanchaPadel(id, tipoCancha, superficie, nombreC, tipoDePared);

                                    System.out.println("Cancha de pádel '" + nombreC + "' creada con éxito.");

                                    break;

                                } else {
                                    System.out.println("El nombre de la cancha '" + nombreC + "' ya existe. Intente con otro.");
                                }
                            }
                        }
                        }
                        case 3 ->{ while (true) {
                        System.out.print("Ingrese el nombre de la cancha de tenis: ");
                        String nombreC = sc.nextLine();

                        boolean nombreEsValido = GestorCancha.validarDatos(nombreC);

                        if (nombreEsValido) {
                            int id = GestorCancha.asignarId();

                            System.out.print("Ingrese la superficie (ej: Polvo de ladrillo, Cemento): ");
                            String superficie = sc.nextLine();
                            System.out.print("¿La cancha es para dobles? (true/false): ");
                            boolean esDoble = sc.nextBoolean();
                            sc.nextLine(); // Limpiamos el buffer

                            // Asignamos el tipo de cancha correspondiente
                            TipoCancha tipoCancha = TipoCancha.TENIS;

                            // Creamos la instancia de CanchaTenis usando tu constructor (con el nombre agregado)
                            Cancha cancha = new CanchaTenis(id, nombreC, superficie, tipoCancha, esDoble);

                            System.out.println("Cancha de tenis '" + nombreC + "' creada con éxito.");

                            break;

                        } else {
                            System.out.println("El nombre de la cancha '" + nombreC + "' ya existe. Intente con otro.");
                        }
                    }
                    }

                }
                case 3 -> {
                    System.out.print("Canchas Disponibles: ");
                    List<Cancha> canchas = GestorCancha.mostrarCanchas();
                    System.out.println("ingresa el ID de la cancha que desees alquilar");
                    //hay que validar que esa cancha no este ocupada
                }

                case 4 ->GestorCancha.mostrarCanchas();
                case 5 -> GestorReserva.historialReservas();
                case 6 -> {break;}
                default -> System.out.println("❌ Opción inválida.");
            }
            System.out.println();
        } while (opcion != 6);
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
