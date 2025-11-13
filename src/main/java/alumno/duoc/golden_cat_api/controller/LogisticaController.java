package duoc.nocodenolife.perfulandia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import duoc.nocodenolife.perfulandia.model.Pedido;
import duoc.nocodenolife.perfulandia.service.LogisticaService;
import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

//url: localhost:8081/
@RestController
@RequestMapping("/api/v1/logi")
@Tag(name = "Logistica", description = "Aqui se gestionan los servicios de despacho de productos")
public class LogisticaController {

	
	@Autowired
	private LogisticaService logisticaService;
	
	@GetMapping
	@Operation(summary = "Listar pedidos", description = "Se muestra una lista de todos los pedidos")
	public List<Pedido> listarPedidos() {
		return logisticaService.getListaPedidos();
	}
	@GetMapping("/{id}")
	@Operation(summary = "Buscar pedido", description = "Se busca un pedido a travez de su ID")
	public Pedido buscarPedido(@PathVariable int idPedido) {
		return logisticaService.getPedidoId(idPedido);
	}
	@PostMapping("/{id}")
	@Operation(summary = "Buscar pedido", description = "Se busca un pedido a travez de su ID")
	public Pedido crearPedido(@RequestBody Pedido pedido) {
		return logisticaService.nuevoPedido(pedido);
	}
	@DeleteMapping("/{id}")
	@Operation(summary = "Borrar pedido", description = "Se elimina un pedido a travez de su ID")
	public String eliminarProducto(@PathVariable int idPedido) {
		return logisticaService.expungePedido(idPedido);
	}
	@PutMapping("/actualizar_estado/{id}")
	@Operation(summary = "Atualizar estado", description = "Se actualiza el estado de un pedido o envio")
	public String actualizarEstado(@PathVariable int id, @RequestParam String estado) {
		return "Estado de Pedido Cambiado a " + estado;
	}
	@Operation(summary = "Listar pedidos por estado", description = "Devuelve todos los pedidos con el estado indicado (no sensible a mayúsculas)")
	@GetMapping("/estado/{estado}")
	public List<Pedido> listarPedidosPorEstado(@PathVariable String estado) {
		return logisticaService.getPedidosPorEstado(estado);
	}
	@Operation(summary = "Listar pedidos por dirección", description = "Devuelve todos los pedidos cuya dirección de entrega contenga la palabra indicada")
	@GetMapping("/direccion/{direccion}")
	public List<Pedido> listarPedidosPorDireccion(@PathVariable String direccion) {
		return logisticaService.getPedidosPorDireccion(direccion);
	}


}
