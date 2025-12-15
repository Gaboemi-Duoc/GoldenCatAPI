package alumno.duoc.golden_cat_api.model;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "pedido_items")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_pedido_item;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(name = "price_at_purchase", nullable = false)
    private Integer priceAtPurchase;
    
    // Relación Many-to-One con Order
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido", nullable = false)
    private Pedido pedido;
    
    // Relación Many-to-One con Product
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private Product product;
    
    // Constructor para facilitar creación
    public PedidoItems(Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
        this.priceAtPurchase = product.getPrice();
    }
    
    // Método para calcular subtotal
    public Integer getSubtotal() {
        return priceAtPurchase * quantity;
    }
}