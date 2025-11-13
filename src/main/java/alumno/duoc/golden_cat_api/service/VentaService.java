package duoc.nocodenolife.perfulandia.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import duoc.nocodenolife.perfulandia.model.Venta;
import duoc.nocodenolife.perfulandia.repository.VentaRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class VentaService {
	@Autowired
	private VentaRepository ventaRepository;

	public List<Venta> getListaVenta() {
		return ventaRepository.findAll();
	}

	public Venta getVentaPorId(int idVenta) {
		return ventaRepository.findById(idVenta).get();
	}

	public Venta addVenta(Venta venta) {
		return ventaRepository.save(venta);
	}

	// public String expungeVenta(int idVenta) {
	// 	ventaRepository.deleteById(idVenta);
	// 	return "Registro de Venta Eliminada exitosamente";
	// }

	public List<Venta> getListaVentaPorEmpleado(int run) {
		return ventaRepository.obtenerListaVentasEmpleado(run);
	}

	public List<Venta> getListaVentaContieneProducto(String idProducto) {
		return ventaRepository.obtenerListaVentasContieneProducto(idProducto);
	}

	public String expungeVenta(int idVenta) {
    if (!ventaRepository.existsById(idVenta)) {
        throw new RuntimeException("Venta con ID " + idVenta + " no encontrada");
    }
    ventaRepository.deleteById(idVenta);
    	return "Venta eliminada exitosamente";
	}

	public List<Venta> getVentasPorRangoFechas(String desde, String hasta) {
    	return ventaRepository.buscarPorFecha(desde, hasta);
	}
	//fecha formato xx-xx-xxx
}
