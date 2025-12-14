package alumno.duoc.golden_cat_api.model;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "blog")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Blog {
	@Id // Clave primaria
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Auto incrementa el id
	private Long id_blog;

	@Column(nullable = false)
	private String nombre; //nombre producto

	@Column(nullable = true)
	private String description;

	@Column(nullable = false)
	private String body;

	@Column(nullable = false)
	private String date;

	@Column(nullable = false)
	private String writer;
}
