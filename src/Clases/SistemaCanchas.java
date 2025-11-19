package Clases;

import interfaces.*;
import Excepciones.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * [TPI A.4] GRASP CONTROLADOR: Es el intermediario entre el Usuario y la Lógica.
 * Recibe la entrada, coordina y delega, pero NO ejecuta reglas de negocio.
 */
public class SistemaCanchas {

    private final Scanner sc;
    // [SOLID DIP] Depende de las Interfaces de los gestores, no de las clases.
    private final IGestorCancha gestorCancha;
    private final IGestorRegistro gestorRegistro;
    private final IGestorReserva gestorReserva;

    public SistemaCanchas() {
        this.sc = new Scanner(System.in);
        // [TPI Arquitectura] Inyección de dependencias manual (Wiring).
        this.gestorRegistro = new GestorRegistro();
        this.gestorCancha = new GestorCancha();
        this.gestorReserva = new GestorReserva(gestorRegistro, gestorCancha);
    }

    public void iniciar() {
        // ... (bucle del menú principal) ...
    }

    private void registrarNuevoUsuario() {
        // [TPI B.4] Gestión de Errores: Bloque try-catch robusto.
        try {
            // ... (recolección de datos por Scanner) ...

            if (tipo.equals("cliente")) {
                // ... (creación de objeto) ...

                // [GRASP Controller] Delegación al Experto.
                gestorRegistro.registarCliente(cliente);
                System.out.println(" Cliente registrado correctamente.");
            }
            // ... (else if admin) ...

        } catch (ClienteYaExisteException e) {
            // [TPI B.3] Manejo de Excepción de Negocio: Feedback claro al usuario.
            System.out.println(" ERROR DE REGISTRO: " + e.getMessage());
        } catch (IOException e) {
            // [TPI B.4] Manejo de Excepción Técnica: Error de E/S.
            System.out.println(" ERROR DE ARCHIVO: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(" ERROR INESPERADO: " + e.getMessage());
        }
    }

    // ... (iniciarSesionUsuario, menus...) ...

    private void listarCanchas() {
        List<Cancha> canchas = gestorCancha.obtenerCanchas();
        if (canchas.isEmpty()) {
            System.out.println("No hay canchas registradas.");
        } else {
            // [TPI B.1] Ordenamiento: Uso de Comparator para cumplir el requisito.
            canchas.sort(Comparator.comparing(Cancha::getNombre));

            System.out.println("\n🏟️ Canchas registradas (Ordenadas por nombre):");
            for (Cancha c : canchas) {
                System.out.println("- " + c.toString());
            }
        }
    }

    // ... (resto de métodos, todos aplicando try-catch y delegación a gestores) ...
}