package duoc.nocodenolife.perfulandia.repository;
import duoc.nocodenolife.perfulandia.model.Empleado;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Integer>  {

	@Query(value = "SELECT e FROM Empleado e WHERE e.nombre_user = :nombre", nativeQuery = true)
	public Empleado buscarPorNombre(String nombre);

	@Query(value = "SELECT e FROM Empleado e WHERE e.correo_user = :correo", nativeQuery = true)
	public Empleado buscarPorCorreo(String correo);

	@Query(value = "SELECT e FROM Empleado e WHERE e.cargo = :numCargo", nativeQuery = true)
	public Empleado buscarPorCargo(int numCargo);

	@Query(value = "SELECT e FROM Empleado e WHERE e.puesto = :strgPuesto", nativeQuery = true)
	public Empleado buscarPorPuesto(String strgPuesto);
	
	@Query(value = "UPDATE Empleado SET contrasenia_user = :newContrasenia WHERE run_user= :run ", nativeQuery = true)
	public Empleado actualizarContrasenia(int run, String newContrasenia);
	
	@Query(value = "UPDATE Empleado SET correo_user = :newCorreo WHERE run_user= :run ", nativeQuery = true)
	public Empleado actualizarCorreo(int run, String newCorreo);

}
