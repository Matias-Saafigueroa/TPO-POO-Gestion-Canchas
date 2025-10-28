package Clases;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reserva {
    private int idReserva;
    private Cliente cliente;
    private Cancha cancha;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private double montoTotal;
    private Pago pago;
}
