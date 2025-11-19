package vistas;

import Clases.Administrador;
import Clases.EstadoReserva;
import Clases.Reserva;
import Excepciones.ReservaException;
import interfaces.*;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * [TPI D.1] Pantalla de Gestión para Administradores.
 * Muestra todas las reservas y permite cancelarlas.
 */
public class PanelAdministrador extends JFrame {

    private IGestorReserva gestorReserva; // Modelo

    // [TPI D.3] Componentes: JTable para listar datos complejos.
    private JTable tablaReservasAdmin;
    private ReservaTableModel tableModelAdmin; // [DEFENSA] "Usamos un Modelo de Tabla propio para desacoplar los datos de la vista."

    public PanelAdministrador(IGestorRegistro gr, IGestorCancha gc, IGestorReserva gestorReserva, Administrador admin) {
        this.gestorReserva = gestorReserva;

        setTitle("Panel de Administración - " + admin.getNombre());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Carga de Datos ---
        // [GRASP Controller] Pedimos los datos al Modelo (GestorReserva)
        List<Reserva> todasLasReservas = gestorReserva.obtenerReservas();

        // --- Configuración de Tabla ---
        tableModelAdmin = new ReservaTableModel(todasLasReservas);
        tablaReservasAdmin = new JTable(tableModelAdmin);

        // [TPI B.1] Ordenamiento en GUI: Habilitamos el sorter automático de Swing.
        tablaReservasAdmin.setAutoCreateRowSorter(true);

        add(new JScrollPane(tablaReservasAdmin), BorderLayout.CENTER);

        // --- Botones ---
        JPanel panelSur = new JPanel();
        JButton btnCancelar = new JButton("Cancelar Reserva");
        panelSur.add(btnCancelar);
        add(panelSur, BorderLayout.SOUTH);

        // --- Evento: Cancelar Reserva ---
        btnCancelar.addActionListener(e -> cancelarReservaAdmin());
    }

    private void cancelarReservaAdmin() {
        // 1. Obtener selección de la tabla
        int fila = tablaReservasAdmin.getSelectedRow();
        if (fila == -1) return;

        // [DEFENSA] "Convertimos el índice de la vista al del modelo por si la tabla está ordenada."
        int modeloIdx = tablaReservasAdmin.convertRowIndexToModel(fila);
        Reserva reserva = tableModelAdmin.getReservaAt(modeloIdx);

        // 2. Confirmación
        int confirm = JOptionPane.showConfirmDialog(this, "¿Cancelar reserva ID " + reserva.getIdReserva() + "?");
        if (confirm != JOptionPane.YES_OPTION) return;

        // 3. Invocar Lógica de Negocio
        try {
            // [GRASP Controller] Delegamos al Experto.
            gestorReserva.cancelarReserva(reserva.getIdReserva());

            // 4. Actualizar Vista
            JOptionPane.showMessageDialog(this, "Cancelada con éxito.");
            // Recargamos los datos en la tabla
            tableModelAdmin.setReservas(gestorReserva.obtenerReservas());

        } catch (ReservaException ex) {
            // [TPI B.3] Mostrar error de negocio (ej: "No se puede cancelar reserva pasada")
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            // [TPI B.4] Mostrar error técnico
            JOptionPane.showMessageDialog(this, "Error de disco: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}