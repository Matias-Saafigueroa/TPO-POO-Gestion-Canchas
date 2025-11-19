package vistas;

import Clases.Cancha;
import Clases.Reserva;
import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * [TPI D.3] Componente Personalizado: Modelo de Tabla.
 * [DEFENSA] "Implementamos un AbstractTableModel para desacoplar la JTable (Vista)
 * de nuestra lista de objetos Reserva (Modelo). Esto permite un control total sobre
 * cómo se muestran los datos."
 */
public class ReservaTableModel extends AbstractTableModel {

    private List<Reserva> reservas;
    private final String[] columnas = {"ID", "Cancha", "Fecha", "Hora", "Estado", "Monto"};

    public ReservaTableModel(List<Reserva> reservas) {
        this.reservas = reservas;
    }

    @Override
    public int getRowCount() { return reservas.size(); }
    @Override
    public int getColumnCount() { return columnas.length; }
    @Override
    public String getColumnName(int col) { return columnas[col]; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Reserva r = reservas.get(rowIndex);
        Cancha c = r.getCancha();

        // Mapeo de columnas a atributos del objeto
        switch (columnIndex) {
            case 0: return r.getIdReserva();
            case 1: return (c != null) ? c.getNombre() : "Eliminada"; // Null-safe
            case 2: return r.getFecha();
            case 3: return r.getHoraInicio();
            case 4: return r.getEstado();
            case 5: return r.getMontoTotal();
            default: return null;
        }
    }

    /**
     * [TPI B.1] Ordenamiento en GUI.
     * [DEFENSA] "Sobrescribimos getColumnClass para que el sorter automático de la JTable
     * sepa que la columna 'Monto' es un número y la ordene numéricamente, no alfabéticamente."
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) return Integer.class;
        if (columnIndex == 5) return Double.class;
        return String.class;
    }

    // Método helper para refrescar datos
    public void setReservas(List<Reserva> nuevas) {
        this.reservas = nuevas;
        fireTableDataChanged(); // Notifica a la vista que se repinte
    }

    public Reserva getReservaAt(int row) {
        return reservas.get(row);
    }
}