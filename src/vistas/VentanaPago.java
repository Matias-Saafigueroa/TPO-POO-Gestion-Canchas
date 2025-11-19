package vistas;

import Clases.Reserva;
import Excepciones.ReservaException;
import interfaces.IGestorReserva;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.IOException;

/**
 * [TPI D.1] Formulario de Pago.
 * [TPI D.3] Uso de Layouts Dinámicos (CardLayout).
 */
public class VentanaPago extends JDialog {

    private IGestorReserva gestorReserva;
    private Reserva reservaAPagar;

    // Componentes para CardLayout
    private JPanel panelCamposDinamicos;
    private CardLayout cardLayout;
    private JComboBox<String> comboMedioPago;

    // Campos específicos
    private JTextField txtNumTarjeta, txtCBU;
    private final String P_TARJETA = "TARJETA";
    private final String P_TRANSF = "TRANSFERENCIA";

    public VentanaPago(JFrame owner, IGestorReserva gr, Reserva reserva) {
        super(owner, "Pago", true);
        this.gestorReserva = gr;
        this.reservaAPagar = reserva;

        // ... (Configuración básica de ventana) ...

        // [TPI D.3] Implementación de CardLayout para cambiar campos según el medio de pago.
        // [DEFENSA] "Usamos CardLayout para mostrar u ocultar los campos de Tarjeta o CBU
        // dinámicamente según lo que elija el usuario en el ComboBox."
        cardLayout = new CardLayout();
        panelCamposDinamicos = new JPanel(cardLayout);

        // Construcción de paneles (Omitida por brevedad: GridBagLayout)
        // panelCamposDinamicos.add(panelTarjeta, P_TARJETA);
        // panelCamposDinamicos.add(panelTransf, P_TRANSF);

        configurarEventos();
    }

    private void configurarEventos() {
        // [TPI D.2] Evento ItemListener para el cambio dinámico de paneles.
        comboMedioPago.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                cardLayout.show(panelCamposDinamicos, (String) e.getItem());
            }
        });

        // Evento de Pago
        btnConfirmar.addActionListener(e -> procesarPago());
    }

    private void procesarPago() {
        try {
            String medio = (String) comboMedioPago.getSelectedItem();

            if (P_TARJETA.equals(medio)) {
                // Validación GUI
                if (txtNumTarjeta.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Ingrese número tarjeta.");
                    return;
                }
                // [GRASP Controller] Delegación al Experto (GestorReserva)
                // El Gestor sabe crear el objeto PagoTarjeta y calcular comisiones.
                gestorReserva.confirmarPagoTarjeta(reservaAPagar, txtNumTarjeta.getText(), 1, "Visa");

            } else {
                gestorReserva.confirmarPagoEfectivo(reservaAPagar, true);
            }

            JOptionPane.showMessageDialog(this, "Pago Exitoso.");
            dispose();

        } catch (ReservaException | IOException ex) {
            // [TPI B.3] Excepción de Negocio (ej: error al guardar estado confirmado)
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}