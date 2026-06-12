package com.senkiu.ong.controller;

import com.senkiu.solicitud.dto.SolicitudOngRequest;
import com.senkiu.solicitud.service.SolicitudOrganizacionService;
import com.senkiu.security.CustomUserDetails;
import com.senkiu.categoria.repository.CategoriaRepository;
import com.senkiu.ong.model.Ong;
import com.senkiu.ong.repository.OngRepository;
import com.senkiu.ong.service.OngService;

import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.senkiu.rifa.ticket.dto.TicketRifaDetalleResponse;

@Controller
@RequestMapping("/ong")
public class OngController {

    private final SolicitudOrganizacionService solicitudService;
    private final CategoriaRepository categoriaRepository;
    private final OngRepository ongRepository;
    private final OngService ongService;

    public OngController(
            SolicitudOrganizacionService solicitudService,
            CategoriaRepository categoriaRepository,
            OngRepository ongRepository,
            OngService ongService
    ) {
        this.solicitudService = solicitudService;
        this.categoriaRepository = categoriaRepository;
        this.ongRepository = ongRepository;
        this.ongService = ongService;
    }

    @GetMapping("/solicitud")
    public String form(Model model) {
        model.addAttribute("solicitud", new SolicitudOngRequest());
        model.addAttribute(
            "categorias",
            categoriaRepository.findByTipo("ONG")
    );
        return "ong/solicitud";
    }

    @PostMapping("/solicitud")
    public String crearSolicitud(

            @Valid
            @ModelAttribute("solicitud")
            SolicitudOngRequest request,

            BindingResult result,

            @RequestParam("archivo")
            MultipartFile archivo,

            @AuthenticationPrincipal
            CustomUserDetails user,

            Model model
    ) {

        if (result.hasErrors()) {

            model.addAttribute(
                    "categorias",
                    categoriaRepository.findByTipo("ONG")
            );

            return "ong/solicitud";
        }

        try {

            solicitudService.crearSolicitudOng(
                    user.getId(),
                    request,
                    archivo
            );

            return "redirect:/";

        } catch (RuntimeException e) {

            String mensaje = e.getMessage();

            if (mensaje.contains("número de cuenta")) {

                result.rejectValue(
                        "numeroCuenta",
                        "error.numeroCuenta",
                        mensaje
                );
            }

            if (mensaje.contains("CCI")) {

                result.rejectValue(
                        "cci",
                        "error.cci",
                        mensaje
                );
            }

            model.addAttribute(
                    "categorias",
                    categoriaRepository.findByTipo("ONG")
            );

            return "ong/solicitud";
        }
    }
    
    @GetMapping("/tickets")
    public String ticketsOng(
            Model model,
            @AuthenticationPrincipal CustomUserDetails user
    ) {

        Ong ong = ongRepository.findByUsuarioId(user.getId())
                .orElseThrow();

        List<TicketRifaDetalleResponse> tickets =
                ongService.obtenerTickets(ong.getId());

        Map<String, List<TicketRifaDetalleResponse>> ticketsPorRifa =
                tickets.stream()
                        .collect(Collectors.groupingBy(
                                TicketRifaDetalleResponse::getRifaTitulo,
                                LinkedHashMap::new,
                                Collectors.toList()
                        ));

        model.addAttribute("ticketsPorRifa", ticketsPorRifa);

        return "ong/tickets";
    }
}