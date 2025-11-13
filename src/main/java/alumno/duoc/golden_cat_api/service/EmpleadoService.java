package duoc.nocodenolife.perfulandia.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import duoc.nocodenolife.perfulandia.model.Empleado;
import duoc.nocodenolife.perfulandia.repository.EmpleadoRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class EmpleadoService {

	@Autowired

	private EmpleadoRepository empleadoRepository;

	public List<Empleado> getListaEmpleados(){
		return empleadoRepository.findAll();//nombre metodo
	}

	public Empleado getEmpleadoRut(int run){
		return empleadoRepository.findById(run).get();
	}

	public Empleado getEmpleadoNombre(String nombreEmpleado){

		return empleadoRepository.buscarPorNombre(nombreEmpleado);
	}

	public Empleado getEmpleadoCargo(int cargo){

		return empleadoRepository.buscarPorCargo(cargo);
	}

	public void eliminar(int run) {
		empleadoRepository.deleteById(run);
	}

	public Empleado guardarEmpleado(Empleado usu) {
		return empleadoRepository.save(usu);
	}

	public Empleado actualizarContrasenia(int run, String newContrasenia) {
		return empleadoRepository.actualizarContrasenia(run, newContrasenia);
	}
}
