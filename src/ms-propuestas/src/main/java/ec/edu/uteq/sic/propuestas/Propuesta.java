package ec.edu.uteq.sic.propuestas;

import java.time.LocalDate;

/**
 * Propuesta de tema de titulación tal como la guarda este servicio.
 * Solo tiene los id del estudiante y del tutor; sus datos los tiene ms-usuarios.
 */
public record Propuesta(Long id, String titulo, Estado estado, LocalDate fechaEnvio,
                        Long idEstudiante, Long idTutor) {

    public enum Estado {
        ENVIADA,
        EN_REVISION,
        APROBADA,
        RECHAZADA
    }
}
