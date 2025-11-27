package alumno.duoc.golden_cat_api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import alumno.duoc.golden_cat_api.model.Usuario;
import alumno.duoc.golden_cat_api.repository.UsuarioRepository;

@Service
public class AuthService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public boolean authenticate(String username, String password) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);
        
        if (usuarioOpt.isEmpty()) {
            return false;
        }
        
        Usuario usuario = usuarioOpt.get();
        
        return password.equals(usuario.getClave());
    }
}
