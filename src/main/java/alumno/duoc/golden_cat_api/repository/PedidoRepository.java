package alumno.duoc.golden_cat_api.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import alumno.duoc.golden_cat_api.model.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    // Find all pedidos by usuario ID
    @Query(value = "SELECT * FROM pedido WHERE usuario_id = :usuarioId ORDER BY fecha_creacion DESC", nativeQuery = true)
    List<Pedido> findByUsuarioId(@Param("usuarioId") Long usuarioId);
    
    // Find pedidos by estado
    List<Pedido> findByEstado(String estado);
    
    // Find pedidos within a date range
    @Query(value = "SELECT * FROM pedido WHERE fecha_creacion BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<Pedido> findPedidosByDateRange(@Param("startDate") String startDate, @Param("endDate") String endDate);
    
    // Count pedidos by usuario
    @Query(value = "SELECT COUNT(*) FROM pedido WHERE usuario_id = :usuarioId", nativeQuery = true)
    long countByUsuarioId(@Param("usuarioId") Long usuarioId);
}