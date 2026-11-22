package ec.edu.uteq.sic.propuestas.cliente;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/**
 * Cliente REST de ms-usuarios. Es la única clase de ms-propuestas que sabe
 * dónde está ms-usuarios y cómo se le pregunta.
 */
public class UsuariosClient {

    private static final Logger log = LoggerFactory.getLogger(UsuariosClient.class);

    private final RestClient restClient;
    private final String baseUrl;

    public UsuariosClient(RestClient restClient, String baseUrl) {
        this.restClient = restClient;
        this.baseUrl = baseUrl;
    }

    public Optional<UsuarioDto> obtener(Long id) {
        long inicio = System.nanoTime();
        try {
            UsuarioDto usuario = restClient.get()
                    .uri("/api/usuarios/{id}", id)
                    .retrieve()
                    .body(UsuarioDto.class);
            log.info("GET {}/api/usuarios/{} -> 200 en {} ms", baseUrl, id, milisDesde(inicio));
            return Optional.ofNullable(usuario);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
                log.info("GET {}/api/usuarios/{} -> 404 en {} ms", baseUrl, id, milisDesde(inicio));
                return Optional.empty();
            }
            throw new UsuariosNoDisponibleException("ms-usuarios respondió " + e.getStatusCode(), e);
        } catch (RestClientException e) {
            log.warn("GET {}/api/usuarios/{} falló en {} ms: {}", baseUrl, id, milisDesde(inicio), e.getMessage());
            throw new UsuariosNoDisponibleException("ms-usuarios no responde en " + baseUrl, e);
        }
    }

    public String baseUrl() {
        return baseUrl;
    }

    private static long milisDesde(long inicioNanos) {
        return (System.nanoTime() - inicioNanos) / 1_000_000;
    }
}
