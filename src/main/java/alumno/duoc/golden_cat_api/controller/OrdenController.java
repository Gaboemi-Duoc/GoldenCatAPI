package alumno.duoc.golden_cat_api.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import alumno.duoc.golden_cat_api.model.Orden;
import alumno.duoc.golden_cat_api.model.OrdenArc;
import alumno.duoc.golden_cat_api.model.Product;
import alumno.duoc.golden_cat_api.model.Usuario;
import alumno.duoc.golden_cat_api.repository.OrdenRepository;
import alumno.duoc.golden_cat_api.repository.OrdenarcRepository;
import alumno.duoc.golden_cat_api.repository.ProductRepository;
import alumno.duoc.golden_cat_api.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/order")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrdenRepository ordenRepository;
    private final OrdenarcRepository ordenarcRepository;
    private final ProductRepository productRepository;
    private final UsuarioRepository usuarioRepository;

    // Inyección por constructor
    public OrderController(OrdenRepository ordenRepository,
                          OrdenarcRepository ordenarcRepository,
                          ProductRepository productRepository,
                          UsuarioRepository usuarioRepository) {
        this.ordenRepository = ordenRepository;
        this.ordenarcRepository = ordenarcRepository;
        this.productRepository = productRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Obtener todos los pedidos
    @GetMapping
    public List<Orden> getAllOrders() {
        return ordenRepository.findAll();
    }

    // Obtener pedido por ID
    @GetMapping("/{id}")
    public Orden getOrderById(@PathVariable Long id) {
        return ordenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    // Obtener pedidos por usuario
    @GetMapping("/user/{userId}")
    public List<Orden> getOrdersByUser(@PathVariable Long userId) {
        return ordenRepository.findByUserId(userId);
    }

    // Crear un nuevo pedido
    @PostMapping
    public Orden createOrder(@RequestBody Orden orden) {
        // Validar que el usuario existe
        Usuario user = usuarioRepository.findById(orden.getUser().getId_user())
                .orElseThrow(() -> new RuntimeException("User not found"));
        orden.setUser(user);
        
        // Calcular total si no viene calculado
        if (orden.getTotalAmount() == null || orden.getTotalAmount() == 0) {
            orden.calculateTotal();
        }
        
        // Guardar primero el pedido
        Orden savedOrden = ordenRepository.save(orden);
        
        // Guardar los items del pedido
        if (orden.getItems() != null) {
            for (OrdenArc item : orden.getItems()) {
                // Validar que el producto existe
                Product product = productRepository.findById(item.getProduct().getId_producto())
                        .orElseThrow(() -> new RuntimeException("Product not found"));
                item.setProduct(product);
                item.setOrden(savedOrden);
                
                // Reducir stock del producto
                product.setStock(product.getStock() - item.getQuantity());
                productRepository.save(product);
                
                ordenarcRepository.save(item);
            }
        }
        
        return savedOrden;
    }

    // Actualizar pedido existente
    @PutMapping("/{id}")
    public Orden updateOrder(@PathVariable Long id, @RequestBody Orden ordenDetails) {
        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Actualizar campos básicos
        orden.setStatus(ordenDetails.getStatus());
        orden.setShippingAddress(ordenDetails.getShippingAddress());
        orden.setPaymentMethod(ordenDetails.getPaymentMethod());
        orden.setTotalAmount(ordenDetails.getTotalAmount());
        
        // Si se cambia el usuario, validar que existe
        if (ordenDetails.getUser() != null) {
            Usuario user = usuarioRepository.findById(ordenDetails.getUser().getId_user())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            orden.setUser(user);
        }
        
        // Si el pedido se cancela, devolver stock de productos
        if ("cancelled".equalsIgnoreCase(ordenDetails.getStatus()) && 
            !"cancelled".equalsIgnoreCase(orden.getStatus())) {
            returnStock(orden);
        }
        
        return ordenRepository.save(orden);
    }

    // Actualizar solo el estado del pedido
    @PatchMapping("/{id}/status")
    public Orden updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        String oldStatus = orden.getStatus();
        orden.setStatus(status);
        
        // Si se cancela un pedido que no estaba cancelado, devolver stock
        if ("cancelled".equalsIgnoreCase(status) && !"cancelled".equalsIgnoreCase(oldStatus)) {
            returnStock(orden);
        }
        
        return ordenRepository.save(orden);
    }

    // Eliminar pedido
    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        // Devolver stock si el pedido no está cancelado
        if (!"cancelled".equalsIgnoreCase(orden.getStatus())) {
            returnStock(orden);
        }
        
        // Eliminar primero los items (por la relación foreign key)
        ordenarcRepository.deleteAll(orden.getItems());
        
        // Luego eliminar el pedido
        ordenRepository.delete(orden);
    }

    // Método auxiliar para devolver stock
    private void returnStock(Orden orden) {
        for (OrdenArc item : orden.getItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
        }
    }
}