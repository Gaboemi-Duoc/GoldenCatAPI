package alumno.duoc.golden_cat_api.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import alumno.duoc.golden_cat_api.model.Usuario;
import alumno.duoc.golden_cat_api.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    // Inyección por constructor
    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Obtener todos los usuarios
    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    // Obtener usuario por ID
    @GetMapping("/{id}")
    public Usuario getUsuarioById(@PathVariable int id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // Crear un nuevo usuario
    @PostMapping
    public Usuario createUsuario(@RequestBody Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // Actualizar usuario existente
    @PutMapping("/{id}")
    public Usuario updateUsuario(@PathVariable int id, @RequestBody Usuario usuarioDetails) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setNombre_user(usuarioDetails.getNombre_user());
        usuario.setCorreo_user(usuarioDetails.getCorreo_user());
        usuario.setPassword(usuarioDetails.getPassword());
        usuario.setNumber(usuarioDetails.getNumber());

        return usuarioRepository.save(usuario);
    }

    // Eliminar usuario
    @DeleteMapping("/{id}")
    public void deleteUsuario(@PathVariable int id) {
        usuarioRepository.deleteById(id);
    }
}
