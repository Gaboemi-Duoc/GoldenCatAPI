package alumno.duoc.golden_cat_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "producto")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
	@Id
	private String id_producto;
	@Column(nullable = false)
	private String nom_producto; //nombre producto
	@Column(nullable = true)
	private String descripcion_producto;
	@Column(nullable = false)
	private int precio;
	@Column(nullable = false)
	private int stock;
	@Column(nullable = false)
	private String categoria;
	@Column(nullable = true)
	private float descuento;
}
