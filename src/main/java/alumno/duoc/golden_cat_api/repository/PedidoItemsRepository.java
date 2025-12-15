package alumno.duoc.golden_cat_api.repository;

import alumno.duoc.golden_cat_api.model.PedidoItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoItemsRepository extends JpaRepository<PedidoItems, Long> {
    
    // Obtener items por pedido
    List<PedidoItems> findByPedidoIdPedido(Long pedidoId);
    
    // Obtener items por producto
    @Query("SELECT oi FROM pedido_items oi WHERE oi.product.id_producto = :productId")
    List<PedidoItems> findByProductId(@Param("productId") Long productId);
    
    // Contar cuántas veces se ha vendido un producto
    @Query("SELECT SUM(oi.quantity) FROM pedido_items oi WHERE oi.product.id_producto = :productId")
    Integer countSalesByProductId(@Param("productId") Long productId);
    
    // Obtener los productos más vendidos
    @Query("SELECT oi.product.id_producto, SUM(oi.quantity) as totalQuantity " +
           "FROM pedido_items oi GROUP BY oi.product.id_producto ORDER BY totalQuantity DESC")
    List<Object[]> findTopSellingProducts();
}