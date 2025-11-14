package alumno.duoc.golden_cat_api.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
	@Id
	private int run_user;
	@Column(nullable = false)

	private String dv_user;
	@Column(nullable = false)

	private String nombre_user;
	@Column(nullable = false)

	private String Password;
	@Column(nullable = false)

	private String correo_user;

	private String number;
}