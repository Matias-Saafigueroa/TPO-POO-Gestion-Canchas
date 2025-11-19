package vistas;

package vistas;

import Clases.Administrador;
import Clases.Cliente;
import Clases.Persona;
import interfaces.IGestorCancha;
import interfaces.IGestorRegistro;
import interfaces.IGestorReserva;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * [TPI D.1] Pantalla Principal de Acceso.
 * [TPI A.4] PATRÓN MVC: Actúa como Vista (JFrame) y Controlador (ActionListener).
 */
public class VentanaLogin extends JFrame {

    // [SOLID DIP] Dependencia de abstracciones (Interfaces), no de clases concretas.
    private IGestorRegistro gestorRegistro;
    private IGestorCancha gestorCancha;
    private IGestorReserva gestorReserva;

    // Componentes de la GUI
    private JTextField txtDNI;
    private JPasswordField txtPassword;
    private JButton btnEntrar;
    private JButton btnRegistrarse;
    private JButton btnCancelar;

    // [TPI C.3] Inyección de Dependencias: Recibe los gestores ya inicializados desde Main.
    public VentanaLogin(IGestorRegistro gestorRegistro, IGestorCancha gestorCancha, IGestorReserva gestorReserva) {
        this.gestorRegistro = gestorRegistro;
        this.gestorCancha = gestorCancha;
        this.gestorReserva = gestorReserva;

        // Configuración básica de la ventana (Vista)
        setTitle("Inicio de Sesión");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en pantalla
        setResizable(false);

        // [TPI D.3] Uso de Layouts Complejos: GridBagLayout para un diseño preciso.
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // ... (Construcción de la UI omitida por brevedad, es código estándar de Vista) ...
        // (Añadir DNI, Password, Botones al panel...)

        add(panel);

        validarCampos();    // Configura restricciones de entrada
        configurarEventos(); // Configura la lógica de control
    }

    /**
     * [TPI D.2] Validación en tiempo real (Evento KeyListener).
     * [DEFENSA] "Validamos que solo se ingresen números en el DNI directamente en la vista
     * para mejorar la experiencia de usuario y evitar errores de formato en el backend."
     */
    private void validarCampos() {
        txtDNI.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || txtDNI.getText().length() >= 8) {
                    e.consume(); // Ignorar la tecla si no es válida
                }
            }
        });
    }

    private void configurarEventos() {
        // --- Botón Entrar (Controlador) ---
        btnEntrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 1. Validación de Vista (Campos vacíos o incompletos)
                String dniStr = txtDNI.getText();
                String passStr = new String(txtPassword.getPassword());

                if (dniStr.length() != 8) {
                    JOptionPane.showMessageDialog(VentanaLogin.this,
                            "DNI inválido (deben ser 8 dígitos).", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // ... (Otras validaciones visuales) ...

                // 2. Invocación al Modelo (Backend)
                try {
                    int dni = Integer.parseInt(dniStr);

                    // [GRASP Controller] Delegamos la autenticación al Experto (GestorRegistro).
                    Persona usuario = gestorRegistro.iniciarSesion(dni, passStr);

                    // 3. Lógica de Navegación basada en el resultado
                    if (usuario != null) {
                        JOptionPane.showMessageDialog(VentanaLogin.this, "Bienvenido/a " + usuario.getNombre());

                        // [TPI A.1] Polimorfismo en la Interfaz Gráfica:
                        // [DEFENSA] "El método iniciarSesion devuelve una 'Persona'. Usamos 'instanceof'
                        // para determinar qué pantalla mostrar, aprovechando la herencia."
                        if (usuario instanceof Administrador) {
                            new PanelAdministrador(gestorRegistro, gestorCancha, gestorReserva, (Administrador) usuario).setVisible(true);
                        } else if (usuario instanceof Cliente) {
                            new PanelCliente(gestorRegistro, gestorCancha, gestorReserva, (Cliente) usuario).setVisible(true);
                        }

                        VentanaLogin.this.dispose(); // Cerrar login

                    } else {
                        JOptionPane.showMessageDialog(VentanaLogin.this, "Credenciales incorrectas.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    // [TPI B.4] Manejo de excepciones de formato.
                    JOptionPane.showMessageDialog(VentanaLogin.this, "Formato de DNI incorrecto.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // --- Botón Registrarse ---
        btnRegistrarse.addActionListener(e -> {
            // Navegación a la ventana de registro, pasando las dependencias necesarias.
            VentanaRegistro registro = new VentanaRegistro(VentanaLogin.this, gestorRegistro, VentanaLogin.this);
            registro.setVisible(true);
            VentanaLogin.this.setVisible(false);
        });

        btnCancelar.addActionListener(e -> System.exit(0));
    }
}