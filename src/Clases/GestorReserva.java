package Clases;

import interfaces.IGestorReserva;
import interfaces.IGestorCancha;
import interfaces.IGestorRegistro;
import interfaces.IGestionArchivos;
import Excepciones.ReservaException; // (B.3) Importar excepción
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * (A.4) EXPERTO (GRASP): Esta clase es la experta en la lógica de negocio de Reservas.
 * (A.4) CONTROLADOR (GRASP): Coordina los pagos y estados de las reservas.
 */
public class GestorReserva implements IGestorReserva {

    private ArrayList<Reserva> listaReservas;
    private IGestionArchivos gestorDeArchivos;

    // (A.4 / DIP) Dependencia de las interfaces (abstracciones)
    private IGestorRegistro gestorRegistro;
    private IGestorCancha gestorCancha;

    // (C.1) Ruta del archivo de persistencia
    private static final String RUTA_RESERVAS = "HistorialReservas.csv";

    // (A.4 / C.3) Inyección de Dependencias en el constructor
    public GestorReserva(IGestorRegistro gestorRegistro, IGestorCancha gestorCancha) {
        this.gestorDeArchivos = new GestionArchivosCSV();
        this.listaReservas = new ArrayList<>();
        this.gestorRegistro = gestorRegistro; // Guardamos la referencia
        this.gestorCancha = gestorCancha;     // Guardamos la referencia

        try {
            // (B.4) Cargamos los datos desde el CSV al iniciar
            cargarReservas();
        } catch (IOException | NumberFormatException e) {
            System.err.println("ERROR CRÍTICO: No se pudieron cargar las reservas: " + e.getMessage());
        }
    }

    @Override
    public void registrarReserva(Reserva reserva) throws ReservaException, IOException {
        // --- CORRECCIÓN 1 ---
        // El 2do parámetro era erróneo. Debe ser reserva.getFecha()
        if (!validarDisponibilidad(reserva.getCancha().getIdCancha(), reserva.getFecha(), reserva.getHoraInicio())) {
            throw new ReservaException("La cancha ya está ocupada en ese horario.");
        }

        // (A.1) Seteamos el estado inicial (Polimorfismo de Estados)
        reserva.setEstado(EstadoReserva.PENDIENTE);
        this.listaReservas.add(reserva);
        guardarReservas(); // (C.1) Persistimos el cambio
    }

    @Override
    public void confirmarReserva(int idReserva, boolean pagoRealizado) throws ReservaException, IOException {
        Reserva reserva = buscarReserva(idReserva);
        if (reserva == null) {
            throw new ReservaException("La reserva " + idReserva + " no existe.");
        }

        if (pagoRealizado) {
            reserva.setEstado(EstadoReserva.CONFIRMADA); // Corregido a 'CONFIRMADA'
            guardarReservas();
        } else {
            reserva.setEstado(EstadoReserva.PENDIENTE);
            guardarReservas();
        }
    }

    @Override
    public void cancelarReserva(int idReserva) throws ReservaException, IOException {
        Reserva reserva = buscarReserva(idReserva);
        if (reserva == null) {
            throw new ReservaException("La reserva " + idReserva + " no existe.");
        }

        // Lógica de negocio: no se puede cancelar una reserva pasada
        if (reserva.getFecha().isBefore(LocalDate.now())) {
            throw new ReservaException("No se puede cancelar una reserva pasada.");
        }

        reserva.setEstado(EstadoReserva.CANCELADA);
        guardarReservas();
    }

    // --- Lógica de Pago (El "Experto") ---

    @Override
    public void confirmarPagoTarjeta(Reserva reserva, String numeroTarjeta, int cuotas, String entidad) throws ReservaException, IOException {
        // (A.4) El Gestor es el Experto que sabe "crear" el pago
        int idPago = generarIdPagoUnico();

        // (A.3) Composición: Creamos el objeto Pago que "pertenece" a la Reserva
        PagoTarjeta pago = new PagoTarjeta(idPago, reserva.getIdReserva(), LocalDate.now(), numeroTarjeta, cuotas, entidad);

        // (Opcional: lógica de negocio)
        // double comision = pago.aplicarComision();
        // reserva.setMontoTotal(reserva.getMontoTotal() + comision);

        reserva.setPago(pago); // La reserva ahora "posee" al pago

        // Finalmente, confirmamos la reserva (lógica centralizada)
        this.confirmarReserva(reserva.getIdReserva(), true);
    }

    @Override
    public void confirmarPagoEfectivo(Reserva reserva, boolean abonadoEnCaja) throws ReservaException, IOException {
        int idPago = generarIdPagoUnico();

        // (A.3) Composición
        PagoEfectivo pago = new PagoEfectivo(idPago, reserva.getIdReserva(), LocalDate.now(), abonadoEnCaja);

        reserva.setPago(pago);
        this.confirmarReserva(reserva.getIdReserva(), true);
    }

    // --- Métodos de Consulta ---

