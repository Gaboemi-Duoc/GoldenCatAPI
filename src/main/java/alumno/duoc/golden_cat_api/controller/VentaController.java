package duoc.nocodenolife.perfulandia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import duoc.nocodenolife.perfulandia.model.Venta;
import duoc.nocodenolife.perfulandia.service.VentaService;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

//url: localhost:8081/
@RestController
@RequestMapping("/api/v1/ventas")
@Tag(name = "Ventas", description = "aca se manejan las ventas")
public class VentaController {

	@Autowired
	private VentaService ventaService;
	@Operation(summary = "Listar ventas(?", description = "Se listan las ventas realizadas :)")

	@GetMapping
	public List<Venta> listarVentas() {
		return ventaService.getListaVenta();
	}

	@PostMapping
	public String crearVenta(@RequestBody int id) {
		return new String();
	}

	@GetMapping("/{id}")
	public Venta buscarVenta(@PathVariable int idVenta) {
		return ventaService.getVentaPorId(idVenta);
	}

	// @DeleteMapping("/{id}")
	// public String eliminarVenta(@PathVariable int id) {
	// 	return ventaService.expungeVenta(id);
	// }

	@GetMapping("/buscar_venta_por_empleado/{id}")
	public List<Venta> buscarVentasPorEmpleado(@PathVariable int runEmpleado) {
		return ventaService.getListaVentaPorEmpleado(runEmpleado);
	}

	@GetMapping("/buscar_venta_con_producto/{id}")
	public List<Venta> buscarVentasConProducto(@PathVariable String idProducto) {
		return ventaService.getListaVentaContieneProducto(idProducto);
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<String> eliminarVenta(@PathVariable int id) {
    	return ResponseEntity.ok(ventaService.expungeVenta(id));
	}
	@GetMapping("/filtrar_por_fecha/{desde}/{hasta}")
	public List<Venta> buscarVentasPorFecha(
        @PathVariable String desde,
        @PathVariable String hasta) {
    	return ventaService.getVentasPorRangoFechas(desde, hasta);
	}
	//fecha formato xx-xx-xxx
}
