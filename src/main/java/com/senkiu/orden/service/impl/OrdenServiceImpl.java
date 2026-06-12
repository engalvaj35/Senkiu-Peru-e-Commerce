package com.senkiu.orden.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.senkiu.carrito.model.Carrito;
import com.senkiu.carrito.model.CarritoItem;
import com.senkiu.carrito.repository.CarritoRepository;
import com.senkiu.entrega.service.EntregaService;
import com.senkiu.estado.orden.model.EstadoOrden;
import com.senkiu.estado.orden.repository.EstadoOrdenRepository;
import com.senkiu.orden.dto.OrdenRequest;
import com.senkiu.orden.model.Orden;
import com.senkiu.orden.model.OrdenDetalle;
import com.senkiu.orden.repository.OrdenRepository;
import com.senkiu.orden.service.OrdenService;
import com.senkiu.pago.dto.CrearPagoRequest;
import com.senkiu.pago.model.MetodoPago;
import com.senkiu.pago.model.Pago;
import com.senkiu.pago.model.TipoPago;
import com.senkiu.pago.service.PagoService;
import com.senkiu.producto.model.Producto;
import com.senkiu.producto.repository.ProductoRepository;
import com.senkiu.notificacion.service.NotificacionService;

@Service
@Transactional
public class OrdenServiceImpl implements OrdenService {

	private final OrdenRepository ordenRepository;
	private final CarritoRepository carritoRepository;
	private final EstadoOrdenRepository estadoOrdenRepository;
	private final ProductoRepository productoRepository;
	private final EntregaService entregaService;
	private final PagoService pagoService;
	private final NotificacionService notificacionService;

	public OrdenServiceImpl(OrdenRepository ordenRepository, CarritoRepository carritoRepository,
			EstadoOrdenRepository estadoOrdenRepository, ProductoRepository productoRepository,
			EntregaService entregaService, PagoService pagoService, NotificacionService notificacionService) {
		this.ordenRepository = ordenRepository;
		this.carritoRepository = carritoRepository;
		this.estadoOrdenRepository = estadoOrdenRepository;
		this.productoRepository = productoRepository;
		this.entregaService = entregaService;
		this.pagoService = pagoService;
		this.notificacionService = notificacionService;
	}

