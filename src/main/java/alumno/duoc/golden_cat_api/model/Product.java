package alumno.duoc.golden_cat_api.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "producto")
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
	private String desc;
	@Column(nullable = false)
	private int price;
	@Column(nullable = false)
	private int stock;
	@Column(nullable = false)
	private String cat;
	@Column(nullable = true)
	private float descuento;
}
