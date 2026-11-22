package ec.edu.uteq.sic.propuestas;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import ec.edu.uteq.sic.propuestas.Propuesta.Estado;

/**
 * Repositorio en memoria con propuestas de prueba. No hay base de datos en E1.
 */
@Repository
public class PropuestaRepositorio {

    private final Map<Long, Propuesta> propuestas = new ConcurrentHashMap<>();

    public PropuestaRepositorio() {
        guardar(new Propuesta(1L,
                "Aplicación móvil para el seguimiento de prácticas preprofesionales",
                Estado.EN_REVISION, LocalDate.of(2026, 11, 10), 1L, 10L));
        guardar(new Propuesta(2L,
                "Sistema de alerta temprana de deserción estudiantil con aprendizaje automático",
                Estado.APROBADA, LocalDate.of(2026, 10, 28), 2L, 11L));
        guardar(new Propuesta(3L,
                "Plataforma web para la reserva de laboratorios de la facultad",
                Estado.ENVIADA, LocalDate.of(2026, 11, 18), 3L, 10L));
    }

    private void guardar(Propuesta propuesta) {
        propuestas.put(propuesta.id(), propuesta);
    }

    public Optional<Propuesta> buscarPorId(Long id) {
        return Optional.ofNullable(propuestas.get(id));
    }

    public List<Propuesta> listar() {
        return propuestas.values().stream()
                .sorted(Comparator.comparing(Propuesta::id))
                .toList();
    }

    public int total() {
        return propuestas.size();
    }
}
