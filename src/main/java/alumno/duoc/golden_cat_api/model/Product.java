package alumno.duoc.golden_cat_api.model;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.Map;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "product")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
	@Id // Clave primaria
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Auto incrementa el id
	private Long id_producto;

	@Column(nullable = false)
	private String nombre; //nombre producto

	@Column(nullable = true)
	private String description;

	@Column(nullable = false)
	private int price;

	@Column(nullable = false)
	private int stock;

	@Column(nullable = false)
	private String cat;

	@Column(nullable = true) // Changed from nullable = false
    private List<String> detail;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "jsonb", nullable = true)
	private Map<String, String> details;
	
	@Column(nullable = true)
	private float discount;
}