    @Override
    public boolean validarDisponibilidad(int idCancha, LocalDate fecha, LocalTime horaInicio) {
        // Lógica de negocio: chequear si ya existe una reserva en listaReservas
        // que coincida con esos 3 parámetros y NO esté CANCELADA.
        return listaReservas.stream()
                .noneMatch(r ->
                        // --- CORRECCIÓN 2 ---
                        // 'r' es una Reserva, debe llamar a r.getCancha().getIdCancha()
                        r.getCancha().getIdCancha() == idCancha &&
                                r.getFecha().equals(fecha) &&
                                r.getHoraInicio().equals(horaInicio) &&
                                r.getEstado() != EstadoReserva.CANCELADA
                );
    }

    @Override
    public boolean validarReserva(int idReserva) {
        Reserva r = buscarReserva(idReserva);
        // (Corregido a 'CONFIRMADA')
        return (r != null && r.getEstado() == EstadoReserva.CONFIRMADA);
    }

    @Override
    public Reserva buscarReserva(int idReserva) {
        return listaReservas.stream()
                .filter(r -> r.getIdReserva() == idReserva)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Reserva> historialReservaCliente(int idCliente) {
        return listaReservas.stream()
                .filter(r ->
                        // --- CORRECCIÓN 3 ---
                        // 'r' es una Reserva, debe llamar a r.getCliente().getIdCliente()
                        r.getCliente().getIdCliente() == idCliente
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<Reserva> obtenerReservas() {
        return this.listaReservas;
    }

    /**
     * (A.4) EXPERTO (GRASP): Este gestor es el experto en saber
     * cuál es el próximo ID de reserva disponible.
     * @return Un ID numérico único para una nueva reserva.
     */
    @Override
    public int asignarIdReserva() {
        if (listaReservas.isEmpty()) {
            return 1; // Si no hay reservas, empieza en 1
        }

        // Devuelve el ID más alto en la lista actual + 1
        return listaReservas.stream()
                .mapToInt(Reserva::getIdReserva)
                .max()
                .orElse(0) + 1;
    }

    // --- MÉTODOS DE PERSISTENCIA PRIVADOS (IMPLEMENTADOS 100%) ---

    private int generarIdPagoUnico() {
        // Lógica simple para generar un ID único de pago
        // (Esto debería mejorarse en un sistema real)
        return (int) (System.currentTimeMillis() % 10000);
    }

    /**
     * (C.1) Carga la lista de reservas desde el archivo CSV.
     * (A.3) Resuelve la Agregación conectando IDs con los Gestores.
     */
    private void cargarReservas() throws IOException, NumberFormatException {
        ArrayList<String> lineas = gestorDeArchivos.leerArchivo(RUTA_RESERVAS);

        for (String linea : lineas) {
            if (linea.isEmpty()) continue;
            String[] datos = linea.split(";");
            if (datos.length < 8) continue; // Validar formato

            try {
                // Formato CSV Asumido:
                // 0:idReserva; 1:idCliente; 2:idCancha; 3:fecha; 4:horaInicio; 5:horaFin; 6:montoTotal; 7:estado

                int idReserva = Integer.parseInt(datos[0]);
                int idCliente = Integer.parseInt(datos[1]);
                int idCancha = Integer.parseInt(datos[2]);

                // (A.3) Resolvemos la Agregación usando los gestores inyectados
                Cliente cliente = (Cliente) gestorRegistro.buscarCliente(idCliente);
                Cancha cancha = gestorCancha.buscarCancha(idCancha);

                // Si el cliente o la cancha fueron eliminados, no cargamos la reserva
                if (cliente == null || cancha == null) {
                    System.err.println("Omitiendo reserva (ID: " + idReserva + ") por datos huérfanos.");
                    continue;
                }

                LocalDate fecha = LocalDate.parse(datos[3]);
                LocalTime horaInicio = LocalTime.parse(datos[4]);
                LocalTime horaFin = LocalTime.parse(datos[5]);
                double montoTotal = Double.parseDouble(datos[6]);
                EstadoReserva estado = EstadoReserva.valueOf(datos[7]);

                // Asumo que tienes un constructor en Reserva que acepta estos datos
                Reserva reserva = new Reserva(idReserva, cliente, cancha, fecha, horaInicio, horaFin, montoTotal, null); // Pago se carga después
                reserva.setEstado(estado); // Seteamos el estado guardado

                this.listaReservas.add(reserva);

            } catch (DateTimeParseException e) {
                System.err.println("Error de formato Fecha/Hora en reservas.csv (se saltea): " + linea);
            } catch (Exception e) {
                System.err.println("Error desconocido al parsear reserva (se saltea): " + linea);
            }
        }
    }

    /**
     * (C.1) Guarda la lista de reservas en memoria al archivo CSV.
     */
    private void guardarReservas() throws IOException {
        ArrayList<String> lineas = new ArrayList<>();

        for (Reserva reserva : this.listaReservas) {
            // (A.1) Polimorfismo: Llama al 'toCSVString()' de Reserva
            // Asumimos que toCSVString() existe en Reserva y guarda los IDs
            lineas.add(reserva.toCSVString());
        }

        gestorDeArchivos.escribirArchivo(RUTA_RESERVAS, lineas);
    }

}