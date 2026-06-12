package com.senkiu.producto.service.impl;

import org.springframework.stereotype.Service;

import com.senkiu.producto.service.ProductoService;
import com.senkiu.solicitud.producto.model.SolicitudProducto;
import com.senkiu.producto.repository.ProductoRepository;
import com.senkiu.producto.dto.ProductoResponse;
import com.senkiu.producto.model.Producto;
import com.senkiu.producto.mapper.ProductoMapper;
import com.senkiu.empresa.model.Empresa;
import com.senkiu.empresa.repository.EmpresaRepository;
import com.senkiu.estado.publicacion.repository.EstadoPublicacionRepository;
import com.senkiu.estado.publicacion.model.EstadoPublicacion;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

	private final ProductoRepository productoRepository;
	private final EmpresaRepository empresaRepository;
	private final EstadoPublicacionRepository estadoRepository;

	public ProductoServiceImpl(ProductoRepository productoRepository, EmpresaRepository empresaRepository,
			EstadoPublicacionRepository estadoRepository) {
		this.productoRepository = productoRepository;
		this.empresaRepository = empresaRepository;
		this.estadoRepository = estadoRepository;
	}

	@Override
	public Producto crearDesdeSolicitud(SolicitudProducto sol) {
		EstadoPublicacion publicado = estadoRepository.findByNombre("PUBLICADO")
				.orElseThrow(() -> new RuntimeException("Estado PUBLICADO no existe"));

		Producto producto = new Producto();

		producto.setNombre(sol.getNombre());
		producto.setDescripcion(sol.getDescripcion());
		producto.setPrecio(sol.getPrecio());
		producto.setStock(sol.getStock());
		producto.setTiempoEntrega(sol.getTiempoEntrega());
		producto.setEmpresa(empresaRepository.findById(sol.getEmpresaId())
				.orElseThrow(() -> new RuntimeException("Empresa no existe")));
		producto.setCategoria(sol.getCategoria());
		producto.setEstado(publicado);

		return productoRepository.save(producto);
	}

	@Override
	public List<ProductoResponse> listarPorEmpresa(Long empresaId) {
		return productoRepository.findByEmpresaId(empresaId).stream().map(ProductoMapper::toDto).toList();
	}

	@Override
	public List<ProductoResponse> listarPublicados() {
		return productoRepository.findByEstadoNombre("PUBLICADO").stream().map(p -> {
			ProductoResponse dto = new ProductoResponse();

			dto.setId(p.getId());
			dto.setNombre(p.getNombre());
			dto.setPrecio(p.getPrecio());
			dto.setStock(p.getStock());
			dto.setImagenUrl(p.getImagenUrl());
			dto.setEmpresaId(p.getEmpresa().getId());
			dto.setCategoria(p.getCategoria().getNombre());
			dto.setEstado(p.getEstado().getNombre());
			dto.setEmpresa(p.getEmpresa().getNombre());

			return dto;
		}).toList();
	}

	@Override
	public List<ProductoResponse> listarPublicados(String nombre) {
		List<Producto> productos;

		if (nombre == null || nombre.isBlank()) {
			productos = productoRepository.findByEstadoNombre("PUBLICADO");

		} else {
			productos = productoRepository.findByEstadoNombreAndNombreContainingIgnoreCase("PUBLICADO", nombre);
		}

		return productos.stream().map(p -> {
			ProductoResponse dto = new ProductoResponse();

			dto.setId(p.getId());
			dto.setNombre(p.getNombre());
			dto.setPrecio(p.getPrecio());
			dto.setStock(p.getStock());
			dto.setImagenUrl(p.getImagenUrl());

			dto.setEmpresaId(p.getEmpresa().getId());

			dto.setCategoria(p.getCategoria().getNombre());

			dto.setEstado(p.getEstado().getNombre());

			dto.setEmpresa(p.getEmpresa().getNombre());

			return dto;
		}).toList();
	}

	@Override
	public List<ProductoResponse> listarPublicados(String nombre, String categoria) {

		List<Producto> productos;

		boolean tieneNombre = nombre != null && !nombre.isBlank();

		boolean tieneCategoria = categoria != null && !categoria.isBlank();

		if (!tieneNombre && !tieneCategoria) {

			productos = productoRepository.findByEstadoNombre("PUBLICADO");

		} else if (tieneNombre && !tieneCategoria) {

			productos = productoRepository.findByEstadoNombreAndNombreContainingIgnoreCase("PUBLICADO", nombre);

		} else if (!tieneNombre) {

			productos = productoRepository.findByEstadoNombreAndCategoriaNombre("PUBLICADO", categoria);

		} else {

			productos = productoRepository
					.findByEstadoNombreAndNombreContainingIgnoreCaseAndCategoriaNombre("PUBLICADO", nombre, categoria);
		}

		return productos.stream().map(p -> {

			ProductoResponse dto = new ProductoResponse();

			dto.setId(p.getId());
			dto.setNombre(p.getNombre());
			dto.setPrecio(p.getPrecio());
			dto.setStock(p.getStock());
			dto.setImagenUrl(p.getImagenUrl());
			dto.setDescripcion(p.getDescripcion());

			dto.setEmpresaId(p.getEmpresa().getId());

			dto.setCategoria(p.getCategoria().getNombre());

			dto.setEstado(p.getEstado().getNombre());

			dto.setEmpresa(p.getEmpresa().getNombre());

			return dto;
		}).toList();
	}

	@Override
	public void actualizarPrecioYStock(Long productoId, Long usuarioId, BigDecimal precio, Integer stockAgregar) {

		Producto producto = productoRepository.findById(productoId)
				.orElseThrow(() -> new RuntimeException("Producto no encontrado"));

		Empresa empresa = empresaRepository.findByUsuarioId(usuarioId)
				.orElseThrow(() -> new RuntimeException("Empresa no encontrada"));

		if (!producto.getEmpresa().getId().equals(empresa.getId())) {

			throw new RuntimeException("No puedes modificar productos de otra empresa");
		}

		if (!"PUBLICADO".equals(producto.getEstado().getNombre())) {

			throw new RuntimeException("Solo se pueden editar productos publicados");
		}

		if (precio == null && stockAgregar == null) {

			throw new RuntimeException("Debes modificar al menos un campo");
		}

		if (precio != null) {

			if (precio.compareTo(BigDecimal.ZERO) <= 0) {

				throw new RuntimeException("El precio debe ser mayor a cero");
			}

			producto.setPrecio(precio);
		}

		if (stockAgregar != null) {

			if (stockAgregar < 0) {

				throw new RuntimeException("El stock a agregar no puede ser negativo");
			}

			producto.setStock(producto.getStock() + stockAgregar);
		}

		productoRepository.save(producto);
	}
}