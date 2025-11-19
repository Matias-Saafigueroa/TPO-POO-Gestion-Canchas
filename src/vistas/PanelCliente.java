package vistas;

import Clases.Cliente;
import Clases.EstadoReserva;
import Clases.Reserva;
import Excepciones.ReservaException;
import interfaces.IGestorCancha;
import interfaces.IGestorRegistro;
import interfaces.IGestorReserva;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * [TPI D.1] Desarrollo de GUI: Pantalla Principal para el Cliente.
 * [TPI A.4] PATRÓN MVC: Esta clase actúa como VISTA (JFrame/Componentes) y CONTROLADOR (ActionListeners).
 * Su responsabilidad es mostrar información al usuario y delegar las acciones a los gestores (Modelo).
 */
public class PanelCliente extends JFrame {

    // [TPI C.3 / SOLID DIP] Inyección de Dependencias:
    // Se reciben las interfaces de los gestores, no las clases concretas.
    // Esto desacopla la vista de la implementación de la lógica de negocio.
    private IGestorRegistro gestorRegistro;
    private IGestorCancha gestorCancha;
    private IGestorReserva gestorReserva;

    // [TPI A.4] Estado de la Sesión: Guardamos el cliente logueado para personalizar la vista.
    private Cliente clienteLogueado;

    // Componentes de la GUI
    // [TPI D.3] Uso de componentes Swing avanzados como JTable y JScrollPane.
    private JTable tablaReservas;
    private ReservaTableModel tableModel; // [TPI D.3] Modelo de tabla personalizado para separar datos de vista.
    private JButton btnReservar;
    private JButton btnPagar;

    public PanelCliente(IGestorRegistro gestorRegistro, IGestorCancha gestorCancha,
                        IGestorReserva gestorReserva, Cliente cliente) {

        this.gestorRegistro = gestorRegistro;
        this.gestorCancha = gestorCancha;
        this.gestorReserva = gestorReserva;
        this.clienteLogueado = cliente;

        setTitle("Panel del Cliente");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // --- Encabezado ---
        // [TPI D.3] Uso de Layouts: BorderLayout para estructurar la ventana principal.
        JPanel panelNorte = new JPanel(new BorderLayout());
        String textoBienvenida = String.format("Bienvenido, %s %s (DNI: %d)",
                cliente.getNombre(), cliente.getApellido(), cliente.getDni());
        JLabel lblBienvenida = new JLabel(textoBienvenida, SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 18));
        lblBienvenida.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelNorte.add(lblBienvenida, BorderLayout.NORTH);

        // --- Panel de Botones Superior ---
        // [TPI D.3] Uso de FlowLayout para organizar botones en fila.
        JPanel panelBotonesAccion = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnReservar = new JButton("Reservar Cancha");
        btnPagar = new JButton("Pagar Reserva Seleccionada");

        panelBotonesAccion.add(btnReservar);
        panelBotonesAccion.add(btnPagar);

        panelNorte.add(panelBotonesAccion, BorderLayout.CENTER);

        add(panelNorte, BorderLayout.NORTH);

        // --- Tabla de Reservas (Panel Central) ---
        // [GRASP Controller] Solicitamos datos al Modelo (GestorReserva) para llenar la vista.
        List<Reserva> misReservas = gestorReserva.historialReservaCliente(clienteLogueado.getIdCliente());

        tableModel = new ReservaTableModel(misReservas);
        tablaReservas = new JTable(tableModel);
        tablaReservas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // [TPI B.1] Ordenamiento: Habilitamos el ordenamiento automático de columnas en la JTable.
        tablaReservas.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(tablaReservas);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Mis Reservas"));

        add(scrollPane, BorderLayout.CENTER);

        // --- Panel de Botones Inferior ---
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnCancelarReserva = new JButton("Cancelar Reserva Seleccionada");
        JButton btnActualizar = new JButton("Actualizar Lista");

        panelSur.add(btnCancelarReserva);
        panelSur.add(btnActualizar);

        add(panelSur, BorderLayout.SOUTH);

        // --- Lógica de Eventos (Controlador) ---
        // [TPI D.2] Manejo de Eventos Complejo: Conectamos acciones de usuario con lógica de negocio.

