package alumno.duoc.golden_cat_api.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import alumno.duoc.golden_cat_api.model.PedidoItem;

@Repository
public interface PedidoItemRepository extends JpaRepository<PedidoItem, Long> {
    
    // Find all items for a specific pedido
    @Query(value = "SELECT * FROM pedido_item WHERE pedido_id = :pedidoId", nativeQuery = true)
    List<PedidoItem> findByPedidoId(@Param("pedidoId") Long pedidoId);
    
    // Find all items for a specific product
    @Query(value = "SELECT * FROM pedido_item WHERE producto_id = :productoId", nativeQuery = true)
    List<PedidoItem> findByProductoId(@Param("productoId") Long productoId);
    
    // Find total quantity sold for a product
    @Query(value = "SELECT SUM(cantidad) FROM pedido_item WHERE producto_id = :productoId", nativeQuery = true)
    Integer findTotalQuantitySoldByProductId(@Param("productoId") Long productoId);
}