package duoc.nocodenolife.perfulandia.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pedido")
@Data // Genera getters, setters, toString, equals, hashCode y un constructor con los campos requeridos.
@AllArgsConstructor // Genera un constructor con todos los campos.
@NoArgsConstructor // Genera un constructor vacio.
public class Pedido {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id_pedido;
	@Column(nullable = false)
	private String estado_pedido;
	@Column(nullable = false)
	private String direccion_entrega;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "num_boleta", nullable = false) // Clave foránea
	private Venta venta_asociada;
	
}
