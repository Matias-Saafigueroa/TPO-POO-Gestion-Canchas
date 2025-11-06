package Clases;

import interfaces.IGestorCancha;
import interfaces.IGestionArchivos;
import Excepciones.CanchaException; // (B.3) Importar excepción
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional; // Aunque no lo usamos en la versión final, es bueno importarlo si se usa 'findFirst()'

public class GestorCancha implements IGestorCancha {

    private ArrayList<Cancha> listaCanchas;
    private IGestionArchivos gestorDeArchivos;
    private static final String RUTA_CANCHAS = "canchas.csv";

    public GestorCancha() {
        this.gestorDeArchivos = new GestionArchivosCSV();
        this.listaCanchas = new ArrayList<>();
        try {
            // (B.4) Cargamos los datos al iniciar
            cargarCanchas();
        } catch (IOException e) {
            System.err.println("ERROR CRÍTICO (IO): No se pudieron cargar las canchas: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("ERROR CRÍTICO (Formato): El archivo canchas.csv está corrupto: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("ERROR CRÍTICO (Inesperado): " + e.getMessage());
        }
    }

    // --- MÉTODOS DE LA INTERFAZ (IMPLEMENTADOS) ---

    @Override
    public void agregarCancha(Cancha cancha) throws CanchaException, IOException {
        if (!validarDatos(cancha.getNombre())) {
            // (B.3) Lanzamos la excepción de negocio
            throw new CanchaException("El nombre '" + cancha.getNombre() + "' ya está en uso.");
        }
        this.listaCanchas.add(cancha);
        guardarCanchas(); // Guardamos el estado actualizado
    }

    @Override
    public void eliminarCancha(int idCancha) throws CanchaException, IOException {
        boolean eliminado = this.listaCanchas.removeIf(c -> c.getIdCancha() == idCancha);

        if (!eliminado) {
            // (B.3) Lanzamos la excepción si no se pudo eliminar
            throw new CanchaException("No se encontró la cancha con ID: " + idCancha);
        }
        guardarCanchas(); // Guardamos el estado actualizado
    }

    @Override
    public void asignarPrecio(int idCancha, double precio) throws CanchaException, IOException {
        Cancha cancha = buscarCancha(idCancha);

        if (cancha != null) {
            // Implementación 100%: Asumimos que la clase Cancha tiene este método
            cancha.setPrecioPorHora(precio);
            guardarCanchas(); // Guardamos el estado actualizado
        } else {
            // (B.3) Lanzamos la excepción si no se encontró
            throw new CanchaException("No se encontró la cancha con ID: " + idCancha);
        }
    }

    // --- Métodos de Consulta (No cambian) ---

    @Override
    public Cancha buscarCancha(int idCancha) {
        // Usamos la API de Streams para buscar
        return this.listaCanchas.stream()
                .filter(c -> c.getIdCancha() == idCancha)
                .findFirst()
                .orElse(null); // Devuelve null si no se encuentra
    }

    @Override
    public List<Cancha> obtenerCanchas() {
        // Devuelve una copia o la lista (depende de si quieres protegerla)
        return this.listaCanchas;
    }

    @Override
    public boolean validarDatos(String nombre) {
        // Valida que el nombre sea único (ignorando mayúsculas/minúsculas)
        return this.listaCanchas.stream()
                .noneMatch(c -> c.getNombre().equalsIgnoreCase(nombre));
    }

    @Override
    public int asignarId() {
        // Lógica simple para generar un ID único
        if (listaCanchas.isEmpty()) {
            return 1;
        }
        // Devuelve el ID más alto + 1
        return listaCanchas.get(listaCanchas.size() - 1).getIdCancha() + 1;
    }

    // --- MÉTODOS DE PERSISTENCIA PRIVADOS (IMPLEMENTADOS 100%) ---

    /**
     * Carga la lista de canchas desde el archivo CSV.
     * (B.4) Maneja errores de formato.
     */
    private void cargarCanchas() throws IOException, NumberFormatException {
        ArrayList<String> lineas = gestorDeArchivos.leerArchivo(RUTA_CANCHAS);

        for (String linea : lineas) {
            if (linea.isEmpty()) continue; // Saltea líneas vacías

            String[] datos = linea.split(";");
            if (datos.length < 6) continue; // Saltea líneas corruptas

            try {
                // 1. Parsear datos comunes
                int id = Integer.parseInt(datos[0]);
                String nombre = datos[1];
                String superficie = datos[2];
                double precio = Double.parseDouble(datos[3]);
                TipoCancha tipo = TipoCancha.valueOf(datos[4].toUpperCase()); // (B.2) Uso de Enum
                String datoEspecifico = datos[5];

                Cancha cancha = null;

                // 2. (A.1) Polimorfismo: Decidir qué clase instanciar
                switch (tipo) {
                    case FUTBOL:
                        int cantJugadores = Integer.parseInt(datoEspecifico);
                        cancha = new CanchaFutbol(id,tipo, superficie,nombre, precio, cantJugadores);
                        break;
                    case PADEL:
                        String tipoPared = datoEspecifico;
                        cancha = new CanchaPadel(id,tipo, superficie,nombre, precio, tipoPared);
                        break;
                    case TENIS:
                        boolean esDoble = Boolean.parseBoolean(datoEspecifico);
                        cancha = new CanchaTenis(id,tipo, superficie,nombre, precio, esDoble);
                        break;
                }

                if (cancha != null) {
                    this.listaCanchas.add(cancha);
                }

            } catch (NumberFormatException e) {
                System.err.println("Error de formato numérico en línea (se saltea): " + linea);
            } catch (IllegalArgumentException e) {
                System.err.println("Error de tipo de cancha (se saltea): " + linea);
            }
        }
    }

    /**
     * (C.2) Guarda la lista de canchas en memoria al archivo CSV.
     */
    private void guardarCanchas() throws IOException {
        ArrayList<String> lineas = new ArrayList<>();

        // (A.1) Polimorfismo: Llama al 'toCSVString()' específico de cada subclase
        for (Cancha cancha : this.listaCanchas) {
            lineas.add(cancha.toCSVString());
        }

        gestorDeArchivos.escribirArchivo(RUTA_CANCHAS, lineas);
    }
}