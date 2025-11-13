package duoc.nocodenolife.perfulandia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import duoc.nocodenolife.perfulandia.model.Empleado;
import duoc.nocodenolife.perfulandia.service.EmpleadoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;


import java.util.List;
//url: localhost:8081/
@RestController
@RequestMapping("/api/v1/empleado")
@Tag(name = "Empleados", description = "A qui se gestionan los empleados")
public class EmpleadoController {

	@Autowired
	private EmpleadoService empleadoService;
	

	@GetMapping
	@Operation(summary = "Lista empleados", description = "Se obtiene una lista de todos los empleados")
	public List<Empleado> listarEmpleados() {
		return empleadoService.getListaEmpleados();
	}

	@GetMapping("/buscar_por_rut/{run}")
	@Operation(summary = "Buscar por rut", description = "Se puede ingresar el rut y mostrara el empleado asociado a este")
	public Empleado buscarPorRut(@PathVariable int run) {
		return empleadoService.getEmpleadoRut(run);
	}
	@GetMapping("/buscar_por_nombre/{nombre}")
	@Operation(summary = "Buscar por nombre", description = "Se puede ingresa el nombre y te muestra los empleados con ese nombre")
	public Empleado buscarPorNombre(@PathVariable String nombre) {
		return empleadoService.getEmpleadoNombre(nombre);
	}
	@GetMapping("/buscar_por_cargo/{cargo}")
	@Operation(summary = "Buscar por cargo", description = "Se selecciona el cargo del empleado y muestra los empleados de ese cargo")
	public Empleado buscarPorCargo(@PathVariable int cargo) {
		return empleadoService.getEmpleadoCargo(cargo);
	}
	@DeleteMapping("/eliminar/{run}")
	@Operation(summary = "Eliminar por run", description = "Se ingresa el run del empleado y este se borra")
	public String eliminarEmpleado(@PathVariable int run) {
		empleadoService.eliminar(run);
		return "Empleado con RUN " + run + " eliminado correctamente.";
	}
	@PostMapping("/guardar")
	@Operation(summary = "Guardar", description = "Se guardan los datos ingresados")
	public Empleado guardarEmpleado(@RequestBody Empleado empleado) {
		return empleadoService.guardarEmpleado(empleado);
	}
	@PutMapping("/actualizar/{run}/contraseña")
	@Operation(summary = "Actualizar", description = "Sirve para actualizar los datos")
	public Empleado actualizarContrasenia(@PathVariable int run, @RequestParam String newContrasenia) {
		return empleadoService.actualizarContrasenia(run, newContrasenia);
	}

}
