package alumno.duoc.golden_cat_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import alumno.duoc.golden_cat_api.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Buscar por correo
    @Query(value = "SELECT * FROM usuario WHERE correo_user = :correo", nativeQuery = true)
    Usuario buscarPorCorreo(String correo);

    // Buscar por nombre
    @Query(value = "SELECT * FROM usuario WHERE nombre_user = :nombre", nativeQuery = true)
    Usuario buscarPorNombre(String nombre);

    // Actualizar contraseña
    @Modifying
    @Transactional
    @Query(value = "UPDATE usuario SET contrasenia_user = :newPass WHERE run_user = :run", nativeQuery = true)
    int actualizarContrasenia(int run, String newPass);

    // Actualizar correo
    @Modifying
    @Transactional
    @Query(value = "UPDATE usuario SET correo_user = :newCorreo WHERE run_user = :run", nativeQuery = true)
    int actualizarCorreo(int run, String newCorreo);

}
