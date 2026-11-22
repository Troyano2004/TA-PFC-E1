package ec.edu.uteq.sic.usuarios;

/**
 * Usuario del sistema. En E1 vive solo en memoria; en E3 pasa a su propia base de datos.
 */
public record Usuario(Long id, String nombres, String correo, Rol rol, String carrera) {

    public enum Rol {
        ESTUDIANTE,
        DOCENTE,
        COORDINADOR
    }
}
