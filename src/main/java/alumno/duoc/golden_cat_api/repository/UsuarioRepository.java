package alumno.duoc.golden_cat_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import alumno.duoc.golden_cat_api.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query(value = "SELECT * FROM app_user WHERE username = :nombreuser", nativeQuery = true)
    Optional<Usuario> findByUsername(String nombreuser);

    @Query(value = "SELECT * FROM app_user WHERE email = :user_email", nativeQuery = true)
    Optional<Usuario> findByEmail(String user_email);
    
}
