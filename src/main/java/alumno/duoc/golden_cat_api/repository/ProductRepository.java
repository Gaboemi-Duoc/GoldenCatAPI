package alumno.duoc.golden_cat_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import alumno.duoc.golden_cat_api.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM product p WHERE p.cat = :category")
    List<Product> findByCat(String category);
    
    @Query("SELECT DISTINCT p.cat FROM product p")
    List<String> findAllCategories();
    
    List<Product> findByStockLessThan(int threshold);
    
    @Query("SELECT COUNT(p) FROM product p WHERE p.discount > 0")
    long countDiscountedProducts();
}
