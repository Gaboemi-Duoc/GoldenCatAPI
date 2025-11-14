package alumno.duoc.golden_cat_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@MappedSuperclass
@Data // Genera getters, setters, toString, equals, hashCode y un constructor con los campos requeridos.
@AllArgsConstructor // Genera un constructor con todos los campos.
@NoArgsConstructor // Genera un constructor vacio.
public abstract class Usuario {
	@Id
	private int run_user;
	@Column(nullable = false)
	private String dv_user;
	@Column(nullable = false)
	private String nombre_user;
	@Column(nullable = false)
	private String contrasenia_user;
	@Column(nullable = false)
	private String correo_user;
}