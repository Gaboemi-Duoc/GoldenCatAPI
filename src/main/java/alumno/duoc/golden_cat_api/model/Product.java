package alumno.duoc.golden_cat_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Table(name = "product")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long id_producto;
    
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "price", nullable = false)
    private Integer price;
    
    @Column(name = "stock", nullable = false)
    private Integer stock;
    
    @Column(name = "cat", nullable = false)
    private String cat;
    
    // Para PostgreSQL array - usar columna simple
    @Column(name = "detail", columnDefinition = "TEXT")
    private String detail; // Guardar como string separado por comas
    
    @Column(name = "discount")
    private Float discount;
    
    @Column(name = "details", columnDefinition = "TEXT")
    private String details; // Guardar JSON como string
    
    // Método para obtener detail como lista
    public List<String> getDetailList() {
        if (detail == null || detail.isEmpty()) {
            return new ArrayList<>();
        }
        return List.of(detail.split(","));
    }
    
    // Método para establecer detail desde lista
    public void setDetailList(List<String> details) {
        if (details == null || details.isEmpty()) {
            this.detail = "";
        } else {
            this.detail = String.join(",", details);
        }
    }
    
    // Métodos de negocio
    public boolean isAvailable() {
        return stock > 0;
    }
    
    public Integer getDiscountedPrice() {
        if (discount != null && discount > 0) {
            return price - (int)(price * (discount / 100.0));
        }
        return price;
    }
}