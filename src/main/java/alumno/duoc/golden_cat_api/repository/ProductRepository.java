package alumno.duoc.golden_cat_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import alumno.duoc.golden_cat_api.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
}
