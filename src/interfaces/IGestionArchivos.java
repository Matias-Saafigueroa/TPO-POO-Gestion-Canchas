package interfaces;

import java.io.IOException;

public interface IGestionArchivos {
    //metodos crud
    public void crearArchivo(String ruta);
    public void leerArchivo(String ruta) throws IOException;
    public void actualizarArchivo(String ruta);
    public void borrarArchivo(String ruta);
    
}
