package interfaces;

import Clases.Cancha;
import Clases.Persona;

public interface IAdministrador {
    public void crearCancha(Cancha cancha);
    public void eliminarCancha(Cancha cancha);
    public void asignarPrecio(Cancha cancha);

}
