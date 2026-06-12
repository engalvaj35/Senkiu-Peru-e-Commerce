package com.senkiu.ticket.service;

import com.senkiu.rifa.ticket.dto.TicketRifaDetalleResponse;
import com.senkiu.ticket.dto.TicketResponse;
import com.senkiu.pago.model.MetodoPago;

import java.util.List;

public interface TicketService {

    List<TicketResponse> obtenerMisTickets(Long usuarioId);

    Long comprarTickets(Long usuarioId, Long rifaId, int cantidad, MetodoPago metodoPago);

    List<TicketRifaDetalleResponse> listarTicketsPorRifa(Long rifaId);
}