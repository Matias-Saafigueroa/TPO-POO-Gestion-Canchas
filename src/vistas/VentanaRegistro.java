package vistas;

import Clases.Administrador;
import Clases.Cliente;
import Clases.GestorRegistro;
import Clases.Persona;
import Excepciones.ClienteYaExisteException;
import interfaces.IGestorRegistro;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

/**
 * [TPI D.1] Desarrollo de GUI: Formulario complejo para la gestión de datos (Registro).
 * [TPI A.4] PATRÓN MVC (Vista/Controlador): Esta clase captura los datos del usuario
 * y delega la lógica de negocio al Modelo (GestorRegistro).
 */
public class VentanaRegistro extends JDialog {

    // Componentes de la GUI (Campos de texto, botones, etc.)
    private JTextField txtNombre, txtApellido, txtDni, txtTelefono, txtEmail, txtCodigoAdmin;
    private JPasswordField txtPassword;
    private JComboBox<String> comboTipoUsuario;
    private JButton btnRegistrar, btnCancelar;
    private JLabel lblCodigoAdmin;

    // [TPI C.3] INYECCIÓN DE DEPENDENCIA:
    // Dependemos de la interfaz IGestorRegistro, no creamos una instancia nueva.
    // Esto nos permite usar el mismo gestor (con los mismos datos cargados) que el Login.
    private IGestorRegistro gestorRegistro;
    private VentanaLogin loginOwner; // Referencia a la ventana padre para volver.

    public VentanaRegistro(Frame owner, IGestorRegistro gestorRegistro, VentanaLogin loginOwner) {
        // Configuración de JDialog modal (bloquea la ventana anterior hasta cerrarse)
        super(owner, "Registro de Nuevo Usuario", true);
        this.gestorRegistro = gestorRegistro;
        this.loginOwner = loginOwner;

        // [TPI D.3] Uso de Look & Feel para mejorar la apariencia (Nimbus).
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            System.out.println("Error al aplicar Nimbus Look and Feel: " + e.getMessage());
        }

        configurarLayout();  // Inicializa componentes visuales
        configurarEventos(); // Configura los controladores (listeners)

