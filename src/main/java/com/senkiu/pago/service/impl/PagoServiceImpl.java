package com.senkiu.pago.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.senkiu.donacion.model.Donacion;
import com.senkiu.donacion.repository.DonacionRepository;
import com.senkiu.estado.orden.model.EstadoOrden;
import com.senkiu.estado.orden.repository.EstadoOrdenRepository;
import com.senkiu.estado.pago.model.EstadoPago;
import com.senkiu.estado.pago.repository.EstadoPagoRepository;
import com.senkiu.estado.suscripcion.model.EstadoSuscripcion;
import com.senkiu.estado.suscripcion.repository.EstadoSuscripcionRepository;
import com.senkiu.estado.ticket.model.EstadoTicket;
import com.senkiu.estado.ticket.repository.EstadoTicketRepository;
import com.senkiu.orden.model.Orden;
import com.senkiu.orden.repository.OrdenRepository;
import com.senkiu.pago.dto.CrearPagoRequest;
import com.senkiu.pago.model.MetodoPago;
import com.senkiu.pago.model.Pago;
import com.senkiu.pago.model.TipoPago;
import com.senkiu.pago.repository.PagoRepository;
import com.senkiu.pago.service.PagoService;
import com.senkiu.programa.service.ProgramaSocialService;
import com.senkiu.rifa.model.Rifa;
import com.senkiu.rifa.repository.RifaRepository;
import com.senkiu.suscripcion.model.Suscripcion;
import com.senkiu.suscripcion.repository.SuscripcionRepository;
import com.senkiu.ticket.model.Ticket;
import com.senkiu.ticket.repository.TicketRepository;
import com.senkiu.usuario.model.Usuario;
import com.senkiu.usuario.repository.UsuarioRepository;
import com.senkiu.wallet.service.WalletService;

@Service
public class PagoServiceImpl implements PagoService {

	private final PagoRepository pagoRepository;
	private final UsuarioRepository usuarioRepository;
	private final EstadoPagoRepository estadoPagoRepository;
	private final OrdenRepository ordenRepository;
	private final EstadoOrdenRepository estadoOrdenRepository;
	private final DonacionRepository donacionRepository;
	private final ProgramaSocialService programaSocialService;
	private final TicketRepository ticketRepository;
	private final EstadoTicketRepository estadoTicketRepository;
	private final RifaRepository rifaRepository;
	private final SuscripcionRepository suscripcionRepository;
	private final EstadoSuscripcionRepository estadoSuscripcionRepository;
	private final WalletService walletService;

	public PagoServiceImpl(PagoRepository pagoRepository, UsuarioRepository usuarioRepository,
			EstadoPagoRepository estadoPagoRepository, OrdenRepository ordenRepository,
			EstadoOrdenRepository estadoOrdenRepository, DonacionRepository donacionRepository,
			ProgramaSocialService programaSocialService, TicketRepository ticketRepository,
			EstadoTicketRepository estadoTicketRepository, RifaRepository rifaRepository,
			SuscripcionRepository suscripcionRepository, EstadoSuscripcionRepository estadoSuscripcionRepository,
			WalletService walletService) {
		this.pagoRepository = pagoRepository;
		this.usuarioRepository = usuarioRepository;
		this.estadoPagoRepository = estadoPagoRepository;
		this.ordenRepository = ordenRepository;
		this.estadoOrdenRepository = estadoOrdenRepository;
		this.donacionRepository = donacionRepository;
		this.programaSocialService = programaSocialService;
		this.ticketRepository = ticketRepository;
		this.estadoTicketRepository = estadoTicketRepository;
		this.rifaRepository = rifaRepository;
		this.suscripcionRepository = suscripcionRepository;
		this.estadoSuscripcionRepository = estadoSuscripcionRepository;
		this.walletService = walletService;
	}

	@Override
	@Transactional
	public Pago crearPago(CrearPagoRequest request) {
		
		Usuario usuario = null;

		if (request.getUsuarioId() != null) {
			usuario = usuarioRepository.findById(request.getUsuarioId())
					.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
		}

		if (request.getTipo() == TipoPago.RECARGA_WALLET) {
			validarLimiteRecarga(request.getUsuarioId(), request.getMonto());

			if (request.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
				throw new RuntimeException("El monto debe ser mayor a cero");
			}
		}

		EstadoPago estado = estadoPagoRepository.findByNombre("PENDIENTE")
				.orElseThrow(() -> new RuntimeException("Estado PENDIENTE no existe"));

		Pago pago = new Pago();

		pago.setUsuario(usuario);
		pago.setTipo(request.getTipo());
		pago.setReferenciaId(request.getReferenciaId());
		pago.setMonto(request.getMonto());
		pago.setMetodo(request.getMetodo());
		pago.setEstado(estado);

		return pagoRepository.save(pago);
	}

