package alumno.duoc.golden_cat_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "pedido")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_pedido;
    
    @Column(nullable = false)
    private LocalDateTime pedidoDate;
    
    @Column(nullable = false, length = 20)
    private String status; // "pending", "processing", "shipped", "delivered", "cancelled"
    
    @Column(nullable = false)
    private Integer totalAmount;
    
    @Column(nullable = true, length = 500)
    private String shippingAddress;
    
    @Column(nullable = true, length = 50)
    private String paymentMethod;
    
    // Relación Many-to-One con Usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private Usuario user;
    
    // Relación One-to-Many con OrderItem
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoItems> items = new ArrayList<>();
    
    // Método para calcular el total
    public void calculateTotal() {
        if (items != null && !items.isEmpty()) {
            this.totalAmount = items.stream()
                .mapToInt(item -> item.getPriceAtPurchase() * item.getQuantity())
                .sum();
        } else {
            this.totalAmount = 0;
        }
    }
    
    // Método para establecer fecha automáticamente antes de persistir
    @PrePersist
    protected void onCreate() {
        if (pedidoDate == null) {
            pedidoDate = LocalDateTime.now();
        }
        if (status == null) {
            status = "pending";
        }
        if (totalAmount == null) {
            calculateTotal();
        }
    }
}