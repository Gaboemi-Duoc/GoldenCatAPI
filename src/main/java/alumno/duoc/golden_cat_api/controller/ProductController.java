package alumno.duoc.golden_cat_api.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import alumno.duoc.golden_cat_api.model.Product;
import alumno.duoc.golden_cat_api.repository.ProductRepository;

@RestController
@RequestMapping("/api/product")
@CrossOrigin(origins = "*") // Permitir frontend
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
        product.setDesc(productDetails.getDesc());
        product.setStock(productDetails.getStock());
        product.setCat(productDetails.getCat());

        return productRepository.save(product);
    }

    // Eliminar producto
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
    }
}
