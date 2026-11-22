package ec.edu.uteq.sic.propuestas.cliente;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class ClienteConfig {

    @Bean
    UsuariosClient usuariosClient(@Value("${usuarios.url}") String url,
                                  @Value("${usuarios.timeout-ms}") int timeoutMs) {
        // Sin tiempo límite, una caída de ms-usuarios dejaría colgadas las peticiones de ms-propuestas.
        SimpleClientHttpRequestFactory fabrica = new SimpleClientHttpRequestFactory();
        fabrica.setConnectTimeout(timeoutMs);
        fabrica.setReadTimeout(timeoutMs);

        RestClient restClient = RestClient.builder()
                .baseUrl(url)
                .requestFactory(fabrica)
                .build();
        return new UsuariosClient(restClient, url);
    }
}
