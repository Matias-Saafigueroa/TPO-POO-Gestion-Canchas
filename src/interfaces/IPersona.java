package interfaces;

/**
 * [TPI A.2] Interfaz base para entidades.
 * [TPI C.1] Define el contrato de persistencia para la jerarquía de personas.
 */
public interface IPersona {
    // [INFO] Todos los métodos en una interface son 'public abstract' implícitamente.
    String toCSVString();
}