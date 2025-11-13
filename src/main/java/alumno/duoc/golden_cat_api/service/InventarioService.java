package duoc.nocodenolife.perfulandia.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import duoc.nocodenolife.perfulandia.model.Producto;
import duoc.nocodenolife.perfulandia.repository.InventarioRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class InventarioService {

	@Autowired
	private InventarioRepository inventarioRepository;

	public List<Producto> getListaProductoInventario(){
		return inventarioRepository.findAll();
	}

	public Producto getProductoiD(String id_producto){
		return inventarioRepository.findById(id_producto).get();
	}
	
	public Producto getProductoNombre(String nombre){
		return inventarioRepository.buscarPorNombre(nombre);
	}
	
	public String deleteProducto(String id_producto){
		inventarioRepository.deleteById(id_producto);
		return "Producto eliminado exitosamente.";
	}

	public Producto postProducto(Producto producto) {
		return inventarioRepository.save(producto);
	}
	
	public Producto updateDiscountProducto(String id, int nuevoDescuento) {
    	inventarioRepository.actualizarDescuento(id, nuevoDescuento);
    	return inventarioRepository.findById(id).orElse(null);
	}

	public Producto updateStockProducto(String id, int stock) {
    	int nuevoStock = inventarioRepository.findById(id).orElse(null).getStock() + stock;

    	inventarioRepository.actualizarStock(id, nuevoStock);
    	return inventarioRepository.findById(id).orElse(null);
	}
	//NUEVO
	public int consultarStock(String idProducto) {
    	return inventarioRepository.obtenerStock(idProducto);
	}
	//nuevo
	public List<Producto> obtenerProductosBajoStock() {
    	return inventarioRepository.listarProductosBajoStock();
	}

}
