package ec.edu.uteq.sic.propuestas;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ec.edu.uteq.sic.propuestas.cliente.UsuariosNoDisponibleException;

@RestController
@RequestMapping("/api/propuestas")
public class PropuestaController {

    private static final Logger log = LoggerFactory.getLogger(PropuestaController.class);

    private final PropuestaService servicio;

    public PropuestaController(PropuestaService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<PropuestaDetalle> listar() {
        log.info("GET /api/propuestas");
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        log.info("GET /api/propuestas/{}", id);
        return servicio.detalle(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "No existe la propuesta " + id)));
    }

    /** Si ms-usuarios está caído, se responde 503 en lugar de una respuesta a medias. */
    @ExceptionHandler(UsuariosNoDisponibleException.class)
    public ResponseEntity<Map<String, String>> usuariosNoDisponible(UsuariosNoDisponibleException e) {
        log.warn("Respondiendo 503: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("error", "ms-usuarios no está disponible", "detalle", e.getMessage()));
    }
}
