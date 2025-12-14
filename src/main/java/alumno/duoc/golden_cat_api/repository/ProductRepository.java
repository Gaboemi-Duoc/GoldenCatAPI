package alumno.duoc.golden_cat_api.repository;

<<<<<<< HEAD
import alumno.duoc.golden_cat_api.model.Product;
=======
import java.util.List;

>>>>>>> 66180b07434f6f9564846da51a1f15c6ba38ba35
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT * FROM product WHERE cat = :category", nativeQuery = true)
    List<Product> findByCat(@Param("category") String category);
    
<<<<<<< HEAD
    // Buscar por categoría
    List<Product> findByCat(String cat);
    
    // Buscar por nombre (case insensitive)
    List<Product> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar productos con stock disponible
    List<Product> findByStockGreaterThan(Integer stock);
    
    // Buscar productos con descuento
    @Query("SELECT p FROM Product p WHERE p.discount > 0")
    List<Product> findProductsWithDiscount();
    
    // Buscar por rango de precio
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    List<Product> findByPriceBetween(@Param("minPrice") Integer minPrice, 
                                    @Param("maxPrice") Integer maxPrice);
    
    // Buscar productos que contengan un detalle específico
    @Query("SELECT p FROM Product p WHERE p.detail LIKE %:keyword%")
    List<Product> findByDetailContaining(@Param("keyword") String keyword);
    
    // Obtener productos por múltiples categorías
    @Query("SELECT p FROM Product p WHERE p.cat IN :categories")
    List<Product> findByCategories(@Param("categories") List<String> categories);
    
    // Obtener productos más vendidos (necesitarías join con order_items)
    @Query("SELECT p FROM Product p ORDER BY p.stock ASC")
    List<Product> findLowStockProducts();
}
=======
    @Query(value = "SELECT DISTINCT cat FROM product", nativeQuery = true)
    List<String> findAllCategories();
    
    List<Product> findByStockLessThan(int stock);
    
    @Query(value = "SELECT COUNT(*) FROM product WHERE discount > 0", nativeQuery = true)
    long countDiscountedProducts();

}
>>>>>>> 66180b07434f6f9564846da51a1f15c6ba38ba35
