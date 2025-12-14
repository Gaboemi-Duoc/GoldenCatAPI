package alumno.duoc.golden_cat_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import alumno.duoc.golden_cat_api.model.Product;
import alumno.duoc.golden_cat_api.repository.ProductRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/product")
@CrossOrigin(origins = "*") // Permitir frontend
@Tag(name = "Product Controller", description = "API para gestionar operaciones CRUD de productos")
public class ProductController {

    private final ProductRepository productRepository;

    // Inyección por constructor
    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Obtener todos los productos
    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Obtener producto por ID
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    // Crear un nuevo producto
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        product.setId_producto(null);
        return productRepository.save(product);
    }

    // Actualizar producto existente
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Ajusta estos campos según tu clase Product
        product.setNombre(productDetails.getNombre());
        product.setPrice(productDetails.getPrice());
        product.setDescription(productDetails.getDescription());
        product.setStock(productDetails.getStock());
        product.setCat(productDetails.getCat());
        product.setDiscount(productDetails.getDiscount());
        product.setDetail(productDetails.getDetail());

        return productRepository.save(product);
    }

    // Actualizar producto existente
    @PutMapping("/ofertas/{id}")
    public Product ofertaProduct(@PathVariable Long id, @RequestAttribute Float newDiscount) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Ajusta estos campos según tu clase Product
        product.setDiscount(newDiscount);

        return productRepository.save(product);
    }

    // Eliminar producto
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
    }

    // Get product count for dashboard
    @GetMapping("/count")
    @Operation(summary = "Get total product count", description = "Returns the total number of products")
    public Long getProductCount() {
        return productRepository.count();
    }

    // Get products by category
    @GetMapping("/category/{category}")
    @Operation(summary = "Get products by category", description = "Returns all products in a specific category")
    public List<Product> getProductsByCategory(@PathVariable String category) {
        return productRepository.findByCat(category);
    }

    // Get all unique categories
    @GetMapping("/categories")
    @Operation(summary = "Get all product categories", description = "Returns a list of all unique product categories")
    public List<String> getAllCategories() {
        return productRepository.findAllCategories();
    }

    // Get low stock products (for alerts)
    @GetMapping("/low-stock/{threshold}")
    @Operation(summary = "Get low stock products", description = "Returns products with stock below threshold")
    public List<Product> getLowStockProducts(@PathVariable int threshold) {
        return productRepository.findByStockLessThan(threshold);
    }

    // Bulk product operations
    @PostMapping("/bulk-delete")
    @Operation(summary = "Delete multiple products", description = "Deletes multiple products by their IDs")
    public ResponseEntity<?> bulkDeleteProducts(@RequestBody List<Long> productIds) {
        try {
            productRepository.deleteAllById(productIds);
            return ResponseEntity.ok().body(Map.of("message", "Products deleted successfully", "deletedCount", productIds.size()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete products", "details", e.getMessage()));
        }
    }

    // Update product stock
    @PatchMapping("/{id}/stock")
    @Operation(summary = "Update product stock", description = "Updates the stock quantity of a product")
    public Product updateProductStock(@PathVariable Long id, @RequestParam int newStock) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        if (newStock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
        
        product.setStock(newStock);
        return productRepository.save(product);
    }

    // Apply discount to multiple products
    @PostMapping("/bulk-discount")
    @Operation(summary = "Apply discount to multiple products", description = "Applies the same discount percentage to multiple products")
    public ResponseEntity<?> applyBulkDiscount(@RequestParam Float discountPercentage, 
                                            @RequestBody List<Long> productIds) {
        try {
            if (discountPercentage < 0 || discountPercentage > 100) {
                throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
            }
            
            List<Product> updatedProducts = productRepository.findAllById(productIds).stream()
                    .map(product -> {
                        product.setDiscount(discountPercentage);
                        return product;
                    })
                    .collect(Collectors.toList());
            
            productRepository.saveAll(updatedProducts);
            
            return ResponseEntity.ok().body(Map.of(
                "message", "Discount applied successfully",
                "discountPercentage", discountPercentage,
                "affectedProducts", updatedProducts.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
