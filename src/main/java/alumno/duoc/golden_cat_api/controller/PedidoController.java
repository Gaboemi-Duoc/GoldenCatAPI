package alumno.duoc.golden_cat_api.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import alumno.duoc.golden_cat_api.model.Pedido;
import alumno.duoc.golden_cat_api.model.PedidoItems;
import alumno.duoc.golden_cat_api.model.Product;
import alumno.duoc.golden_cat_api.model.Usuario;
import alumno.duoc.golden_cat_api.repository.PedidoRepository;
import alumno.duoc.golden_cat_api.repository.PedidoItemsRepository;
import alumno.duoc.golden_cat_api.repository.ProductRepository;
import alumno.duoc.golden_cat_api.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/pedido")
@CrossOrigin(origins = "*")
public class PedidoController {

    private final PedidoRepository pedidoRepository;
    private final PedidoItemsRepository PedidoItems;
    private final ProductRepository productRepository;
    private final UsuarioRepository usuarioRepository;

    // Inyección por constructor
    public PedidoController(PedidoRepository pedidoRepository,
                          PedidoItemsRepository PedidoItems,
                          ProductRepository productRepository,
                          UsuarioRepository usuarioRepository) {
        this.pedidoRepository = pedidoRepository;
        this.PedidoItems = PedidoItems;
        this.productRepository = productRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Obtener todos los pedidos
    @GetMapping
    public List<Pedido> getAllOrders() {
        return pedidoRepository.findAll();
    }

    // Obtener pedido por ID
    @GetMapping("/{id}")
    public Pedido getOrderById(@PathVariable Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    // Obtener pedidos por usuario
    @GetMapping("/user/{userId}")
    public List<Pedido> getOrdersByUser(@PathVariable Long userId) {
        return pedidoRepository.findByUserId(userId);
    }

    // Crear un nuevo pedido
    @PostMapping
    public Pedido createOrder(@RequestBody Pedido pedido) {
        // Validar que el usuario existe
        Usuario user = usuarioRepository.findById(pedido.getUser().getId_user())
                .orElseThrow(() -> new RuntimeException("User not found"));
        pedido.setUser(user);
        
        // Calcular total si no viene calculado
        if (pedido.getTotalAmount() == null || pedido.getTotalAmount() == 0) {
            pedido.calculateTotal();
        }
        
        // Guardar primero el pedido
        Pedido savedpedido = pedidoRepository.save(pedido);
        
        // Guardar los items del pedido
        if (pedido.getItems() != null) {
            for (PedidoItems item : pedido.getItems()) {
                // Validar que el producto existe
                Product product = productRepository.findById(item.getProduct().getId_producto())
                        .orElseThrow(() -> new RuntimeException("Product not found"));
                item.setProduct(product);
                item.setPedido(savedpedido);
                
                // Reducir stock del producto
                product.setStock(product.getStock() - item.getQuantity());
                productRepository.save(product);
                
                PedidoItems.save(item);
            }
        }
        
        return savedpedido;
    }

    // Actualizar pedido existente
    @PutMapping("/{id}")
    public Pedido updateOrder(@PathVariable Long id, @RequestBody Pedido pedidoDetails) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Actualizar campos básicos
        pedido.setStatus(pedidoDetails.getStatus());
        pedido.setShippingAddress(pedidoDetails.getShippingAddress());
        pedido.setPaymentMethod(pedidoDetails.getPaymentMethod());
        pedido.setTotalAmount(pedidoDetails.getTotalAmount());
        
        // Si se cambia el usuario, validar que existe
        if (pedidoDetails.getUser() != null) {
            Usuario user = usuarioRepository.findById(pedidoDetails.getUser().getId_user())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            pedido.setUser(user);
        }
        
        // Si el pedido se cancela, devolver stock de productos
        if ("cancelled".equalsIgnoreCase(pedidoDetails.getStatus()) && 
            !"cancelled".equalsIgnoreCase(pedido.getStatus())) {
            returnStock(pedido);
        }
        
        return pedidoRepository.save(pedido);
    }

    // Actualizar solo el estado del pedido
    @PatchMapping("/{id}/status")
    public Pedido updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        String oldStatus = pedido.getStatus();
        pedido.setStatus(status);
        
        // Si se cancela un pedido que no estaba cancelado, devolver stock
        if ("cancelled".equalsIgnoreCase(status) && !"cancelled".equalsIgnoreCase(oldStatus)) {
            returnStock(pedido);
        }
        
        return pedidoRepository.save(pedido);
    }

    // Eliminar pedido
    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        // Devolver stock si el pedido no está cancelado
        if (!"cancelled".equalsIgnoreCase(pedido.getStatus())) {
            returnStock(pedido);
        }
        
        // Eliminar primero los items (por la relación foreign key)
        PedidoItems.deleteAll(pedido.getItems());
        
        // Luego eliminar el pedido
        pedidoRepository.delete(pedido);
    }

    // Método auxiliar para devolver stock
    private void returnStock(Pedido pedido) {
        for (PedidoItems item : pedido.getItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
        }
    }
}