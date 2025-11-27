package alumno.duoc.golden_cat_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import alumno.duoc.golden_cat_api.model.LoginRequest;
import alumno.duoc.golden_cat_api.model.Usuario;
import alumno.duoc.golden_cat_api.repository.UsuarioRepository;
import alumno.duoc.golden_cat_api.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private final UsuarioRepository usuarioRepository;
    @Autowired
    private AuthService authService;

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
    @Operation(summary = "Registrar Nuevo Usuario", description = "Registra un nuevo Usuario, y lo retorna de vuelta")
    public Usuario createUsuario(@RequestBody Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @PostMapping("/login")
    @Operation(summary = "Login por Username", description = "Retorna un Usuario por nombre de usuario, habiendo ingresado su clave")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        if (authService.authenticate(loginRequest.getUsername(), loginRequest.getPassword())) {
            return ResponseEntity.ok("Login successful!");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
        }
    }

    // Actualizar usuario existente
    @PutMapping("/{id}")
    public Usuario updateUsuario(@PathVariable int id, @RequestBody Usuario usuarioDetails) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setUsername(usuarioDetails.getUsername());
        usuario.setEmail(usuarioDetails.getEmail());
        usuario.setClave(usuarioDetails.getClave());

        return usuarioRepository.save(usuario);
    }

    // Eliminar usuario
    @DeleteMapping("/{id}")
    public void deleteUsuario(@PathVariable int id) {
        usuarioRepository.deleteById(id);
    }
}
