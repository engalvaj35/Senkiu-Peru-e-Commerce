package com.senkiu.ticket.service.impl;

import com.senkiu.estado.pago.model.EstadoPago;
import com.senkiu.estado.pago.repository.EstadoPagoRepository;
import com.senkiu.estado.ticket.model.EstadoTicket;
import com.senkiu.estado.ticket.repository.EstadoTicketRepository;
import com.senkiu.pago.model.MetodoPago;
import com.senkiu.pago.model.Pago;
import com.senkiu.pago.model.TipoPago;
import com.senkiu.pago.repository.PagoRepository;
import com.senkiu.rifa.model.Rifa;
import com.senkiu.rifa.repository.RifaRepository;
import com.senkiu.rifa.ticket.dto.TicketRifaDetalleResponse;
import com.senkiu.ticket.dto.TicketResponse;
import com.senkiu.ticket.model.Ticket;
import com.senkiu.ticket.repository.TicketRepository;
import com.senkiu.ticket.service.TicketService;
import com.senkiu.usuario.model.Usuario;
import com.senkiu.usuario.repository.UsuarioRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final UsuarioRepository usuarioRepository;
    private final RifaRepository rifaRepository;
    private final EstadoTicketRepository estadoTicketRepository;
    private final PagoRepository pagoRepository;
    private final EstadoPagoRepository estadoPagoRepository;

    public TicketServiceImpl(
            TicketRepository ticketRepository,
            UsuarioRepository usuarioRepository,
            RifaRepository rifaRepository,
            EstadoTicketRepository estadoTicketRepository,
            PagoRepository pagoRepository,
            EstadoPagoRepository estadoPagoRepository
    ) {
        this.ticketRepository = ticketRepository;
        this.usuarioRepository = usuarioRepository;
        this.rifaRepository = rifaRepository;
        this.estadoTicketRepository = estadoTicketRepository;
        this.pagoRepository = pagoRepository;
        this.estadoPagoRepository = estadoPagoRepository;
    }

    @Override
    public List<TicketResponse> obtenerMisTickets(Long usuarioId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));

        return ticketRepository.findByUsuario(usuario)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public Long comprarTickets(
            Long usuarioId,
            Long rifaId,
            int cantidad
    ) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));

        Rifa rifa = rifaRepository.findById(rifaId)
                .orElseThrow(() ->
                        new RuntimeException("Rifa no encontrada"));

        if (!"ACTIVA".equalsIgnoreCase(
                rifa.getEstado().getNombre()
        )) {

            throw new RuntimeException(
                    "La rifa no está activa"
            );
        }

        if (rifa.getFechaFin().isBefore(
                java.time.LocalDateTime.now()
        )) {

            throw new RuntimeException(
                    "La rifa ya finalizó"
            );
        }

        int disponibles =
                rifa.getStockTickets()
                        - rifa.getTicketsVendidos();

        if (cantidad > disponibles) {

            throw new RuntimeException(
                    "No hay suficientes tickets disponibles"
            );
        }

        EstadoTicket estadoPendiente =
                estadoTicketRepository
                        .findByNombre("PENDIENTE_PAGO")
                        .orElseThrow();

        EstadoPago estadoPago =
                estadoPagoRepository
                        .findByNombre("PENDIENTE")
                        .orElseThrow();

        Pago pago = new Pago();

        pago.setUsuario(usuario);
        pago.setTipo(TipoPago.TICKET);
        pago.setMetodo(MetodoPago.MOCK);

        pago.setMonto(
                rifa.getPrecioTicket().multiply(
                        BigDecimal.valueOf(cantidad)
                )
        );

        pago.setEstado(estadoPago);

        pago = pagoRepository.save(pago);

        for (int i = 0; i < cantidad; i++) {

            Ticket ticket = new Ticket();

            ticket.setUsuario(usuario);

            ticket.setRifa(rifa);

            ticket.setPago(pago);

            ticket.setEstado(estadoPendiente);

            ticket.setCodigo(
                    "TCK-"
                    + UUID.randomUUID()
                    .toString()
                    .substring(0, 8)
                    .toUpperCase()
            );

            ticketRepository.save(ticket);
        }

        return pago.getId();
    }

    private TicketResponse mapToResponse(
            Ticket ticket
    ) {

        return new TicketResponse(
                ticket.getId(),
                ticket.getCodigo(),
                ticket.getRifa().getTitulo(),
                ticket.getEstado().getNombre(),
                ticket.getPago() != null
                        ? ticket.getPago().getId()
                        : null
        );
    }

    @Override
    public List<TicketRifaDetalleResponse> listarTicketsPorRifa(Long rifaId) {
        return ticketRepository.findTicketsByRifaId(rifaId);
    }
}