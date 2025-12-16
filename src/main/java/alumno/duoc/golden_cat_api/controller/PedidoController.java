package alumno.duoc.golden_cat_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import alumno.duoc.golden_cat_api.model.Pedido;
import alumno.duoc.golden_cat_api.model.PedidoItem;
import alumno.duoc.golden_cat_api.model.Product;
import alumno.duoc.golden_cat_api.model.Usuario;
import alumno.duoc.golden_cat_api.repository.PedidoRepository;
import alumno.duoc.golden_cat_api.repository.PedidoItemRepository;
import alumno.duoc.golden_cat_api.repository.ProductRepository;
import alumno.duoc.golden_cat_api.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*") // Permitir frontend
@Tag(name = "Pedido Controller", description = "API para gestionar pedidos de usuarios")
public class PedidoController {

    private final PedidoRepository pedidoRepository;
    private final PedidoItemRepository pedidoItemRepository;
    private final ProductRepository productRepository;
    private final UsuarioRepository usuarioRepository;

    // Inyección por constructor
    public PedidoController(PedidoRepository pedidoRepository,
                          PedidoItemRepository pedidoItemRepository,
                          ProductRepository productRepository,
                          UsuarioRepository usuarioRepository) {
        this.pedidoRepository = pedidoRepository;
        this.pedidoItemRepository = pedidoItemRepository;
        this.productRepository = productRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Obtener todos los pedidos
    @GetMapping
    @Operation(summary = "Obtener todos los pedidos", description = "Retorna una lista de todos los pedidos")
    public List<Pedido> getAllPedidos() {
        return pedidoRepository.findAll();
    }

    // Obtener pedido por ID
    @GetMapping("/{id}")
    @Operation(summary = "Obtener pedido por ID", description = "Retorna un pedido específico por su ID")
    public ResponseEntity<?> getPedidoById(@PathVariable Long id) {
        Optional<Pedido> pedido = pedidoRepository.findById(id);
        if (pedido.isPresent()) {
            return ResponseEntity.ok(pedido.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Pedido no encontrado"));
    }

    // Obtener pedidos por usuario
    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Obtener pedidos por usuario", description = "Retorna todos los pedidos de un usuario específico")
    public ResponseEntity<?> getPedidosByUsuario(@PathVariable Long usuarioId) {
        // Verificar que el usuario existe
        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
        if (!usuario.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Usuario no encontrado"));
        }
        
        List<Pedido> pedidos = pedidoRepository.findByUsuarioId(usuarioId);
        return ResponseEntity.ok(pedidos);
    }

    // Obtener pedidos por estado
    @GetMapping("/estado/{estado}")
    @Operation(summary = "Obtener pedidos por estado", description = "Retorna pedidos filtrados por estado")
    public List<Pedido> getPedidosByEstado(@PathVariable String estado) {
        return pedidoRepository.findByEstado(estado);
    }

    // Crear un nuevo pedido
    @PostMapping
    @Operation(summary = "Crear nuevo pedido", description = "Crea un nuevo pedido con sus items")
    public ResponseEntity<?> createPedido(@RequestBody Map<String, Object> pedidoData) {
        try {
            // Extraer datos del request
            Long usuarioId = Long.parseLong(pedidoData.get("usuarioId").toString());
            String direccionEnvio = (String) pedidoData.get("direccionEnvio");
            List<Map<String, Object>> itemsData = (List<Map<String, Object>>) pedidoData.get("items");
            
            // Validar que el usuario existe
            Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
            if (!usuario.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Usuario no encontrado"));
            }
            
            // Crear el pedido
            Pedido pedido = new Pedido();
            pedido.setUsuarioId(usuarioId);
            pedido.setDireccionEnvio(direccionEnvio);
            pedido.setEstado("PENDIENTE");
            
            // Calcular total y procesar items
            Integer total = 0;
            
            // Validar y procesar cada item
            for (Map<String, Object> itemData : itemsData) {
                Long productoId = Long.parseLong(itemData.get("productoId").toString());
                Integer cantidad = Integer.parseInt(itemData.get("cantidad").toString());
                
                Optional<Product> producto = productRepository.findById(productoId);
                if (!producto.isPresent()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(Map.of("error", "Producto no encontrado: " + productoId));
                }
                
                Product product = producto.get();
                
                // Validar stock
                if (product.getStock() < cantidad) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(Map.of("error", "Stock insuficiente para: " + product.getNombre()));
                }
                
                // Calcular subtotal (usar precio con descuento si aplica)
                Integer precioUnitario = product.getDiscountedPrice();
                Integer subtotal = precioUnitario * cantidad;
                total += subtotal;
                
                // Reducir stock
                product.setStock(product.getStock() - cantidad);
                productRepository.save(product);
            }
            
            pedido.setTotal(total);
            Pedido savedPedido = pedidoRepository.save(pedido);
            
            // Crear y guardar los items del pedido
            for (Map<String, Object> itemData : itemsData) {
                Long productoId = Long.parseLong(itemData.get("productoId").toString());
                Integer cantidad = Integer.parseInt(itemData.get("cantidad").toString());
                
                Product product = productRepository.findById(productoId).get();
                Integer precioUnitario = product.getDiscountedPrice();
                Integer subtotal = precioUnitario * cantidad;
                
                PedidoItem pedidoItem = new PedidoItem();
                pedidoItem.setPedido(savedPedido);
                pedidoItem.setProductoId(productoId);
                pedidoItem.setCantidad(cantidad);
                pedidoItem.setPrecioUnitario(precioUnitario);
                pedidoItem.setSubtotal(subtotal);
                
                pedidoItemRepository.save(pedidoItem);
            }
            
            // Recargar pedido con items para devolverlo completo
            savedPedido = pedidoRepository.findById(savedPedido.getId_pedido()).get();
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                        "message", "Pedido creado exitosamente",
                        "pedido", savedPedido
                    ));
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Error al crear pedido: " + e.getMessage()));
        }
    }

