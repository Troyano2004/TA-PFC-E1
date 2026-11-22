package ec.edu.uteq.sic.propuestas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import ec.edu.uteq.sic.propuestas.cliente.UsuariosClient;

/**
 * Deja en el log el puerto propio y la dirección de ms-usuarios que se va a usar.
 */
@Component
public class ArranqueListener {

    private static final Logger log = LoggerFactory.getLogger(ArranqueListener.class);

    private final PropuestaRepositorio repositorio;
    private final UsuariosClient usuarios;

    public ArranqueListener(PropuestaRepositorio repositorio, UsuariosClient usuarios) {
        this.repositorio = repositorio;
        this.usuarios = usuarios;
    }

    @EventListener
    public void listo(ApplicationReadyEvent evento) {
        var entorno = evento.getApplicationContext().getEnvironment();
        String puerto = entorno.getProperty("local.server.port", entorno.getProperty("server.port"));
        log.info("ms-propuestas listo en http://localhost:{}/api/propuestas ({} propuestas en memoria)",
                puerto, repositorio.total());
        log.info("ms-usuarios se consulta en {}", usuarios.baseUrl());
    }
}
