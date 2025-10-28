package Clases;

import interfaces.IGestorCancha;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GestorCancha implements IGestorCancha {
    private Map<Integer, Cancha> canchas;

    public GestorCancha(Map<Integer, Cancha> canchas) {
        this.canchas = canchas;
    }
    @Override
    public Cancha agregarCancha(Cancha cancha) {
        this.canchas.put(cancha.getIdCancha(), cancha);
        return cancha;
    }
    @Override
    public void eliminarCancha(int id) {
        if (this.canchas.containsKey(id)) {
            this.canchas.remove(id);
        }
    }
    @Override
    public Optional<Cancha> buscarCancha(int id){
        return Optional.ofNullable(this.canchas.get(id));
    }

}