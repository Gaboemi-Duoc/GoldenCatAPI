package alumno.duoc.golden_cat_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@CrossOrigin(origins = "*")
@Tag(name = "Pedido Controller", description = "API para gestionar pedidos de usuarios")
public class PedidoController {

    private final PedidoRepository pedidoRepository;
    private final PedidoItemRepository pedidoItemRepository;
    private final ProductRepository productRepository;
    private final UsuarioRepository usuarioRepository;

    public PedidoController(PedidoRepository pedidoRepository,
                          PedidoItemRepository pedidoItemRepository,
                          ProductRepository productRepository,
                          UsuarioRepository usuarioRepository) {
        this.pedidoRepository = pedidoRepository;
        this.pedidoItemRepository = pedidoItemRepository;
        this.productRepository = productRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // DTO para evitar referencias circulares
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PedidoDTO {
        private Long id_pedido;
        private String fechaCreacion;
        private String estado;
        private Integer total;
        private String direccionEnvio;
        private Long usuarioId;
        private List<PedidoItemDTO> items;
        
        // Constructor from Pedido entity
        public PedidoDTO(Pedido pedido) {
            this.id_pedido = pedido.getId_pedido();
            this.fechaCreacion = pedido.getFechaCreacion().toString();
            this.estado = pedido.getEstado();
            this.total = pedido.getTotal();
            this.direccionEnvio = pedido.getDireccionEnvio();
            this.usuarioId = pedido.getUsuarioId();
        }
        
        // Getters y setters
        public Long getId_pedido() { return id_pedido; }
        public void setId_pedido(Long id_pedido) { this.id_pedido = id_pedido; }
        
        public String getFechaCreacion() { return fechaCreacion; }
        public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }
        
        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }
        
        public Integer getTotal() { return total; }
        public void setTotal(Integer total) { this.total = total; }
        
        public String getDireccionEnvio() { return direccionEnvio; }
        public void setDireccionEnvio(String direccionEnvio) { this.direccionEnvio = direccionEnvio; }
        
        public Long getUsuarioId() { return usuarioId; }
        public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
        
        public List<PedidoItemDTO> getItems() { return items; }
        public void setItems(List<PedidoItemDTO> items) { this.items = items; }
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PedidoItemDTO {
        private Long id_item;
        private Long productoId;
        private String productoNombre;
        private Integer cantidad;
        private Integer precioUnitario;
        private Integer subtotal;
        
        // Constructor from PedidoItem entity
        public PedidoItemDTO(PedidoItem item) {
            this.id_item = item.getId_item();
            this.productoId = item.getProductoId();
            this.cantidad = item.getCantidad();
            this.precioUnitario = item.getPrecioUnitario();
            this.subtotal = item.getSubtotal();
        }
        
        // Getters y setters
        public Long getId_item() { return id_item; }
        public void setId_item(Long id_item) { this.id_item = id_item; }
        
        public Long getProductoId() { return productoId; }
        public void setProductoId(Long productoId) { this.productoId = productoId; }
        
        public String getProductoNombre() { return productoNombre; }
        public void setProductoNombre(String productoNombre) { this.productoNombre = productoNombre; }
        
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
        
        public Integer getPrecioUnitario() { return precioUnitario; }
        public void setPrecioUnitario(Integer precioUnitario) { this.precioUnitario = precioUnitario; }
        
        public Integer getSubtotal() { return subtotal; }
        public void setSubtotal(Integer subtotal) { this.subtotal = subtotal; }
    }

