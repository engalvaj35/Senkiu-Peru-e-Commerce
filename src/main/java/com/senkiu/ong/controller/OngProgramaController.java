package com.senkiu.ong.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.senkiu.ong.model.Ong;
import com.senkiu.ong.repository.OngRepository;
import com.senkiu.programa.dto.ProgramaSocialDTO;
import com.senkiu.programa.service.ProgramaSocialService;
import com.senkiu.security.CustomUserDetails;
import com.senkiu.solicitud.programa.dto.SolicitudProgramaRequest;
import com.senkiu.solicitud.programa.model.SolicitudPrograma;
import com.senkiu.solicitud.programa.service.SolicitudProgramaService;
import com.senkiu.categoria.repository.CategoriaRepository;

@Controller
@RequestMapping("/ong")
public class OngProgramaController {

    private final ProgramaSocialService programaSocialService;

    private final SolicitudProgramaService solicitudProgramaService;

    private final OngRepository ongRepository;

    private final CategoriaRepository categoriaRepository;

    public OngProgramaController(
            ProgramaSocialService programaSocialService,
            SolicitudProgramaService solicitudProgramaService,
            OngRepository ongRepository,
            CategoriaRepository categoriaRepository
    ) {

        this.programaSocialService =
                programaSocialService;

        this.solicitudProgramaService =
                solicitudProgramaService;

        this.ongRepository =
                ongRepository;

        this.categoriaRepository = 
                categoriaRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard() {

        return "ong/dashboard";
    }

    @GetMapping("/programas")
    public String listarProgramas(
            Model model,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        Long userId = userDetails.getId();

        Ong ong = ongRepository
                .findByUsuarioId(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "ONG no encontrada"
                        ));

        List<ProgramaSocialDTO> programas =
                programaSocialService
                        .listarPorOng(ong.getId());

        model.addAttribute(
                "programas",
                programas
        );

        return "ong/lista";
    }

    @GetMapping("/solicitudes")
    public String listarSolicitudes(
            Model model,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        Long userId = userDetails.getId();

        Ong ong = ongRepository
                .findByUsuarioId(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "ONG no encontrada"
                        ));

        List<SolicitudPrograma> solicitudes =
                solicitudProgramaService
                        .listarPorOng(ong.getId());

        model.addAttribute(
                "solicitudes",
                solicitudes
        );

        return "ong/solicitudes";
    }

    @GetMapping("/programas/nuevo")
    public String mostrarFormulario(
            Model model
    ) {

        model.addAttribute(
                "solicitudProgramaRequest",
                new SolicitudProgramaRequest()
        );

        model.addAttribute(
                "categorias",
                categoriaRepository.findByTipo("PROGRAMA")
        );

        return "ong/programa";
    }

    @PostMapping("/programas/nuevo")
    public String guardarSolicitud(
            @ModelAttribute
            SolicitudProgramaRequest solicitudProgramaRequest,

            @AuthenticationPrincipal
            CustomUserDetails userDetails
    ) {

        Long userId = userDetails.getId();

        Ong ong = ongRepository
                .findByUsuarioId(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "ONG no encontrada"
                        ));

        solicitudProgramaService.crearSolicitud(
                ong.getId(),
                solicitudProgramaRequest
        );

        return "redirect:/ong/solicitudes";
    }
}