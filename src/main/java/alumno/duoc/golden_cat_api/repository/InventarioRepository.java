package duoc.nocodenolife.perfulandia.repository;
import duoc.nocodenolife.perfulandia.model.Producto;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InventarioRepository extends JpaRepository<Producto, String> {

	@Query(value = "SELECT p FROM Producto p WHERE p.nom_producto = :nombre", nativeQuery = true)
	public Producto buscarPorNombre(String nombre);

	@Query(value = "UPDATE Producto SET descuento = :nuevoDescuento WHERE id_producto= :id_producto ", nativeQuery = true)
	void actualizarDescuento(String id_producto, float nuevoDescuento);

	@Query(value = "UPDATE Producto SET stock = :nuevoStock WHERE id_producto= :id_producto ", nativeQuery = true )
	void actualizarStock(String id_producto, int nuevoStock);

	// Obtener solo el stock actual de un producto
	@Query(value = "SELECT stock FROM producto WHERE id_producto = :id_producto", nativeQuery = true)
	int obtenerStock(String id_producto);

	// Listar productos con stock igual o menor a 10
	@Query(value = "SELECT p FROM producto p WHERE stock <= 10", nativeQuery = true)
	List<Producto> listarProductosBajoStock();
}
