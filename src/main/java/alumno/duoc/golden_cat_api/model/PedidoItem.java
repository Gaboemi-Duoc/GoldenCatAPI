package alumno.duoc.golden_cat_api.model;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "pedido_item")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_item;
    
    // Reference to Pedido
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;
    
    // Reference to Product by ID only (no duplicate data)
    @Column(name = "producto_id", nullable = false)
    private Long productoId;
    
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;
    
    @Column(name = "precio_unitario", nullable = false)
    private Integer precioUnitario;
    
    @Column(name = "subtotal", nullable = false)
    private Integer subtotal;
    
    // PrePersist to calculate subtotal
    @PrePersist
    @PreUpdate
    protected void calculateSubtotal() {
        if (subtotal == null && precioUnitario != null && cantidad != null) {
            subtotal = precioUnitario * cantidad;
        }
    }
}