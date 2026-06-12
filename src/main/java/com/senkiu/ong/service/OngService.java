package com.senkiu.ong.service;

import com.senkiu.ong.model.Ong;
import com.senkiu.rifa.ticket.dto.TicketRifaDetalleResponse;
import com.senkiu.solicitud.model.SolicitudOrganizacion;

import java.util.List;

public interface OngService {

    Ong obtenerPorUsuario(Long usuarioId);

    Ong crearDesdeSolicitud(SolicitudOrganizacion solicitud);

    void suspender(Long ongId);

    void activar(Long ongId);

    List<Ong> findAll();

    List<TicketRifaDetalleResponse> obtenerTickets(Long ongId);
}