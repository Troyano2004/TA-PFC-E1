package ec.edu.uteq.sic.propuestas.cliente;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Lo que ms-propuestas necesita de un usuario. Si ms-usuarios agrega campos, se ignoran.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UsuarioDto(Long id, String nombres, String correo, String rol) {
}
