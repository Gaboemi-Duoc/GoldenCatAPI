package alumno.duoc.golden_cat_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import alumno.duoc.golden_cat_api.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT * FROM product WHERE cat = :category", nativeQuery = true)
    List<Product> findByCat(@Param("category") String category);
    
    @Query(value = "SELECT DISTINCT cat FROM product", nativeQuery = true)
    List<String> findAllCategories();
    
    List<Product> findByStockLessThan(int stock);
    
    @Query(value = "SELECT COUNT(*) FROM product WHERE discount > 0", nativeQuery = true)
    long countDiscountedProducts();

}
