package Clases;

import java.time.LocalDate;

/**
 * (A.1) Requisito TPI: Subclase de Pago
 * (A.4) EXPERTO (GRASP): Esta clase es la experta en calcular
 * comisiones basadas en cuotas o entidad bancaria.
 */
public class PagoTarjeta extends Pago {

    private String numeroTarjeta;
    private int cuotas;
    private String entidadBancaria;

    /**
     * Constructor para ser usado por GestorReserva.
     */
    public PagoTarjeta(int idPago, int idReserva, LocalDate fechaPago, String numeroTarjeta, int cuotas, String entidadBancaria) {
        super(idPago, idReserva, fechaPago);
        this.numeroTarjeta = numeroTarjeta;
        this.cuotas = cuotas;
        this.entidadBancaria = entidadBancaria;
    }

    // --- Getters y Setters ---

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public int getCuotas() {
        return cuotas;
    }

    public void setCuotas(int cuotas) {
        this.cuotas = cuotas;
    }

    public String getEntidadBancaria() {
        return entidadBancaria;
    }

    public void setEntidadBancaria(String entidadBancaria) {
        this.entidadBancaria = entidadBancaria;
    }

    // --- MÉTODOS REQUERIDOS POR EL TPI ---

    /**
     * (A.1) Polimorfismo: Implementación del método abstracto de Pago.
     * (C.1) Persistencia: Genera el string para guardar en un (hipotético) Pagos.csv.
     *
     * Formato CSV Asumido:
     * id;idReserva;fechaPago;TIPO_PAGO;dato_1;dato_2;dato_3
     */
    @Override
    public String toCSVString() {
        return getIdPago() + ";" +
                getIdReserva() + ";" +
                getFechaPago() + ";" +
                "TARJETA;" + // (B.2) Usamos un 'tipo' para el polimorfismo al cargar
                this.numeroTarjeta + ";" +
                this.cuotas + ";" +
                this.entidadBancaria;
    }

    /**
     * (A.4) EXPERTO (GRASP): Lógica de negocio.
     * Esta clase es la experta en calcular su propia comisión.
     * El GestorReserva llamará a este método.
     * * @return El monto a sumar al total (ej: 0.10 para 10%)
     */
    public double aplicarComision() {
        // Lógica de negocio de ejemplo:
        // 3 cuotas = 10% de recargo
        // 6 cuotas = 20% de recargo

        if (this.cuotas == 3) {
            return 0.10; // 10%
        } else if (this.cuotas == 6) {
            return 0.20; // 20%
        }

        return 0.0; // 1 cuota no tiene recargo
    }

    /**
     * toString() mejorado para incluir la información del padre.
     */
    @Override
    public String toString() {
        return "PagoTarjeta {" +
                "idPago=" + getIdPago() +
                ", idReserva=" + getIdReserva() +
                ", fechaPago=" + getFechaPago() +
                ", numeroTarjeta='...'" + // (Ocultamos el número por seguridad)
                ", cuotas=" + cuotas +
                ", entidadBancaria='" + entidadBancaria + '\'' +
                '}';
    }
}