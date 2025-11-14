package alumno.duoc.golden_cat_api.model;

import jakarta.persistence.*;
import lombok.*;
// @Table(name = "usuario")
@Entity
@MappedSuperclass
@Data // Genera getters, setters, toString, equals, hashCode y un constructor con los campos requeridos.
@AllArgsConstructor // Genera un constructor con todos los campos.
@NoArgsConstructor // Genera un constructor vacio.
public abstract class Usuario {
	@Id // Clave primaria
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Auto incrementa el id
	private Long id_user;
	@Column(nullable = false)
	private String nombre;
	@Column(nullable = false)
	private String correo_user;
	@Column(nullable = false)
	private String clave;
	@Column(nullable = true)
	private Boolean admin;
}