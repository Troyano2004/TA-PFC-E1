package ec.edu.uteq.sic.usuarios;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Deja en el log el puerto y cuántos usuarios hay cargados, para comprobar el arranque.
 */
@Component
public class ArranqueListener {

    private static final Logger log = LoggerFactory.getLogger(ArranqueListener.class);

    private final UsuarioRepositorio repositorio;

    public ArranqueListener(UsuarioRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @EventListener
    public void listo(ApplicationReadyEvent evento) {
        var entorno = evento.getApplicationContext().getEnvironment();
        String puerto = entorno.getProperty("local.server.port", entorno.getProperty("server.port"));
        log.info("ms-usuarios listo en http://localhost:{}/api/usuarios ({} usuarios en memoria)",
                puerto, repositorio.total());
    }
}
