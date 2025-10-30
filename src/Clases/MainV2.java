package Clases;

public class MainV2 {
    static void main(String[] args) {
        Reserva nuevaReserva = ...; // La reserva ya fue creada
        double precio = ...;       // El precio ya fue calculado

        System.out.println("¿Cómo desea pagar? (1-Tarjeta, 2-Efectivo)");
        int opcionPago = scanner.nextInt(); // Suponiendo que leemos la entrada del usuario

        Pago pagoDeLaReserva; // Declaramos una variable de tipo Pago (la clase padre)

        if (opcionPago == 1) {
            // El usuario eligió Tarjeta. Creamos un objeto de esa clase específica.
            System.out.println("Procesando pago con tarjeta...");
            pagoDeLaReserva = new PagoTarjeta(1, nuevaReserva, precio, LocalDateTime.now(), "1234-...", 3, "Banco X");

            // Aquí se llama a la lógica específica de PagoTarjeta
            ((PagoTarjeta) pagoDeLaReserva).aplicarComision();

        } else {
            // El usuario eligió Efectivo. Creamos el otro tipo de objeto.
            System.out.println("Procesando pago en efectivo...");
            pagoDeLaReserva = new PagoEfectivo(2, nuevaReserva, precio, LocalDateTime.now(), true);
        }

// Finalmente, sin importar cómo se pagó, asociamos el pago a la reserva.
        nuevaReserva.setPago(pagoDeLaReserva);
        System.out.println("Pago registrado. Monto: $" + pagoDeLaReserva.getMonto());

// Ahora sí, podemos proceder a confirmar la reserva en el GestorReserva
        gestorReserva.confirmarReserva(nuevaReserva.getIdReserva());
    }
}
