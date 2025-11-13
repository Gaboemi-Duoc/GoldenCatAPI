package duoc.nocodenolife.perfulandia.model;

import java.util.Set;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "venta")
@Data // Genera getters, setters, toString, equals, hashCode y un constructor con los campos requeridos.
@AllArgsConstructor // Genera un constructor con todos los campos.
@NoArgsConstructor // Genera un constructor vacio.
public class Venta { // Boleta
	@Id
	private int num_boleta;
	@ManyToOne
	@JoinColumn(name = "run_cliente", nullable = false) // Clave foránea
	private Cliente cliente_venta; // Cliente asociada a la Venta
	@ManyToOne
	@JoinColumn(name = "run_empleado", nullable = false) // Clave foránea
	private Empleado empleado_responsable; // Empleado que manejo la Venta
	@OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<DetalleVenta> productos_venta; // Lista de los Producto de esta Venta
	//private int totalTransaccion;
	@Column(nullable = false)
	private String fecha_venta;
	@Column(nullable = false)
	private String tipo_transaccion;
}
