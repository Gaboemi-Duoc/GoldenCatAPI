package duoc.nocodenolife.perfulandia.controller;

import org.springframework.beans.factory.annotation.Autowired;
//url: localhost:8081/
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import duoc.nocodenolife.perfulandia.model.Producto;

import duoc.nocodenolife.perfulandia.service.InventarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;


import java.util.List;

@RestController
@RequestMapping("/api/v1/inv")
@Tag(name = "Inventario", description = "Aqui se gestiona el inventario")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;
    
    @GetMapping
    @Operation(summary = "Listar productos", description = "Se listan los productos disponibles")
	public List<Producto> listarProductos() {
		return inventarioService.getListaProductoInventario();
	}
	@GetMapping("/{id}")
    @Operation(summary = "Buscar producto por ID", description = "Se busca el producto ingresando si ID")
	public Producto buscarProducto(@PathVariable String id) {
		return inventarioService.getProductoiD(id);
	}

	@GetMapping("/nombre/{nombre}")
    @Operation(summary = "Buscar producto por nombre", description = "Se busca el producto ingresando su nombre")
	public Producto buscarProductoNombre(@PathVariable String nombre) {
		return inventarioService.getProductoNombre(nombre);
	}
	@DeleteMapping("/{id}")
    @Operation(summary = "Borra producto por ID", description = "Se elimina el producto ingresando su ID")
	public String eliminarProducto(@PathVariable String id) {
		return inventarioService.deleteProducto(id);
	}

    @PostMapping
    @Operation(summary = "Borra producto por ID", description = "Se elimina el producto ingresando su ID")
    public Producto crearProducto(@RequestBody Producto producto) {
        return inventarioService.postProducto(producto);
    }

    @PutMapping("/actualizar_descuento/{id}")
    @Operation(summary = "Agregar descuento", description = "Agregregar o modificar un descuento")
    public Producto actualizarDescuento(@PathVariable String id, @RequestParam int descuento) {
        return inventarioService.updateDiscountProducto(id, descuento);
    }

    @PutMapping("/actualizar_stock/{id}")
    @Operation(summary = "Actualizar stock", description = "Se actualiza el stock del producto del ID ingresado")
    public Producto actualizarStock(@PathVariable String id, @RequestParam int darStock) {
        return inventarioService.updateStockProducto(id, darStock);
    }	

    // 1. Obtener stock actual de un producto
    @GetMapping("/stock/{idProducto}")
    @Operation(summary = "Ver stock ", description = "Te muestra el stock de un producto al ingresar su ID")
    public ResponseEntity<String> getStock(@PathVariable String idProducto) {
        int stock = inventarioService.consultarStock(idProducto);
        return ResponseEntity.ok("Stock disponible: " + stock);
    }

    // 2. Listar productos con stock bajo (≤ 10)
    @GetMapping("/bajo-stock")
    @Operation(summary = "Stock bajo", description = "Te muestra una lista de todos los productos que tienen un bajo stock")
    public List<Producto> getProductosBajoStock() {
        return inventarioService.obtenerProductosBajoStock();
    }

}