        // Botón Actualizar -> Llama a método helper para refrescar datos
        btnActualizar.addActionListener(e -> actualizarTabla());

        // Botón Cancelar Reserva -> Llama a método con validaciones y confirmación
        btnCancelarReserva.addActionListener(e -> cancelarReservaSeleccionada());

        // Botón Reservar -> Abre una nueva ventana (JDialog)
        btnReservar.addActionListener(e -> {
            // [DEFENSA] "Navegamos a una sub-ventana modal inyectando las dependencias necesarias."
            VentanaCrearReserva ventanaReserva = new VentanaCrearReserva(
                    this, gestorCancha, gestorReserva, clienteLogueado);
            ventanaReserva.setVisible(true);
            actualizarTabla(); // Actualiza al cerrar la sub-ventana
        });

        // Botón Pagar -> Lógica de validación antes de abrir ventana de pago
        btnPagar.addActionListener(e -> {
            // 1. Obtener la reserva seleccionada de la tabla
            int filaSeleccionada = tablaReservas.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this,
                        "Debe seleccionar una reserva para pagar.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // [DEFENSA] "Convertimos el índice de la vista al del modelo por si la tabla está ordenada."
            int indiceModelo = tablaReservas.convertRowIndexToModel(filaSeleccionada);
            Reserva reservaAPagar = tableModel.getReservaAt(indiceModelo);

            // 2. Validar el estado (Regla de Negocio aplicada en Vista para feedback rápido)
            if (reservaAPagar.getEstado() != EstadoReserva.PENDIENTE) {
                JOptionPane.showMessageDialog(this,
                        "Solo se pueden pagar reservas que estén PENDIENTES.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 3. Abrir la ventana de pago
            VentanaPago ventanaPago = new VentanaPago(this, gestorReserva, reservaAPagar);
            ventanaPago.setVisible(true);

            // 4. Actualizar la tabla al cerrar la ventana de pago
            // (para ver si el estado cambió a CONFIRMADA)
            actualizarTabla();
        });
    }

    /**
     * Método helper para recargar los datos de la tabla desde el backend.
     */
    private void actualizarTabla() {
        List<Reserva> misReservas = gestorReserva.historialReservaCliente(clienteLogueado.getIdCliente());
        tableModel.setReservas(misReservas);
    }

    /**
     * Lógica compleja para cancelar una reserva.
     * Incluye validaciones de UI, confirmación y manejo de excepciones.
     */
    private void cancelarReservaSeleccionada() {
        int filaSeleccionada = tablaReservas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "No seleccionó ninguna reserva.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int indiceModelo = tablaReservas.convertRowIndexToModel(filaSeleccionada);
        Reserva reservaACancelar = tableModel.getReservaAt(indiceModelo);

        if (reservaACancelar == null) {
            JOptionPane.showMessageDialog(this, "Error al obtener la reserva.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validación de estado (EXTRA): No dejar cancelar algo ya cancelado
        if (reservaACancelar.getEstado() == EstadoReserva.CANCELADA) {
            JOptionPane.showMessageDialog(this, "Esta reserva ya está cancelada.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // [DEFENSA] "Usamos un ConfirmDialog para evitar acciones accidentales."
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Seguro que querés cancelar la reserva ID: " + reservaACancelar.getIdReserva() + "?",
                "Confirmar Cancelación", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            // [GRASP Controller] Delegamos la acción al Experto (GestorReserva)
            gestorReserva.cancelarReserva(reservaACancelar.getIdReserva());

            JOptionPane.showMessageDialog(this,
                    "Reserva cancelada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            actualizarTabla();

        } catch (ReservaException ex) {
            // [TPI B.3] Manejo de Excepción de Negocio (ej: fecha pasada)
            JOptionPane.showMessageDialog(this,
                    "Error al cancelar: " + ex.getMessage(), "Error de Negocio", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            // [TPI B.4] Manejo de Excepción Técnica (Error de disco)
            JOptionPane.showMessageDialog(this,
                    "Error de archivo al guardar la cancelación: " + ex.getMessage(), "Error de Sistema", JOptionPane.ERROR_MESSAGE);
        }
    }
}