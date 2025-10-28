package interfaces;

public interface IGestionArchivos {
    //metodos crud
    public void crearArchivo(String ruta);
    public void leerArchivo(String ruta);
    public void actualizarArchivo(String ruta);
    public void borrarArchivo(String ruta);


}
