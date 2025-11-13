package duoc.nocodenolife.perfulandia.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import duoc.nocodenolife.perfulandia.model.Pedido;
import duoc.nocodenolife.perfulandia.repository.LogisticaRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class LogisticaService {

	@Autowired
	private LogisticaRepository logisticaRepository;

	public List<Pedido> getListaPedidos() {
		return logisticaRepository.findAll();
	}

	public Pedido getPedidoId(int idPedido) {
		return logisticaRepository.findById(idPedido).get();
	}

	public Pedido nuevoPedido(Pedido ped) {
		return logisticaRepository.save(ped);
	}

	public String expungePedido(int idPedido) {
		logisticaRepository.deleteById(idPedido);
		return "Pedido eliminado exitosamente.";
	}

	public Pedido updateEstadoPedido(int id, String nuevoEstado) {
		return logisticaRepository.actualizarEstadoPedido(id, nuevoEstado);    
	}

	public List<Pedido> getPedidosPorEstado(String estado) {
    	return logisticaRepository.findByEstadoPedidoIgnoreCase(estado);
	}	

	public List<Pedido> getPedidosPorDireccion(String direccion) {
    	return logisticaRepository.findByDireccionEntregaParcial(direccion);
	}
}
