package alumno.duoc.golden_cat_api.model;

import jakarta.persistence.*;
import lombok.*;

@Entity // Esta clase se convierte en tabla
@Data // Lombok genera getters/setters/toString/etc.
@NoArgsConstructor // Constructor vacío
@AllArgsConstructor // Constructor con todos los campos
public class Book {

    @Id // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto incrementa el id
    private Long id;

    private String title; // Columna title
    private String author; // Columna author

}

