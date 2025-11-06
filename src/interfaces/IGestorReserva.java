package interfaces;

import Clases.Reserva;
import Excepciones.ReservaException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface IGestorReserva {

    // (B.3) Refactorizados
    void registrarReserva(Reserva reserva) throws ReservaException, IOException;
    void confirmarReserva(int idReserva, boolean pagoRealizado) throws ReservaException, IOException;
    void cancelarReserva(int idReserva) throws ReservaException, IOException;

    void confirmarPagoTarjeta(Reserva reserva, String num, int cuotas, String entidad) throws ReservaException, IOException;
    void confirmarPagoEfectivo(Reserva reserva, boolean abonadoEnCaja) throws ReservaException, IOException;

    // (Sin cambios)
    boolean validarDisponibilidad(int idCancha, LocalDate fecha, LocalTime horaInicio);
    boolean validarReserva(int idReserva);
    Reserva buscarReserva(int idReserva);
    List<Reserva> historialReservaCliente(int idCliente);
    List<Reserva> obtenerReservas();
    /**
     * (A.4) EXPERTO: Genera un ID único para una nueva reserva.
     */
    int asignarIdReserva();
}