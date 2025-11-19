package Excepciones;

/**
 * [TPI B.3] Excepción Controlada Propia del Negocio.
 * Se usa para indicar una violación de regla de negocio: DNI duplicado.
 *
 * [INFO] Al extender de 'Exception', creamos una "Checked Exception".
 * Esto obliga a quien use tu método a poner un bloque 'try-catch'.
 * Si extendiéramos de 'RuntimeException', el try-catch no sería obligatorio.
 */
public class ClienteYaExisteException extends Exception {
    public ClienteYaExisteException(String message) {
        super(message); // [INFO] Pasa el mensaje al constructor de la clase padre (Exception)
    }
}