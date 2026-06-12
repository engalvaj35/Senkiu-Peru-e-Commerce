package com.senkiu.rifa.controller;

import com.senkiu.rifa.dto.RifaEmpresaResponse;
import com.senkiu.rifa.dto.RifaResponse;
import com.senkiu.rifa.service.RifaService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rifas")
public class RifaController {

    private final RifaService rifaService;

    public RifaController(RifaService rifaService) {
        this.rifaService = rifaService;
    }

    @GetMapping
    public List<RifaResponse> listar(
            @RequestParam(required = false) String nombre
    ) {

        return rifaService.listarRifasActivas(nombre);
    }

    @GetMapping("/{rifaId}/empresas")
    public List<RifaEmpresaResponse> listarEmpresas(
            @PathVariable Long rifaId
    ) {

        return rifaService.listarEmpresasAsociadas(rifaId);
    }

    @GetMapping("/{id}")
    public RifaResponse detalle(@PathVariable Long id) {
        return rifaService.obtenerDetalle(id);
    }
}