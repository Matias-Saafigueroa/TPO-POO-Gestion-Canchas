package interfaces;

import Clases.Cancha;

public interface IGestorCancha {
    public void agregarCancha(Cancha cancha);
    public void eliminarCancha(int id);
    public void buscarCancha(int id);
    public void mostrarCanchas();
}