        // Configuración final de la ventana
        setSize(450, 480);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    /**
     * [TPI D.3] Componentes y Layouts.
     * [DEFENSA] "Utilizamos GridBagLayout, que es uno de los layouts más flexibles y complejos
     * de Swing, para organizar el formulario de manera precisa y responsive."
     */
    private void configurarLayout() {
        setLayout(new BorderLayout(10, 10));

        // Título
        JLabel lblTitulo = new JLabel("Formulario de Registro", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        add(lblTitulo, BorderLayout.NORTH);

        // Panel de Formulario con GridBagLayout
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); // Espaciado (Padding)
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Estirar componentes horizontalmente

        // --- Fila 0: Nombre ---
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; // Dar peso para que se estire
        txtNombre = new JTextField(20);
        panelFormulario.add(txtNombre, gbc);

        // ... (Se omiten las filas repetitivas de Apellido, DNI, Teléfono, Email y Pass por brevedad) ...
        // ... (La lógica de GridBagLayout se repite para cada campo) ...

        // --- Fila 6: Tipo de Usuario (JComboBox) ---
        gbc.gridx = 0; gbc.gridy = 6; gbc.weightx = 0.0;
        panelFormulario.add(new JLabel("Tipo de Usuario:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        comboTipoUsuario = new JComboBox<>(new String[]{"Cliente", "Administrador"});
        panelFormulario.add(comboTipoUsuario, gbc);

        // --- Fila 7: Código Admin (Campo dinámico) ---
        // [DEFENSA] "Este campo está oculto por defecto y solo se muestra si se elige Administrador."
        gbc.gridx = 0; gbc.gridy = 7; gbc.weightx = 0.0;
        lblCodigoAdmin = new JLabel("Código Admin:");
        panelFormulario.add(lblCodigoAdmin, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtCodigoAdmin = new JTextField(20);
        panelFormulario.add(txtCodigoAdmin, gbc);

        // Estado inicial: Oculto
        lblCodigoAdmin.setVisible(false);
        txtCodigoAdmin.setVisible(false);

        add(panelFormulario, BorderLayout.CENTER);

        // Panel de Botones (FlowLayout a la derecha)
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnRegistrar = new JButton("Registrar");
        btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnCancelar);
        panelBotones.add(btnRegistrar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    /**
     * [TPI D.2] Manejo de Eventos Complejo.
     * Aquí conectamos la Vista (Botones) con el Controlador (Acciones).
     */
    private void configurarEventos() {
        // Botón Cancelar
        btnCancelar.addActionListener(e -> dispose());

        // Botón Registrar -> Llama a la lógica de validación y negocio
        btnRegistrar.addActionListener(e -> procesarRegistro());

        // Lógica dinámica de UI: Mostrar/Ocultar campo de código admin
        comboTipoUsuario.addActionListener(e -> {
            boolean esAdmin = "Administrador".equals(comboTipoUsuario.getSelectedItem());
            lblCodigoAdmin.setVisible(esAdmin);
            txtCodigoAdmin.setVisible(esAdmin);
        });

        // [DEFENSA] "Usamos un WindowListener para garantizar la navegabilidad.
        // Cuando esta ventana se cierra (por éxito o cancelación), reactivamos la ventana de Login."
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                loginOwner.setVisible(true);
            }
        });
    }

    /**
     * Método principal del Controlador de esta vista.
     * Coordina la validación visual y la llamada al Modelo.
     */
    private void procesarRegistro() {
        // 1. Recolección de datos de la Vista
        String nombre = txtNombre.getText().trim();
        // ... (obtención del resto de variables) ...
        String codigoAdmin = txtCodigoAdmin.getText().trim();

        // 2. Validaciones de UI (Responsabilidad de la Vista)
        // [DEFENSA] "Validamos formato (campos vacíos, números) en la vista para no sobrecargar al gestor."
        if (nombre.isEmpty() || /*...otros...*/ false) {
            sgError("Todos los campos son obligatorios.");
            return;
        }

        if (dniStr.length() != 8 || !esNumero(dniStr)) {
            sgError("DNI inválido. Deben ser 8 dígitos numéricos.");
            return;
        }

        // ... (Validaciones de teléfono y contraseña omitidas por brevedad) ...

        // Validación de Email con lógica custom
        if (!esEmailValido(email)) {
            sgError("Email inválido. Debe tener un formato como 'usuario@dominio.com'.");
            return;
        }

        // 3. Conversión de datos
        int dni = Integer.parseInt(dniStr);
        int telefono = Integer.parseInt("11" + telStr);

        try {
            // 4. Lógica de Negocio (Invocación al Modelo)
            Persona nuevaPersona = null;

            if ("Cliente".equals(tipo)) {
                // [INFO] Casting necesario porque el método 'asignarId' es propio de la implementación,
                // no de la interfaz genérica.
                int idCliente = ((GestorRegistro) gestorRegistro).asignarIdCliente();

                // [TPI A.1] Instanciación de subclase concreta
                nuevaPersona = new Cliente(idCliente, nombre, apellido, dni, telefono, email, pass);

                // [GRASP Controller] Delegamos el registro al Experto (GestorRegistro)
                gestorRegistro.registarCliente(nuevaPersona);

            } else if ("Administrador".equals(tipo)) {
                // Validación de seguridad específica de Admin
                if (!codigoAdmin.equals(String.valueOf(GestorRegistro.codigoValidacion))) {
                    sgError("Código de administrador incorrecto.");
                    return;
                }

                int idAdmin = ((GestorRegistro) gestorRegistro).asignarIdAdmin();
                nuevaPersona = new Administrador(idAdmin, nombre, apellido, dni, telefono, email, pass);
                gestorRegistro.registarAdministrador(nuevaPersona);
            }

            // 5. Feedback y Cierre
            sgInfo("¡Registro exitoso! Ya podés iniciar sesión.");
            this.dispose(); // Cierra la ventana y dispara windowClosed

        } catch (ClienteYaExisteException e) {
            // [TPI B.3] Manejo de Excepción de Negocio (DNI Duplicado)
            sgError("ERROR DE NEGOCIO: " + e.getMessage());
        } catch (IOException e) {
            // [TPI B.4] Manejo de Excepción Técnica (Error de disco)
            sgError("ERROR CRÍTICO: No se pudo guardar en el archivo: " + e.getMessage());
        } catch (Exception e) {
            sgError("Error inesperado: " + e.getMessage());
        }
    }

    // --- Métodos Helper (Validaciones y Mensajes) ---

    private boolean esNumero(String s) {
        try {
            Long.parseLong(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean esEmailValido(String email) {
        if (email == null || email.isEmpty()) return false;
        int arrobaPos = email.indexOf('@');
        int puntoPos = email.lastIndexOf('.');
        return arrobaPos > 0 && puntoPos > arrobaPos + 1 && puntoPos < email.length() - 1;
    }

    /**
     * [TPI B.4] Gestión de Errores en GUI.
     * Centralizamos la forma de mostrar errores usando JOptionPane.
     */
    private void sgError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error de Registro", JOptionPane.ERROR_MESSAGE);
    }

    private void sgInfo(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Registro", JOptionPane.INFORMATION_MESSAGE);
    }
}