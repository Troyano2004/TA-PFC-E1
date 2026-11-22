package ec.edu.uteq.sic.propuestas;

import java.time.LocalDate;

import ec.edu.uteq.sic.propuestas.cliente.UsuarioDto;

/**
 * Respuesta combinada: la propuesta sale de este servicio y el estudiante y el tutor, de ms-usuarios.
 */
public record PropuestaDetalle(Long id, String titulo, String estado, LocalDate fechaEnvio,
                               UsuarioDto estudiante, UsuarioDto tutor, String usuariosConsultadosEn) {
}
