package alumno.duoc.golden_cat_api.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
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
    
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(name = "estado", nullable = false)
    private String estado; // e.g., "PENDIENTE", "ENVIADO", "ENTREGADO", "CANCELADO"
    
    @Column(name = "total", nullable = false)
    private Integer total;
    
    @Column(name = "direccion_envio")
    private String direccionEnvio;
    
    // Reference to Usuario by ID only (no duplicate data)
    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;
    
    // One Pedido can have many items (cart products)
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PedidoItem> items;
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}