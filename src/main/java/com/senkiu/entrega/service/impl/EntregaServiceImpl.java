package com.senkiu.entrega.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.senkiu.entrega.dto.ActualizarEntregaRequest;
import com.senkiu.entrega.dto.EntregaResponse;
import com.senkiu.entrega.model.Entrega;
import com.senkiu.entrega.repository.EntregaRepository;
import com.senkiu.entrega.service.EntregaService;
import com.senkiu.estado.entrega.model.EstadoEntrega;
import com.senkiu.estado.entrega.repository.EstadoEntregaRepository;
import com.senkiu.notificacion.service.NotificacionService;
import com.senkiu.orden.model.Orden;
import com.senkiu.orden.repository.OrdenRepository;

@Service
public class EntregaServiceImpl implements EntregaService {

	private final EntregaRepository entregaRepository;
	private final OrdenRepository ordenRepository;
	private final EstadoEntregaRepository estadoRepository;
	private final NotificacionService notificacionService;

	public EntregaServiceImpl(EntregaRepository entregaRepository, OrdenRepository ordenRepository,
			EstadoEntregaRepository estadoRepository, NotificacionService notificacionService) {
		this.entregaRepository = entregaRepository;
		this.ordenRepository = ordenRepository;
		this.estadoRepository = estadoRepository;
		this.notificacionService = notificacionService;
	}

	@Override
	public void crearEntregaInicial(Long ordenId) {

		Orden orden = ordenRepository.findById(ordenId).orElseThrow();
		EstadoEntrega estado = estadoRepository.findByNombre("PENDIENTE").orElseThrow();
		String direccion;

		if (orden.getUsuario() != null) {
			direccion = orden.getUsuario().getDireccion();
		} else {
			direccion = orden.getDireccionEntrega();
		}

		if (direccion == null || direccion.isBlank()) {
			throw new RuntimeException(
					"Debe completar una dirección antes de realizar una compra"
			);
		}

		Entrega entrega = new Entrega();
		entrega.setOrden(orden);
		entrega.setEstado(estado);
		entrega.setDireccion(direccion);
		entregaRepository.save(entrega);
	}

	@Override
	public EntregaResponse obtenerPorOrden(Long ordenId) {
		Entrega entrega = entregaRepository.findByOrdenId(ordenId).orElseThrow();

		return mapToResponse(entrega);
	}

	@Override
	public EntregaResponse actualizarEstado(Long ordenId, ActualizarEntregaRequest request) {

		Entrega entrega = entregaRepository.findByOrdenId(ordenId).orElseThrow();
		EstadoEntrega estado = estadoRepository.findByNombre(request.getEstado()).orElseThrow();

		entrega.setEstado(estado);
		entrega.setComentario(request.getComentario());

		if (estado.getNombre().equals("EN_CAMINO")) {
			entrega.setFechaEnvio(LocalDateTime.now());
		}

		if (estado.getNombre().equals("ENTREGADO")) {
			entrega.setFechaEntrega(LocalDateTime.now());
		}

		entregaRepository.save(entrega);

		Orden orden = entrega.getOrden();

		if (orden.getUsuario() != null) {

			String titulo;
			String mensaje;

			switch (estado.getNombre()) {

				case "PENDIENTE":

					titulo = "Entrega pendiente";

					mensaje =
							"La entrega de su pedido se encuentra pendiente.";

					break;

				case "EN_CAMINO":

					titulo = "Pedido en camino";

					mensaje =
							"Su pedido ha sido despachado y se encuentra en camino.";

					break;

				case "ENTREGADO":

					titulo = "Pedido entregado";

					mensaje =
							"Su pedido fue entregado correctamente.";

					break;

				case "CANCELADO":

					titulo = "Entrega cancelada";

					mensaje =
							"La entrega de su pedido ha sido cancelada.";

					break;

				default:

					titulo = "Actualización de entrega";

					mensaje =
							"El estado de la entrega ha sido actualizado.";
			}

			notificacionService.crear(
					orden.getUsuario().getId(),
					"ENTREGA",
					titulo,
					mensaje
			);
		}

		return mapToResponse(entrega);
	}

	private EntregaResponse mapToResponse(Entrega entrega) {
		EntregaResponse response = new EntregaResponse();

		response.setId(entrega.getId());
		response.setOrdenId(entrega.getOrden().getId());
		response.setEstado(entrega.getEstado().getNombre());
		response.setDireccion(entrega.getDireccion());
		response.setFechaEnvio(entrega.getFechaEnvio());
		response.setFechaEntrega(entrega.getFechaEntrega());
		response.setComentario(entrega.getComentario());

		return response;
	}
}