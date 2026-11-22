package ec.edu.uteq.sic.usuarios;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import ec.edu.uteq.sic.usuarios.Usuario.Rol;

/**
 * Repositorio en memoria con datos de prueba. No hay base de datos en E1.
 */
@Repository
public class UsuarioRepositorio {

    private static final String CARRERA = "Ingeniería de Software";

    private final Map<Long, Usuario> usuarios = new ConcurrentHashMap<>();

    public UsuarioRepositorio() {
        guardar(new Usuario(1L, "Andrea Paola Cedeño Mora", "acedeno@uteq.example", Rol.ESTUDIANTE, CARRERA));
        guardar(new Usuario(2L, "Luis Fernando Vera Alava", "lvera@uteq.example", Rol.ESTUDIANTE, CARRERA));
        guardar(new Usuario(3L, "María José Torres Quinto", "mtorres@uteq.example", Rol.ESTUDIANTE, CARRERA));
        guardar(new Usuario(10L, "Carlos Alberto Ruiz León", "cruiz@uteq.example", Rol.DOCENTE, CARRERA));
        guardar(new Usuario(11L, "Patricia Elena Mora Bravo", "pmora@uteq.example", Rol.DOCENTE, CARRERA));
        guardar(new Usuario(20L, "Jorge Iván Peña Salazar", "jpena@uteq.example", Rol.COORDINADOR, CARRERA));
    }

    private void guardar(Usuario usuario) {
        usuarios.put(usuario.id(), usuario);
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return Optional.ofNullable(usuarios.get(id));
    }

    public List<Usuario> listar(Rol rol) {
        return usuarios.values().stream()
                .filter(u -> rol == null || u.rol() == rol)
                .sorted(Comparator.comparing(Usuario::id))
                .toList();
    }

    public int total() {
        return usuarios.size();
    }
}
