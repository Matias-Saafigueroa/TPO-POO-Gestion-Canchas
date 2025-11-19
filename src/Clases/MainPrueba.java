package Clases;

import interfaces.IGestorCancha;
import interfaces.IGestorRegistro;
import interfaces.IGestorReserva;
import vistas.VentanaLogin;
import javax.swing.*;
import java.util.Scanner;

/**
 * [TPI Arquitectura] Punto de entrada de la aplicación.
 * Demuestra el desacoplamiento total entre el Backend (Lógica) y el Frontend (Vista).
 */
public class MainPrueba {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            System.out.println("Ingrese '1' para utilizar la consola o '2' para utilizar la interfaz: ");
            int opcion = sc.nextInt();

            switch (opcion) {
                case 1 -> {
                    // [DEFENSA] "Aquí reutilizamos la MISMA lógica de negocio para una interfaz de consola."
                    SistemaCanchas aplicacion = new SistemaCanchas();
                    aplicacion.iniciar();
                }

                case 2 -> {
                    // Configuración visual (Look & Feel)
                    try {
                        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                            if ("Nimbus".equals(info.getName())) {
                                UIManager.setLookAndFeel(info.getClassName());
                                break;
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("No se pudo aplicar el Look & Feel Nimbus.");
                    }

                    // [TPI A.4 / SOLID DIP] Inyección de Dependencias (Manual).
                    // Creamos las instancias concretas de los gestores AQUÍ, en el nivel más alto.
                    IGestorRegistro gestorRegistro = new GestorRegistro();
                    IGestorCancha gestorCancha = new GestorCancha();

                    // [TPI A.3] Inyectamos los gestores necesarios dentro de GestorReserva.
                    IGestorReserva gestorReserva = new GestorReserva(gestorRegistro, gestorCancha);

                    // [TPI D.1] Lanzamiento seguro de la GUI en el Event Dispatch Thread (EDT).
                    SwingUtilities.invokeLater(() -> {
                        // [DEFENSA] "La VentanaLogin recibe los gestores listos para usar.
                        // No los crea ella misma, lo que reduce el acoplamiento."
                        VentanaLogin login = new VentanaLogin(gestorRegistro, gestorCancha, gestorReserva);
                        login.setVisible(true);
                    });
                }
                default -> System.out.println("ingrese un numero valido");
            }
        } catch (Exception e) {
            System.err.println("Error al intentar ingresar los datos");
        }
    }
}