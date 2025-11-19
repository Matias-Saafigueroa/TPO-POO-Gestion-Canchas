package vistas;

import Clases.*;
import Excepciones.ReservaException;
import interfaces.IGestorCancha;
import interfaces.IGestorReserva;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * [TPI D.1] Sub-ventana modal para un proceso específico.
 * [TPI A.4] MVC: Vista que captura datos y Controlador que coordina la creación.
 */
public class VentanaCrearReserva extends JDialog {

    private IGestorCancha gestorCancha;
    private IGestorReserva gestorReserva;
    private Cliente clienteLogueado;

    // [TPI D.3] Componentes: Listas con Modelos (DefaultListModel)
    private JComboBox<TipoCancha> comboDeporte;
    private JTextField txtFecha;
    private JList<Cancha> listaCanchas;
    private DefaultListModel<Cancha> canchasListModel; // Modelo de datos de la lista visual
    private JList<LocalTime> listaHorarios;
    private DefaultListModel<LocalTime> horariosListModel;
    private JButton btnConsultar, btnReservar, btnVolver;

    private final LocalTime[] HORARIOS_POSIBLES = {
            LocalTime.of(18, 0), LocalTime.of(19, 0), LocalTime.of(20, 0),
            LocalTime.of(21, 0), LocalTime.of(22, 0)
    };

    public VentanaCrearReserva(JFrame owner, IGestorCancha gc, IGestorReserva gr, Cliente cliente) {
        super(owner, "Crear Nueva Reserva", true); // Modal = true
        this.gestorCancha = gc;
        this.gestorReserva = gr;
        this.clienteLogueado = cliente;

        setSize(600, 450);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        // ... (Construcción de paneles omitida para brevedad, usa BorderLayout y GridLayout) ...

        // [TPI D.3] Uso de Renderer personalizado para mostrar objetos 'Cancha' en la lista
        canchasListModel = new DefaultListModel<>();
        listaCanchas = new JList<>(canchasListModel);
        listaCanchas.setCellRenderer(new CanchaListCellRenderer());

        // ... (Inicialización de otros componentes) ...

        configurarEventos();
    }

    private void configurarEventos() {
        // [TPI D.2] Manejo de Evento: Consulta y Filtrado.
        // [DEFENSA] "Al hacer clic en Consultar, no solo leemos datos, sino que
        // filtramos la lista de canchas desde el backend y actualizamos la vista dinámicamente."
        btnConsultar.addActionListener(e -> consultarDisponibilidad());

        // [TPI D.2] Manejo de Evento: Transacción.
        btnReservar.addActionListener(e -> realizarReserva());

        btnVolver.addActionListener(e -> dispose());
    }

    private void consultarDisponibilidad() {
        try {
            TipoCancha tipo = (TipoCancha) comboDeporte.getSelectedItem();
            // Validar fecha
            LocalDate.parse(txtFecha.getText());

            canchasListModel.clear();

            // [GRASP Controller] Pedimos datos al Modelo y filtramos (Lógica de UI)
            List<Cancha> filtradas = gestorCancha.obtenerCanchas().stream()
                    .filter(c -> c.getTipoCancha() == tipo)
                    .collect(Collectors.toList());

            if (filtradas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay canchas para este deporte.");
                return;
            }

            // Actualizamos el modelo de la lista visual
            for (Cancha c : filtradas) {
                canchasListModel.addElement(c);
            }
            // (Cargar horarios...)

        } catch (DateTimeParseException ex) {
            // [TPI B.4] Manejo de error de formato de fecha
            JOptionPane.showMessageDialog(this, "Fecha inválida (AAAA-MM-DD).", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void realizarReserva() {
        // 1. Validación de Selección (Vista)
        Cancha cancha = listaCanchas.getSelectedValue();
        LocalTime hora = listaHorarios.getSelectedValue();
        if (cancha == null || hora == null) {
            JOptionPane.showMessageDialog(this, "Seleccione cancha y horario.");
            return;
        }

        try {
            LocalDate fecha = LocalDate.parse(txtFecha.getText());

            // 2. Validación de Negocio (Delegación al Experto)
            if (!gestorReserva.validarDisponibilidad(cancha.getIdCancha(), fecha, hora)) {
                JOptionPane.showMessageDialog(this, "Horario ocupado.");
                return;
            }

            // 3. Creación de Reserva
            int idReserva = gestorReserva.asignarIdReserva();
            // (Calculo de hora fin y monto...)
            Reserva nueva = new Reserva(idReserva, clienteLogueado, cancha, fecha, hora, hora.plusHours(1), cancha.getPrecioPorHora(), null);

            // 4. Persistencia
            gestorReserva.registrarReserva(nueva);

            JOptionPane.showMessageDialog(this, "Reserva creada (Pendiente de Pago).");
            dispose();

        } catch (ReservaException | IOException ex) {
            // [TPI B.3 / B.4] Manejo de errores de negocio y sistema
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}