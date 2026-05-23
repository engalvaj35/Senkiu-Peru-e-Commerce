package com.senkiu.ticket.repository;

import com.senkiu.ticket.model.Ticket;
import com.senkiu.usuario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.senkiu.rifa.ticket.dto.TicketRifaDetalleResponse;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByUsuario(Usuario usuario);

    List<Ticket> findByRifaId(Long rifaId);

    @Query("""
    SELECT new com.senkiu.rifa.ticket.dto.TicketRifaDetalleResponse(
        t.id,
        t.rifa.id,
        t.rifa.titulo,
        t.usuario.id,
        t.usuario.nombres,
        t.usuario.email,
        t.createdAt,
        t.estado.nombre
        )
        FROM Ticket t
        WHERE t.rifa.id = :rifaId
        ORDER BY t.createdAt DESC
    """)
    List<TicketRifaDetalleResponse> findTicketsByRifaId(Long rifaId);   

    List<Ticket> findByPagoId(Long pagoId);
}