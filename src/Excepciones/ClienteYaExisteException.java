package Excepciones;

public class ClienteYaExisteException extends Exception {
    public ClienteYaExisteException(String message) {
        super(message);
    }
}
