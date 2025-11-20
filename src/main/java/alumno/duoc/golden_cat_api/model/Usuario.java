package alumno.duoc.golden_cat_api.model;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "app_user")
@Entity
@Data // Genera getters, setters, toString, equals, hashCode y un constructor con los campos requeridos.
@AllArgsConstructor // Genera un constructor con todos los campos.
@NoArgsConstructor // Genera un constructor vacio.
public class Usuario {
	@Id // Clave primaria
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Auto incrementa el id
	private Long id_user;
	@Column(nullable = false)
	private String username;
	@Column(nullable = false)
	private String email;
	@Column(nullable = false)
	private String clave;
	@Column(nullable = true)
	private Boolean admin;
}