    // Actualizar estado del pedido
    @PatchMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado del pedido", description = "Actualiza el estado de un pedido existente")
    public ResponseEntity<?> updatePedidoEstado(@PathVariable Long id, @RequestParam String estado) {
        try {
            Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);
            if (!pedidoOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Pedido no encontrado"));
            }
            
            Pedido pedido = pedidoOpt.get();
            String estadoAnterior = pedido.getEstado();
            
            // Si se cancela un pedido que no estaba cancelado, devolver stock
            if ("CANCELADO".equalsIgnoreCase(estado) && !"CANCELADO".equalsIgnoreCase(estadoAnterior)) {
                devolverStockPedido(pedido);
            }
            
            pedido.setEstado(estado.toUpperCase());
            Pedido updatedPedido = pedidoRepository.save(pedido);
            
            return ResponseEntity.ok(Map.of(
                "message", "Estado del pedido actualizado",
                "pedido", updatedPedido
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Error al actualizar estado: " + e.getMessage()));
        }
    }

    // Actualizar información del pedido
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar pedido", description = "Actualiza la información de un pedido existente")
    public ResponseEntity<?> updatePedido(@PathVariable Long id, @RequestBody Pedido pedidoDetails) {
        try {
            Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);
            if (!pedidoOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Pedido no encontrado"));
            }
            
            Pedido pedido = pedidoOpt.get();
            
            // Actualizar campos permitidos
            if (pedidoDetails.getDireccionEnvio() != null) {
                pedido.setDireccionEnvio(pedidoDetails.getDireccionEnvio());
            }
            
            if (pedidoDetails.getTotal() != null) {
                pedido.setTotal(pedidoDetails.getTotal());
            }
            
            Pedido updatedPedido = pedidoRepository.save(pedido);
            
            return ResponseEntity.ok(Map.of(
                "message", "Pedido actualizado exitosamente",
                "pedido", updatedPedido
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Error al actualizar pedido: " + e.getMessage()));
        }
    }

    // Eliminar pedido
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar pedido", description = "Elimina un pedido existente")
    public ResponseEntity<?> deletePedido(@PathVariable Long id) {
        try {
            Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);
            if (!pedidoOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Pedido no encontrado"));
            }
            
            Pedido pedido = pedidoOpt.get();
            
            // Si el pedido no está cancelado, devolver stock
            if (!"CANCELADO".equalsIgnoreCase(pedido.getEstado())) {
                devolverStockPedido(pedido);
            }
            
            // Eliminar items del pedido
            List<PedidoItem> items = pedidoItemRepository.findByPedidoId(id);
            pedidoItemRepository.deleteAll(items);
            
            // Eliminar pedido
            pedidoRepository.delete(pedido);
            
            return ResponseEntity.ok(Map.of(
                "message", "Pedido eliminado exitosamente"
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Error al eliminar pedido: " + e.getMessage()));
        }
    }

    // Obtener estadísticas de pedidos
    @GetMapping("/estadisticas")
    @Operation(summary = "Obtener estadísticas", description = "Retorna estadísticas sobre los pedidos")
    public ResponseEntity<?> getEstadisticas() {
        try {
            long totalPedidos = pedidoRepository.count();
            long pedidosPendientes = pedidoRepository.findByEstado("PENDIENTE").size();
            long pedidosEntregados = pedidoRepository.findByEstado("ENTREGADO").size();
            long pedidosCancelados = pedidoRepository.findByEstado("CANCELADO").size();
            
            return ResponseEntity.ok(Map.of(
                "totalPedidos", totalPedidos,
                "pendientes", pedidosPendientes,
                "entregados", pedidosEntregados,
                "cancelados", pedidosCancelados
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener estadísticas"));
        }
    }

    // Obtener items de un pedido
    @GetMapping("/{id}/items")
    @Operation(summary = "Obtener items del pedido", description = "Retorna todos los items de un pedido específico")
    public ResponseEntity<?> getPedidoItems(@PathVariable Long id) {
        try {
            List<PedidoItem> items = pedidoItemRepository.findByPedidoId(id);
            
            // Enriquecer items con información del producto
            List<Map<String,? extends Object>> itemsEnriquecidos = items.stream().map(item -> {
                Map<String, Object> itemMap = Map.of(
                    "id_item", item.getId_item(),
                    "productoId", item.getProductoId(),
                    "cantidad", item.getCantidad(),
                    "precioUnitario", item.getPrecioUnitario(),
                    "subtotal", item.getSubtotal()
                );
                
                // Obtener información del producto
                Optional<Product> producto = productRepository.findById(item.getProductoId());
                if (producto.isPresent()) {
                    Product p = producto.get();
                    return Map.of(
                        "id_item", item.getId_item(),
                        "productoId", item.getProductoId(),
                        "productoNombre", p.getNombre(),
                        "productoCategoria", p.getCat(),
                        "cantidad", item.getCantidad(),
                        "precioUnitario", item.getPrecioUnitario(),
                        "subtotal", item.getSubtotal(),
                        "productoImagen", p.getDetail() // Asumiendo que detail podría ser una URL de imagen
                    );
                }
                return itemMap;
            }).collect(Collectors.toList());
            
            return ResponseEntity.ok(itemsEnriquecidos);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Error al obtener items: " + e.getMessage()));
        }
    }

    // Método auxiliar para devolver stock
    private void devolverStockPedido(Pedido pedido) {
        List<PedidoItem> items = pedidoItemRepository.findByPedidoId(pedido.getId_pedido());
        
        for (PedidoItem item : items) {
            Optional<Product> productoOpt = productRepository.findById(item.getProductoId());
            if (productoOpt.isPresent()) {
                Product producto = productoOpt.get();
                producto.setStock(producto.getStock() + item.getCantidad());
                productRepository.save(producto);
            }
        }
    }
}