	@Override
	@Transactional
	public Pago confirmarPago(Long pagoId) {
		Pago pago = pagoRepository.findById(pagoId).orElseThrow(() -> new RuntimeException("Pago no encontrado"));

		if ("COMPLETADO".equals(pago.getEstado().getNombre())) {
			return pago;
		}

		EstadoPago estado = estadoPagoRepository.findByNombre("COMPLETADO")
				.orElseThrow(() -> new RuntimeException("Estado no existe"));

		pago.setEstado(estado);

		if (pago.getTipo() == TipoPago.ORDEN) {
			Orden orden = ordenRepository.findById(pago.getReferenciaId())
					.orElseThrow(() -> new RuntimeException("Orden no encontrada"));

			EstadoOrden estadoOrden = estadoOrdenRepository.findByNombre("ATENDIDO")
					.orElseThrow(() -> new RuntimeException("Estado no existe"));

			orden.setEstado(estadoOrden);

			ordenRepository.save(orden);
		}

		if (pago.getTipo() == TipoPago.DONACION) {

			Donacion donacion = donacionRepository.findById(pago.getReferenciaId())
					.orElseThrow(() -> new RuntimeException("Donación no encontrada"));

			programaSocialService.incrementarMonto(donacion.getPrograma().getId(), donacion.getMonto());
		}

		if (pago.getTipo() == TipoPago.TICKET) {
			List<Ticket> tickets = ticketRepository.findByPagoId(pago.getId());

			EstadoTicket estadoActivo = estadoTicketRepository.findByNombre("ACTIVO").orElseThrow();

			for (Ticket ticket : tickets) {
				ticket.setEstado(estadoActivo);

				ticketRepository.save(ticket);

				Rifa rifa = ticket.getRifa();

				rifa.setTicketsVendidos(rifa.getTicketsVendidos() + 1);

				rifaRepository.save(rifa);
			}

			if (!tickets.isEmpty()) {
				Rifa rifa = tickets.get(0).getRifa();

				int cantidadTickets = tickets.size();
				programaSocialService.incrementarMonto(rifa.getPrograma().getId(),
						rifa.getPrecioTicket().multiply(BigDecimal.valueOf(cantidadTickets)));
			}
		}

		if (pago.getTipo() == TipoPago.SUSCRIPCION) {
			Suscripcion suscripcion = suscripcionRepository.findById(pago.getReferenciaId())
					.orElseThrow(() -> new RuntimeException("Suscripción no encontrada"));

			EstadoSuscripcion estadoActiva = estadoSuscripcionRepository.findByNombre("ACTIVA")
					.orElseThrow(() -> new RuntimeException("Estado ACTIVA no existe"));
			suscripcion.setEstado(estadoActiva);
			LocalDateTime ahora = LocalDateTime.now();
			suscripcion.setFechaInicio(ahora);
			suscripcion.setFechaFin(ahora.plusMonths(1));
			suscripcionRepository.save(suscripcion);

			BigDecimal creditos = suscripcion.getPrecio().multiply(new BigDecimal("1.5"));
			Long usuarioId = suscripcion.getUsuario().getId();

			walletService.crearWalletSiNoExiste(usuarioId);
			walletService.sumarCreditos(usuarioId, creditos);
		}

		if (pago.getTipo() == TipoPago.RECARGA_WALLET) {
			walletService.crearWalletSiNoExiste(pago.getUsuario().getId());

			BigDecimal creditos = pago.getMonto().multiply(new BigDecimal("1.5"));

			walletService.sumarCreditos(pago.getUsuario().getId(), creditos);
		}

		return pagoRepository.save(pago);
	}

	@Override
	@Transactional
	public Pago procesarPagoWallet(Long pagoId) {
		Pago pago = pagoRepository.findById(pagoId).orElseThrow(() -> new RuntimeException("Pago no encontrado"));

		if (pago.getMetodo() != MetodoPago.WALLET) {
			throw new RuntimeException("El pago no es Wallet");
		}

		walletService.restarCreditos(pago.getUsuario().getId(), pago.getMonto());

		return confirmarPago(pagoId);
	}

	private void validarLimiteRecarga(Long usuarioId, BigDecimal montoRecarga) {
		LocalDateTime inicioMes = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0)
				.withNano(0);

		LocalDateTime finMes = inicioMes.plusMonths(1);

		BigDecimal totalMes = pagoRepository.obtenerTotalRecargadoMes(usuarioId, inicioMes, finMes);

		BigDecimal limite = new BigDecimal("200");

		if (totalMes.add(montoRecarga).compareTo(limite) > 0) {
			throw new RuntimeException("Has alcanzado el límite mensual de recarga (S/200)");
		}
	}
}