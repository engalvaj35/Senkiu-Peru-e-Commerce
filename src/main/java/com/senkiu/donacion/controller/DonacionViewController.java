package com.senkiu.donacion.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.senkiu.donacion.dto.DonacionRequest;
import com.senkiu.donacion.model.Donacion;
import com.senkiu.donacion.service.DonacionService;
import com.senkiu.security.CustomUserDetails;

@Controller
@RequestMapping("/donaciones")
public class DonacionViewController {

    private final DonacionService donacionService;

    public DonacionViewController(
            DonacionService donacionService
    ) {
        this.donacionService = donacionService;
    }

    @GetMapping("/nuevo/{programaId}")
    public String vistaDonacion(
            @PathVariable Long programaId,
            Model model
    ) {

        model.addAttribute("programaId", programaId);
        model.addAttribute("donacionRequest", new DonacionRequest());

        return "donaciones/donar";
    }

    @PostMapping("/nuevo/{programaId}")
    public String procesarDonacion(
            @PathVariable Long programaId,
            @ModelAttribute DonacionRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {

        request.setProgramaId(programaId);

        Donacion donacion =
                donacionService.donar(user.getId(), request);

        return "redirect:/donaciones/pago/" + donacion.getId();
    }

    @GetMapping("/pago/{donacionId}")
    public String vistaPagoDonacion(
            @PathVariable Long donacionId,
            Model model
    ) {

        Donacion donacion =
                donacionService.buscarPorId(donacionId);

        model.addAttribute("donacion", donacion);

        return "donaciones/donacion-pago";
    }
}