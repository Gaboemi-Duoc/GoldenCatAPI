package duoc.nocodenolife.perfulandia.repository;
import duoc.nocodenolife.perfulandia.model.Cliente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

	@Query(value = "SELECT e FROM Cliente e WHERE e.nombre_user = :nombre", nativeQuery = true)
	public Cliente buscarPorNombre(String nombre);

	@Query(value = "SELECT e FROM Cliente e WHERE e.correo_user = :correo", nativeQuery = true)
	public Cliente buscarPorCorreo(String correo);
	
	@Query(value = "UPDATE Cliente SET contrasenia_user = :newContrasenia WHERE run_user= :run ", nativeQuery = true)
	public Cliente actualizarContrasenia(int run, String newContrasenia);
	
	@Query(value = "UPDATE Cliente SET correo_user = :newCorreo WHERE run_user= :run ", nativeQuery = true)
	public Cliente actualizarCorreo(int run, String newCorreo);
}
