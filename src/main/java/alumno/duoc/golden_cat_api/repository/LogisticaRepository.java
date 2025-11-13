package duoc.nocodenolife.perfulandia.repository;
import duoc.nocodenolife.perfulandia.model.Pedido;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LogisticaRepository extends JpaRepository<Pedido, Integer> {

	@Query(value =  "UPDATE Pedido SET estado_pedido = :nuevoEstado WHERE id_pedido= :id_pedido", nativeQuery = true)
	public Pedido actualizarEstadoPedido(int id_pedido, String nuevoEstado);

	//public Pedido actualizarDireccionEntrega(int id_pedido, String nuevaDireccion);

	//Filtrar por estado.
	@Query("SELECT p FROM Pedido p WHERE estado_pedido = :estadoPedido")
	List<Pedido> findByEstadoPedidoIgnoreCase(String estadoPedido);

	@Query("SELECT p FROM Pedido p WHERE LOWER(p.direccion_entrega) LIKE LOWER(CONCAT('%', ?1, '%'))")
	List<Pedido> findByDireccionEntregaParcial(String direccion);
}
