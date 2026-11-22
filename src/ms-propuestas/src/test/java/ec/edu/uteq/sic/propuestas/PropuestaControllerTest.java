package ec.edu.uteq.sic.propuestas;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withException;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.ConnectException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestClient;

import ec.edu.uteq.sic.propuestas.cliente.UsuariosClient;

/**
 * Prueba ms-propuestas sin levantar ms-usuarios: MockRestServiceServer contesta en su lugar.
 */
class PropuestaControllerTest {

    private static final String USUARIOS = "http://usuarios.prueba";

    private MockRestServiceServer usuariosFalso;
    private MockMvc mvc;

    @BeforeEach
    void preparar() {
        RestClient.Builder builder = RestClient.builder().baseUrl(USUARIOS);
        usuariosFalso = MockRestServiceServer.bindTo(builder).build();
        UsuariosClient cliente = new UsuariosClient(builder.build(), USUARIOS);
        PropuestaService servicio = new PropuestaService(new PropuestaRepositorio(), cliente);
        mvc = MockMvcBuilders.standaloneSetup(new PropuestaController(servicio)).build();
    }

    @Test
    void combinaLaPropuestaConLosDatosDeMsUsuarios() throws Exception {
        usuariosFalso.expect(requestTo(USUARIOS + "/api/usuarios/1"))
                .andRespond(withSuccess("""
                        {"id":1,"nombres":"Estudiante Uno","correo":"e1@uteq.example","rol":"ESTUDIANTE","carrera":"Software"}
                        """, MediaType.APPLICATION_JSON));
        usuariosFalso.expect(requestTo(USUARIOS + "/api/usuarios/10"))
                .andRespond(withSuccess("""
                        {"id":10,"nombres":"Docente Diez","correo":"d10@uteq.example","rol":"DOCENTE","carrera":"Software"}
                        """, MediaType.APPLICATION_JSON));

        mvc.perform(get("/api/propuestas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("EN_REVISION"))
                .andExpect(jsonPath("$.estudiante.nombres").value("Estudiante Uno"))
                .andExpect(jsonPath("$.tutor.rol").value("DOCENTE"));
        usuariosFalso.verify();
    }

    @Test
    void siElTutorNoExisteElCampoQuedaVacio() throws Exception {
        usuariosFalso.expect(requestTo(USUARIOS + "/api/usuarios/1"))
                .andRespond(withSuccess("""
                        {"id":1,"nombres":"Estudiante Uno","correo":"e1@uteq.example","rol":"ESTUDIANTE"}
                        """, MediaType.APPLICATION_JSON));
        usuariosFalso.expect(requestTo(USUARIOS + "/api/usuarios/10"))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        mvc.perform(get("/api/propuestas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tutor").doesNotExist());
    }

    @Test
    void responde503SiMsUsuariosNoContesta() throws Exception {
        usuariosFalso.expect(requestTo(USUARIOS + "/api/usuarios/1"))
                .andRespond(withException(new ConnectException("Connection refused")));

        mvc.perform(get("/api/propuestas/1"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.error").value("ms-usuarios no está disponible"));
    }

    @Test
    void responde404SiLaPropuestaNoExiste() throws Exception {
        mvc.perform(get("/api/propuestas/999"))
                .andExpect(status().isNotFound());
    }
}
