package com.senkiu.ong.controller;

import com.senkiu.solicitud.dto.SolicitudOngRequest;
import com.senkiu.solicitud.service.SolicitudOrganizacionService;
import com.senkiu.security.CustomUserDetails;
import com.senkiu.categoria.repository.CategoriaRepository;

import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/ong")
public class OngController {

    private final SolicitudOrganizacionService solicitudService;
    private final CategoriaRepository categoriaRepository;

    public OngController(
            SolicitudOrganizacionService solicitudService,
            CategoriaRepository categoriaRepository
    ) {
        this.solicitudService = solicitudService;
        this.categoriaRepository = categoriaRepository;
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
    
}