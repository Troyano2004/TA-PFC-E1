package ec.edu.uteq.sic.propuestas;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Comprueba que el contexto arranca aunque ms-usuarios esté apagado:
 * la conexión se intenta recién cuando llega una petición.
 */
@SpringBootTest
class MsPropuestasApplicationTest {

    @Test
    void elContextoArranca() {
    }
}
