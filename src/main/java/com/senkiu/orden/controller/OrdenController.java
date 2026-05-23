package com.senkiu.orden.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.senkiu.orden.model.Orden;
import com.senkiu.orden.repository.OrdenRepository;
import com.senkiu.orden.service.OrdenService;
import com.senkiu.security.CustomUserDetails;

@Controller
@RequestMapping("/orden")
public class OrdenController {

    private final OrdenService ordenService;
    private final OrdenRepository ordenRepository;

    public OrdenController(
            OrdenService ordenService,
            OrdenRepository ordenRepository
    ) {
        this.ordenService = ordenService;
        this.ordenRepository = ordenRepository;
    }

    @PostMapping("/crear/{empresaId}")
        public String crearOrden(
                @PathVariable Long empresaId,
                @AuthenticationPrincipal CustomUserDetails user,
                RedirectAttributes redirectAttributes
        ) {

        try {

                Orden orden = ordenService.crearOrden(
                        user.getId(),
                        empresaId
                );

                return "redirect:/orden/" + orden.getId() + "/pago";

        } catch (RuntimeException e) {

                redirectAttributes.addFlashAttribute(
                        "error",
                        e.getMessage()
                );

                return "redirect:/productos";
        }
        }

    // =========================
    // NUEVA VISTA: PAGO ORDEN
    // =========================
    @GetMapping("/{ordenId}/pago")
    public String vistaPagoOrden(
            @PathVariable Long ordenId,
            Model model
    ) {

        Orden orden = ordenRepository.findById(ordenId)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        model.addAttribute("orden", orden);

        return "orden/orden-pago";
    }

    @PostMapping("/{ordenId}/estado")
    public String cambiarEstado(
            @PathVariable Long ordenId,
            @RequestParam String estado
    ) {

        ordenService.cambiarEstado(ordenId, estado);

        return "redirect:/empresa/ordenes";
    }
}