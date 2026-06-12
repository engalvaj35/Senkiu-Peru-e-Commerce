package com.senkiu.rifa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.senkiu.programa.repository.ProgramaSocialRepository;
import com.senkiu.ong.repository.OngRepository;
import com.senkiu.security.CustomUserDetails;

@Controller
@RequestMapping("/ong/rifas")
public class RifaOngViewController {

    private final ProgramaSocialRepository programaRepository;
    private final OngRepository ongRepository;

    public RifaOngViewController(
            ProgramaSocialRepository programaRepository,
            OngRepository ongRepository
    ) {
        this.programaRepository = programaRepository;
        this.ongRepository = ongRepository;
    }

    @GetMapping
    public String listar() {
        return "ong/rifas/lista";
    }

    @GetMapping("/crear")
    public String crear(Model model,
                        @AuthenticationPrincipal CustomUserDetails user) {

        Long usuarioId = user.getId();

        Long ongId = ongRepository.findByUsuarioId(usuarioId)
                .orElseThrow()
                .getId();

        model.addAttribute(
                "programas",
                programaRepository.findByOngId(ongId)
        );

        return "ong/rifas/crear";
    }
}