	@Override
	@Transactional
	public Orden crearOrden(OrdenRequest request) {

		Carrito carrito;

		if (request.getUsuarioId() != null) {

			carrito = carritoRepository
					.findByUsuarioIdAndEmpresaId(
							request.getUsuarioId(),
							request.getEmpresaId()
					)
					.orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

		} else {

			if (request.getSessionId() == null) {
				throw new RuntimeException("SessionId requerido para invitados");
			}

			carrito = carritoRepository
					.findBySessionIdAndEmpresaId(
							request.getSessionId(),
							request.getEmpresaId()
					)
					.orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
		}

		if (carrito.getItems().isEmpty()) {
			throw new RuntimeException("El carrito está vacío");
		}

		if (request.getUsuarioId() == null) {

			if (request.getEmail() == null ||
				request.getTelefono() == null ||
				request.getDireccion() == null) {

				throw new RuntimeException(
					"Debes completar email, teléfono y dirección"
				);
			}
		}

		EstadoOrden estado = estadoOrdenRepository.findByNombre("PENDIENTE")
				.orElseThrow(() -> new RuntimeException("Estado no encontrado"));

		Orden orden = new Orden();

		orden.setUsuario(carrito.getUsuario()); 
		orden.setSessionId(request.getSessionId()); 
		orden.setEmpresa(carrito.getEmpresa());
		orden.setEstado(estado);

		if (carrito.getUsuario() != null) {

			orden.setEmailContacto(
				carrito.getUsuario().getEmail()
			);

			orden.setTelefonoContacto(
				carrito.getUsuario().getCelular()
			);

			orden.setDireccionEntrega(
				carrito.getUsuario().getDireccion()
			);

		} else {

			orden.setEmailContacto(
				request.getEmail()
			);

			orden.setTelefonoContacto(
				request.getTelefono()
			);

			orden.setDireccionEntrega(
				request.getDireccion()
			);
		}

		BigDecimal total = BigDecimal.ZERO;

		for (CarritoItem item : carrito.getItems()) {

			Producto producto = item.getProducto();

			if (producto.getStock() < item.getCantidad()) {
				throw new RuntimeException("Stock insuficiente para " + producto.getNombre());
			}

			producto.setStock(producto.getStock() - item.getCantidad());
			productoRepository.save(producto);

			OrdenDetalle detalle = new OrdenDetalle();
			detalle.setOrden(orden);
			detalle.setProducto(producto);
			detalle.setCantidad(item.getCantidad());
			detalle.setPrecio(item.getPrecio());

			orden.getDetalles().add(detalle);

			total = total.add(
					item.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad()))
			);
		}

		orden.setTotal(total);

		Orden ordenGuardada = ordenRepository.save(orden);

		CrearPagoRequest pagoRequest = new CrearPagoRequest();
		pagoRequest.setUsuarioId(
				request.getUsuarioId() != null
						? request.getUsuarioId()
						: (carrito.getUsuario() != null ? carrito.getUsuario().getId() : null)
		);

		pagoRequest.setTipo(TipoPago.ORDEN);
		pagoRequest.setReferenciaId(ordenGuardada.getId());
		pagoRequest.setMonto(total);
		pagoRequest.setMetodo(request.getMetodoPago());

		Pago pago = pagoService.crearPago(pagoRequest);

		if (request.getMetodoPago() == MetodoPago.WALLET) {
			pagoService.procesarPagoWallet(pago.getId());
		}

		ordenGuardada.setPago(pago);
		ordenRepository.save(ordenGuardada);

		entregaService.crearEntregaInicial(ordenGuardada.getId());

		if (ordenGuardada.getUsuario() != null) {

			notificacionService.crear(
					ordenGuardada.getUsuario().getId(),
					"ORDEN",
					"Orden registrada",
					"Tu orden fue registrada correctamente."
			);
		}

		if (ordenGuardada.getEmpresa() != null
				&& ordenGuardada.getEmpresa().getUsuario() != null) {

			notificacionService.crear(
					ordenGuardada.getEmpresa()
							.getUsuario()
							.getId(),
					"ORDEN",
					"Nueva orden recibida",
					"Se ha registrado una nueva compra por S/ "
							+ ordenGuardada.getTotal()
			);
		}

		carrito.getItems().clear();
		carritoRepository.save(carrito);

		return ordenGuardada;
	}

	@Override
	public List<Orden> listarPorUsuario(Long usuarioId) {
		return ordenRepository.findByUsuarioId(usuarioId);
	}

	@Override
	public List<Orden> listarPorEmpresa(Long empresaId) {
		return ordenRepository.findByEmpresaId(empresaId);
	}

	@Override
	public void cambiarEstado(Long ordenId, String estadoNombre) {
		Orden orden = ordenRepository.findById(ordenId).orElseThrow(() -> new RuntimeException("Orden no encontrada"));

		EstadoOrden estado = estadoOrdenRepository.findByNombre(estadoNombre)
				.orElseThrow(() -> new RuntimeException("Estado no encontrado"));

		orden.setEstado(estado);

		ordenRepository.save(orden);

		if (orden.getUsuario() != null) {

			String titulo;
			String mensaje;

			switch (estadoNombre) {

				case "PENDIENTE":

					titulo = "Pedido registrado";

					mensaje =
							"Su pedido se encuentra pendiente de atención.";

					break;

				case "ATENDIDO":

					titulo = "Pedido en preparación";

					mensaje =
							"Su pedido está siendo preparado.";

					break;

				case "COMPLETADO":

					titulo = "Pedido completado";

					mensaje =
							"Su pedido ya ha sido completado.";

					break;

				case "CANCELADO":

					titulo = "Pedido cancelado";

					mensaje =
							"Su pedido ha sido cancelado.";

					break;

				default:

					titulo = "Actualización de pedido";

					mensaje =
							"El estado de su pedido ha sido actualizado.";
			}

			notificacionService.crear(
					orden.getUsuario().getId(),
					"ORDEN",
					titulo,
					mensaje
			);
		}
	}
}