package vistas;

import Clases.Cancha;
import javax.swing.*;
import java.awt.*;

/**
 * [TPI D.3] Componente Personalizado: Renderizador de Lista.
 * [DEFENSA] "En lugar de mostrar el toString() por defecto, creamos este renderer
 * para formatear cómo se ven las canchas en los JList, mejorando la usabilidad."
 */
public class CanchaListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value instanceof Cancha) {
            Cancha c = (Cancha) value;
            setText(c.getNombre() + " (" + c.getTipoCancha() + ") - $" + c.getPrecioPorHora());
        }
        return this;
    }
}