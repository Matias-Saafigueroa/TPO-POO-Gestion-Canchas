package Clases;

import interfaces.IGestorCancha;

import java.util.*;


public class GestorCancha implements IGestorCancha {

    private static Map<Integer, Cancha> canchas;

    public GestorCancha() {
        this.canchas = new HashMap<>();
    }

    public GestorCancha(Map<Integer, Cancha> canchas) {
        this.canchas = canchas;
    }

    @Override
    public static Cancha agregarCancha(Cancha cancha) {
        if (cancha == null) {
            throw new IllegalArgumentException("La cancha no puede ser nula.");
        }
        this.canchas.put(cancha.getIdCancha(), cancha);
        return cancha;
    }
    @Override
    public void eliminarCancha(int id) {
        if (this.canchas.containsKey(id)) {
            this.canchas.remove(id);
            System.out.println(" Cancha eliminada correctamente (ID: " + id + ")");
        } else {
            System.out.println(" No se encontró ninguna cancha con el ID: " + id);
        }
    }

    @Override
    public Optional<Cancha> buscarCancha(int id) {
        return Optional.ofNullable(this.canchas.get(id));
    }


    @Override
    public static List<Cancha> obtenerCanchas() {
        return new ArrayList<>(this.canchas.values());
    }

    public static List<Cancha> mostrarCanchas() {
        if (canchas.isEmpty()) {
            System.out.println(" No hay canchas registradas.");
            return null;
        }

        System.out.println(" Lista de canchas:");
        for (Cancha c : canchas.values()) {
            System.out.println("- ID: " + c.getIdCancha() + " | Nombre: " + c.getNombre() + " | Tipo: " + c.getTipoCancha());
        }
        return null;
    }
}
