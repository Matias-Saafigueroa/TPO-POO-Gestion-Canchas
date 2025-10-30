package Clases;

import interfaces.IGestorReserva;

import java.io.*;

public class GestorReserva implements IGestorReserva {

        public boolean validarIdEnArchivo(int id) throws IOException {
            // Usamos try-with-resources para que el archivo se cierre automáticamente
            try (BufferedReader reader = new BufferedReader(new FileReader(RUTA_ARCHIVO_RESERVAS))) {
                String linea;

                // Leemos el archivo línea por línea
                while ((linea = reader.readLine()) != null) {
                    // Dividimos la línea por la coma para obtener los datos
                    String[] partes = linea.split(",");

                    // Verificamos que la línea no esté vacía y tenga al menos el ID
                    if (partes.length > 0) {
                        try {
                            // Convertimos la primera parte (el ID) a número
                            int idEnArchivo = Integer.parseInt(partes[0].trim());

                            // Si el ID del archivo es igual al que buscamos, ¡lo encontramos!
                            if (idEnArchivo == id) {
                                return true; // El ID existe, terminamos la búsqueda.
                            }
                        } catch (NumberFormatException e) {
                            // Si la primera parte no es un número, ignoramos esa línea.
                            System.err.println("Advertencia: Línea con formato incorrecto saltada -> " + linea);
                        }
                    }
                }
            }



            public boolean confirmarReserva(int id,boolean pagoRealizado) {
                // 1. Validar la lógica de negocio primero
                if (!pagoRealizado) {
                    System.out.println(" Pago no realizado para la reserva " + id + ". No se puede confirmar.");
                    return false;
                }

                File archivoOriginal = new File(RUTA_ARCHIVO);
                File archivoTemp = new File("reservas_temp.txt");
                boolean reservaEncontrada = false;

                // 2. Leer el archivo y escribir los cambios en uno temporal
                try (BufferedReader reader = new BufferedReader(new FileReader(archivoOriginal));
                     BufferedWriter writer = new BufferedWriter(new FileWriter(archivoTemp))) {

                    String lineaActual;
                    while ((lineaActual = reader.readLine()) != null) {
                        String[] partes = lineaActual.split(",");
                        int idActual = Integer.parseInt(partes[0].trim());

                        // 3. Buscar la reserva por su ID y modificarla
                        if (idActual == id) {
                            reservaEncontrada = true;
                            partes[4] = "CONFIRMADA"; // Cambiamos el estado
                            lineaActual = String.join(",", partes); // Volvemos a unir la línea
                        }

                        writer.write(lineaActual + System.lineSeparator());
                    }
                } catch (IOException e) {
                    System.err.println("Error al procesar el archivo: " + e.getMessage());
                    return false;
                }

                // Si el ID no se encontró, no hay nada que cambiar
                if (!reservaEncontrada) {
                    System.out.println("No se encontró ninguna reserva con el ID: " + id);
                    archivoTemp.delete(); // Borramos el archivo temporal inútil
                    return false;
                }

                // 4. Reemplazar el archivo original con el temporal para guardar los cambios
                if (archivoOriginal.delete()) {
                    archivoTemp.renameTo(archivoOriginal);
                    System.out.println("✅ Reserva " + id + " confirmada y guardada correctamente.");
                } else {
                    System.err.println("Error crítico: no se pudo actualizar el archivo de reservas.");
                    return false;
                }
                return true;
            }
    @Override
    public boolean cancelarReserva(int id) {
        return false;
    }

    @Override
    public boolean validarReserva(int id) {
        return false;
    }

    @Override
    public Reserva mostrarReserva(int id) {
        return null;
    }

    @Override
    public void historialReservas() {

    }
}
