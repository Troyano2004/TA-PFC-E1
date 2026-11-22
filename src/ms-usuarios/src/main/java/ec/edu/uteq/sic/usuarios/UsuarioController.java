package ec.edu.uteq.sic.usuarios;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ec.edu.uteq.sic.usuarios.Usuario.Rol;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

    private final UsuarioRepositorio repositorio;

    public UsuarioController(UsuarioRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @GetMapping
    public List<Usuario> listar(@RequestParam(required = false) Rol rol) {
        List<Usuario> resultado = repositorio.listar(rol);
        log.info("GET /api/usuarios rol={} -> {} usuarios", rol, resultado.size());
        return resultado;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        return repositorio.buscarPorId(id)
                .<ResponseEntity<?>>map(usuario -> {
                    log.info("GET /api/usuarios/{} -> 200 {}", id, usuario.nombres());
                    return ResponseEntity.ok(usuario);
                })
                .orElseGet(() -> {
                    log.info("GET /api/usuarios/{} -> 404", id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(Map.of("error", "No existe el usuario " + id));
                });
    }
}
