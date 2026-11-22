package ec.edu.uteq.sic.propuestas;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ec.edu.uteq.sic.propuestas.cliente.UsuarioDto;
import ec.edu.uteq.sic.propuestas.cliente.UsuariosClient;

@Service
public class PropuestaService {

    private final PropuestaRepositorio repositorio;
    private final UsuariosClient usuarios;

    public PropuestaService(PropuestaRepositorio repositorio, UsuariosClient usuarios) {
        this.repositorio = repositorio;
        this.usuarios = usuarios;
    }

    public Optional<PropuestaDetalle> detalle(Long id) {
        return repositorio.buscarPorId(id).map(this::combinar);
    }

    public List<PropuestaDetalle> listar() {
        return repositorio.listar().stream().map(this::combinar).toList();
    }

    private PropuestaDetalle combinar(Propuesta p) {
        // Dos llamadas síncronas a ms-usuarios. Si el usuario no existe, el campo queda en null.
        UsuarioDto estudiante = usuarios.obtener(p.idEstudiante()).orElse(null);
        UsuarioDto tutor = usuarios.obtener(p.idTutor()).orElse(null);
        return new PropuestaDetalle(p.id(), p.titulo(), p.estado().name(), p.fechaEnvio(),
                estudiante, tutor, usuarios.baseUrl());
    }
}
