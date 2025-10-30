package interfaces;

import Clases.Reserva;

import java.util.List;

public interface ICliente {
    public boolean realizarReserva(Reserva reserva);
    public void cancelarReserva(Reserva reserva);
    public void realizarPago();//faltaria ver que parametros pasar aca y que devolver
    public void cancelarPago();//faltaria ver que parametros pasar aca y que devolver
    public List<Reserva> historialReservaCliente(int id);//aca lo que vamos a hacer es recorrer el historial de reservas guardado en gestor reservas -->(sigue abajo)
    //-->> y por cada reserva que tenga el id del cliente lo vamos a almacenar en esta lista


}
