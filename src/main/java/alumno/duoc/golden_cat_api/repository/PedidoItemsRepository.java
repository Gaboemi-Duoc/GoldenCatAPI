package alumno.duoc.golden_cat_api.repository;

import alumno.duoc.golden_cat_api.model.OrderArc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenarcRepository extends JpaRepository<OrderArc, Long> {
    
    // Obtener items por pedido
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.id_order = :orderId")
    List<OrderArc> findByOrderId(@Param("orderId") Long orderId);
    
    // Obtener items por producto
    @Query("SELECT oi FROM OrderItem oi WHERE oi.product.id_producto = :productId")
    List<OrderArc> findByProductId(@Param("productId") Long productId);
    
    // Contar cuántas veces se ha vendido un producto
    @Query("SELECT SUM(oi.quantity) FROM OrderItem oi WHERE oi.product.id_producto = :productId")
    Integer countSalesByProductId(@Param("productId") Long productId);
    
    // Obtener los productos más vendidos
    @Query("SELECT oi.product.id_producto, SUM(oi.quantity) as totalQuantity " +
           "FROM OrderItem oi GROUP BY oi.product.id_producto ORDER BY totalQuantity DESC")
    List<Object[]> findTopSellingProducts();
}