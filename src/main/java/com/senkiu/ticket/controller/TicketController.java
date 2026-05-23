package com.senkiu.ticket.controller;

import com.senkiu.rifa.ticket.dto.TicketRifaDetalleResponse;
import com.senkiu.security.CustomUserDetails;
import com.senkiu.ticket.dto.TicketResponse;
import com.senkiu.ticket.service.TicketService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(
            TicketService ticketService
    ) {
        this.ticketService = ticketService;
    }

    @PostMapping("/comprar")
    public String comprar(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam Long rifaId,
            @RequestParam int cantidad
    ) {

        Long pagoId =
                ticketService.comprarTickets(
                        user.getId(),
                        rifaId,
                        cantidad
                );

        return "redirect:/api/pagos/confirmar/" + pagoId;
    }

    @ResponseBody
    @GetMapping("/usuario")
    public List<TicketResponse> misTickets(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ticketService.obtenerMisTickets(user.getId());
    }

    @ResponseBody
    @GetMapping("/rifa/{rifaId}")
    public List<TicketRifaDetalleResponse> listarPorRifa(
            @PathVariable Long rifaId
    ) {
        return ticketService.listarTicketsPorRifa(rifaId);
    }
}