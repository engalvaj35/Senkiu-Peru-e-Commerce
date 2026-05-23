package com.senkiu.rifa.controller;

import com.senkiu.empresa.model.Empresa;
import com.senkiu.empresa.service.EmpresaService;
import com.senkiu.rifa.dto.AsociarEmpresaRifaRequest;
import com.senkiu.rifa.dto.RifaResponse;
import com.senkiu.rifa.service.RifaService;
import com.senkiu.security.CustomUserDetails;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empresa/rifas")
public class RifaEmpresaController {

    private final RifaService rifaService;
    private final EmpresaService empresaService;

    public RifaEmpresaController(
            RifaService rifaService,
            EmpresaService empresaService
    ) {
        this.rifaService = rifaService;
        this.empresaService = empresaService;
    }

    @GetMapping("/disponibles")
    public List<RifaResponse> listarDisponibles() {

        return rifaService.listarRifasPendientes();
    }

    @PostMapping("/asociar")
    public void asociarEmpresa(

            @RequestBody AsociarEmpresaRifaRequest request,

            @AuthenticationPrincipal
            CustomUserDetails user
    ) {

        Empresa empresa =
                empresaService.obtenerPorUsuario(user.getId());

        rifaService.asociarEmpresa(
                request,
                empresa.getId()
        );
    }

    @GetMapping("/mis-rifas")
    public List<RifaResponse> misRifas(

            @AuthenticationPrincipal
            CustomUserDetails user
    ) {

        Empresa empresa =
                empresaService.obtenerPorUsuario(user.getId());

        return rifaService.listarPorEmpresa(
                empresa.getId()
        );
    }
}