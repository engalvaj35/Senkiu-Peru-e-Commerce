package com.senkiu.estado.ticket.repository;

import com.senkiu.estado.ticket.model.EstadoTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadoTicketRepository extends JpaRepository<EstadoTicket, Long> {

    Optional<EstadoTicket> findByNombre(String nombre);

}