package alumno.duoc.golden_cat_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import alumno.duoc.golden_cat_api.repository.UsuarioRepository;

@Service
public class AuthService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder; // Add Spring Security dependency for this

    public boolean authenticate(String username, String password) {
        return usuarioRepository.findByUsername(username)
                .map(user -> passwordEncoder.matches(password, user.getClave()))
                .orElse(false);
    }
}