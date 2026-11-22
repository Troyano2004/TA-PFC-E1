package ec.edu.uteq.sic.propuestas.cliente;

/**
 * ms-usuarios no respondió a tiempo o devolvió un error del servidor.
 */
public class UsuariosNoDisponibleException extends RuntimeException {

    public UsuariosNoDisponibleException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