    // Obtener todos los pedidos
    @GetMapping
    @Operation(summary = "Obtener todos los pedidos", description = "Retorna una lista de todos los pedidos")
    public ResponseEntity<?> getAllPedidos() {
        try {
            List<Pedido> pedidos = pedidoRepository.findAll();
            List<PedidoDTO> pedidosDTO = pedidos.stream()
                .map(pedido -> {
                    PedidoDTO dto = new PedidoDTO(pedido);
                    // No cargar items aquí para mejor performance
                    return dto;
                })
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(pedidosDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener pedidos: " + e.getMessage()));
        }
    }

    // Obtener pedido por ID
    @GetMapping("/{id}")
    @Operation(summary = "Obtener pedido por ID", description = "Retorna un pedido específico por su ID")
    public ResponseEntity<?> getPedidoById(@PathVariable Long id) {
        try {
            Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);
            if (!pedidoOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Pedido no encontrado"));
            }
            
            Pedido pedido = pedidoOpt.get();
            PedidoDTO pedidoDTO = convertToDTO(pedido);
            
            return ResponseEntity.ok(pedidoDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener pedido: " + e.getMessage()));
        }
    }

    // Obtener pedidos por usuario
    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Obtener pedidos por usuario", description = "Retorna todos los pedidos de un usuario específico")
    public ResponseEntity<?> getPedidosByUsuario(@PathVariable Long usuarioId) {
        try {
            // Verificar que el usuario existe
            Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
            if (!usuario.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Usuario no encontrado"));
            }
            
            List<Pedido> pedidos = pedidoRepository.findByUsuarioId(usuarioId);
            
            // Convertir a DTOs para evitar referencias circulares
            List<PedidoDTO> pedidosDTO = pedidos.stream()
                .map(this::convertToDTO) 
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(pedidosDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener pedidos por usuario: " + e.getMessage(),
                                 "detalle", e.toString()));
        }
    }

    // Obtener pedidos por estado
    @GetMapping("/estado/{estado}")
    @Operation(summary = "Obtener pedidos por estado", description = "Retorna pedidos filtrados por estado")
    public ResponseEntity<?> getPedidosByEstado(@PathVariable String estado) {
        try {
            List<Pedido> pedidos = pedidoRepository.findByEstado(estado);
            List<PedidoDTO> pedidosDTO = pedidos.stream()
                .map(PedidoDTO::new)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(pedidosDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener pedidos por estado: " + e.getMessage()));
        }
    }

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
                
                // Reducir stock
                product.setStock(product.getStock() - cantidad);
                productRepository.save(product);
                
                PedidoItem pedidoItem = new PedidoItem();
                pedidoItem.setPedido(savedPedido);
                pedidoItem.setProductoId(productoId);
                pedidoItem.setCantidad(cantidad);
                pedidoItem.setPrecioUnitario(precioUnitario);
                pedidoItem.setSubtotal(subtotal);
                
                pedidoItemRepository.save(pedidoItem);
            }
            
            // Convertir a DTO para la respuesta
            PedidoDTO responseDTO = convertToDTO(savedPedido);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                        "message", "Pedido creado exitosamente",
                        "pedido", responseDTO
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
            
            PedidoDTO pedidoDTO = new PedidoDTO(updatedPedido);
            
            return ResponseEntity.ok(Map.of(
                "message", "Estado del pedido actualizado",
                "pedido", pedidoDTO
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Error al actualizar estado: " + e.getMessage()));
        }
    }

    // Obtener items de un pedido
    @GetMapping("/{id}/items")
    @Operation(summary = "Obtener items del pedido", description = "Retorna todos los items de un pedido específico")
    public ResponseEntity<?> getPedidoItems(@PathVariable Long id) {
        try {
            List<PedidoItem> items = pedidoItemRepository.findByPedidoId(id);
            
            List<PedidoItemDTO> itemsDTO = items.stream().map(item -> {
                PedidoItemDTO dto = new PedidoItemDTO(item);
                
                // Agregar información adicional del producto
                Optional<Product> producto = productRepository.findById(item.getProductoId());
                if (producto.isPresent()) {
                    Product p = producto.get();
                    dto.setProductoNombre(p.getNombre());
                }
                
                return dto;
            }).collect(Collectors.toList());
            
            return ResponseEntity.ok(itemsDTO);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Error al obtener items: " + e.getMessage()));
        }
    }

    // Método auxiliar para convertir Pedido a DTO con items
    private PedidoDTO convertToDTO(Pedido pedido) {
        PedidoDTO dto = new PedidoDTO(pedido);
        
        // Cargar items si es necesario
        List<PedidoItem> items = pedidoItemRepository.findByPedidoId(pedido.getId_pedido());
        List<PedidoItemDTO> itemsDTO = items.stream()
            .map(item -> {
                PedidoItemDTO itemDTO = new PedidoItemDTO(item);
                
                // Agregar nombre del producto
                Optional<Product> producto = productRepository.findById(item.getProductoId());
                if (producto.isPresent()) {
                    itemDTO.setProductoNombre(producto.get().getNombre());
                }
                
                return itemDTO;
            })
            .collect(Collectors.toList());
        
        dto.setItems(itemsDTO);
        return dto;
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