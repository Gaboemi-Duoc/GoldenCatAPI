package duoc.nocodenolife.perfulandia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import duoc.nocodenolife.perfulandia.model.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer> {

	@Query(value = "SELECT v FROM Venta v WHERE v.run_empleado= :run", nativeQuery = true)
	public List<Venta> obtenerListaVentasEmpleado(int run);

	@Query(value = "SELECT v FROM Venta v INNER JOIN DETALLE_VENTA dv ON v.num_bolenta = dv.num_boleta INNER JOIN Producto p ON dv.id_producto = p.id_producto WHERE p.id_producto= :idProducto" , nativeQuery = true)
	public List<Venta> obtenerListaVentasContieneProducto(String idProducto);

	@Query("SELECT v FROM Venta v WHERE v.fecha_venta BETWEEN :desde AND :hasta")
	List<Venta> buscarPorFecha(String desde, String hasta);
	//fecha formato xx-xx-xxx
	
}
