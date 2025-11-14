package alumno.duoc.golden_cat_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import alumno.duoc.golden_cat_api.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Buscar por nombre
    @Query(value = "SELECT * FROM producto WHERE nombre = :nombre", nativeQuery = true)
    Product buscarPorNombre(String nombre);

    // Buscar por categoría
    @Query(value = "SELECT * FROM producto WHERE cat = :categoria", nativeQuery = true)
    Product buscarPorCategoria(String categoria);

    // Actualizar precio
    @Modifying
    @Transactional
    @Query(value = "UPDATE producto SET price = :newPrice WHERE id_producto = :id", nativeQuery = true)
    int actualizarPrecio(Long id, int newPrice);

    // Actualizar stock
    @Modifying
    @Transactional
    @Query(value = "UPDATE producto SET stock = :newStock WHERE id_producto = :id", nativeQuery = true)
    int actualizarStock(Long id, int newStock);

    // Actualizar descripción
    @Modifying
    @Transactional
    @Query(value = "UPDATE producto SET `desc` = :newDesc WHERE id_producto = :id", nativeQuery = true)
    int actualizarDescripcion(Long id, String newDesc);

    // Actualizar descuento
    @Modifying
    @Transactional
    @Query(value = "UPDATE producto SET descuento = :newDescuento WHERE id_producto = :id", nativeQuery = true)
    int actualizarDescuento(Long id, float newDescuento);
}
