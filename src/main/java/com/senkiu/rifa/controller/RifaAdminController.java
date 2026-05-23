package com.senkiu.rifa.controller;

import com.senkiu.rifa.dto.RifaResponse;
import com.senkiu.rifa.service.RifaService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/rifas")
@PreAuthorize("hasRole('ADMIN')")
public class RifaAdminController {

    private final RifaService rifaService;

    public RifaAdminController(
            RifaService rifaService
    ) {
        this.rifaService = rifaService;
    }

    @GetMapping("/pendientes")
    public List<RifaResponse> listarPendientes() {

        return rifaService
                .listarRifasPendientesAprobacion();
    }

    @PutMapping("/{id}/aprobar")
    public void aprobar(
            @PathVariable Long id
    ) {

        rifaService.activarRifa(id);
    }

    @PutMapping("/{id}/rechazar")
    public void rechazar(
            @PathVariable Long id
    ) {

        rifaService.rechazarRifa(id);
    }

    @GetMapping("/activas")
    public List<RifaResponse> listarActivas() {

        return rifaService.listarRifasActivas();
    }

    @GetMapping("/rechazadas")
    public List<RifaResponse> listarRechazadas() {

        return rifaService.listarRifasRechazadas();
    }
}