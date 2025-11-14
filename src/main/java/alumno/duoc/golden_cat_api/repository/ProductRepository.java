package alumno.duoc.golden_cat_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import alumno.duoc.golden_cat_api.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
}
