package alumno.duoc.golden_cat_api.repository;

import alumno.duoc.golden_cat_api.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenRepository extends JpaRepository<Order, Long> {
    
    // Obtener pedidos por usuario
    @Query("SELECT o FROM Order o WHERE o.user.id_user = :userId")
    List<Order> findByUserId(@Param("userId") Long userId);
    
    // Obtener pedidos por estado
    List<Order> findByStatus(String status);
    
    // Obtener pedidos por rango de fechas
    @Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate")
    List<Order> findByOrderDateBetween(@Param("startDate") java.time.LocalDateTime startDate, 
                                       @Param("endDate") java.time.LocalDateTime endDate);
    
    // Contar pedidos por usuario
    @Query("SELECT COUNT(o) FROM Order o WHERE o.user.id_user = :userId")
    Long countByUserId(@Param("userId") Long userId);
    
    // Obtener pedidos con total mayor a un valor
    @Query("SELECT o FROM Order o WHERE o.totalAmount > :minAmount")
    List<Order> findByTotalAmountGreaterThan(@Param("minAmount") Integer minAmount);
}