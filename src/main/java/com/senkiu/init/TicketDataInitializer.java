package com.senkiu.init;

import com.senkiu.estado.ticket.model.EstadoTicket;
import com.senkiu.estado.ticket.repository.EstadoTicketRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class TicketDataInitializer {

    private final EstadoTicketRepository estadoTicketRepository;

    public TicketDataInitializer(EstadoTicketRepository estadoTicketRepository) {
        this.estadoTicketRepository = estadoTicketRepository;
    }

    @PostConstruct
    public void init() {
        crearEstadoSiNoExiste("PENDIENTE_PAGO");
        crearEstadoSiNoExiste("ACTIVO");
        crearEstadoSiNoExiste("GANADOR");
        crearEstadoSiNoExiste("FINALIZADO");
        crearEstadoSiNoExiste("ANULADO");
    }

    private void crearEstadoSiNoExiste(String nombre) {

        estadoTicketRepository.findByNombre(nombre)
                .orElseGet(() -> {
                    EstadoTicket estado = new EstadoTicket();
                    estado.setNombre(nombre);
                    return estadoTicketRepository.save(estado);
                });
    }
}