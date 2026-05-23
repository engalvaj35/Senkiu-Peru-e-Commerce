package com.senkiu.suscripcion.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PathVariable;

import com.senkiu.security.CustomUserDetails;
import com.senkiu.suscripcion.service.SuscripcionService;
import com.senkiu.suscripcion.repository.SuscripcionRepository;
import com.senkiu.suscripcion.model.Suscripcion;

@Controller
@RequestMapping("/suscripciones")
public class SuscripcionViewController {

    private final SuscripcionService service;
    private final SuscripcionRepository repository;

    public SuscripcionViewController(
        SuscripcionService service,
        SuscripcionRepository repository

    ) {
        this.service = service;
        this.repository = repository;
    }

    @GetMapping
    public String verPlanes(Model model) {

        return "suscripciones/lista";
    }

    @PostMapping("/comprar")
    @PreAuthorize("isAuthenticated()")
    public String comprarSuscripcion(
            @RequestParam String plan,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            RedirectAttributes redirectAttributes
    ) {

        try {

            Suscripcion suscripcion = service.crearSuscripcion(
                    userDetails.getId(),
                    plan
            );

            return "redirect:/suscripciones/" + suscripcion.getId() + "/pago";

        } catch (RuntimeException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );

            return "redirect:/suscripciones";
        }
    }
    
    @GetMapping("/{id}/pago")
    @PreAuthorize("isAuthenticated()")
    public String vistaPago(
            @PathVariable Long id,
            Model model
    ) {

        Suscripcion suscripcion = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Suscripción no encontrada"));

        model.addAttribute("suscripcion", suscripcion);

        return "suscripciones/suscripcion-pago";
    }
}