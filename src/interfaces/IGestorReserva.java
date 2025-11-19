package interfaces;

import Clases.Reserva;
import Excepciones.ReservaException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * [TPI A.2] Contrato para la lógica de reservas.
 * Define todas las operaciones complejas que el sistema puede realizar sobre las reservas.
 */
public interface IGestorReserva {

    void registrarReserva(Reserva reserva) throws ReservaException, IOException;
    void confirmarReserva(int idReserva, boolean pagoRealizado) throws ReservaException, IOException;
    void cancelarReserva(int idReserva) throws ReservaException, IOException;

    // [PATRÓN] Expert: Estos métodos reciben los datos crudos (String, int)
    // porque el Gestor es quien sabe cómo transformarlos en objetos 'Pago'.
    void confirmarPagoTarjeta(Reserva reserva, String num, int cuotas, String entidad) throws ReservaException, IOException;
    void confirmarPagoEfectivo(Reserva reserva, boolean abonadoEnCaja) throws ReservaException, IOException;

    boolean validarDisponibilidad(int idCancha, LocalDate fecha, LocalTime horaInicio);
    boolean validarReserva(int idReserva);

    Reserva buscarReserva(int idReserva);
    List<Reserva> historialReservaCliente(int idCliente);
    List<Reserva> obtenerReservas();

    int asignarIdReserva();
}