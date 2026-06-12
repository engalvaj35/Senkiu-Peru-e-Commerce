package com.senkiu.rifa.controller;

import com.senkiu.rifa.dto.CrearRifaRequest;
import com.senkiu.rifa.dto.RifaResponse;
import com.senkiu.rifa.service.RifaService;
import com.senkiu.security.CustomUserDetails;
import com.senkiu.ong.repository.OngRepository;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/ong/rifas")
@PreAuthorize("hasRole('ONG')")
public class RifaOngController {

    private final RifaService rifaService;
    private final OngRepository ongRepository;

    public RifaOngController(
            RifaService rifaService,
            OngRepository ongRepository
    ) {
        this.rifaService = rifaService;
        this.ongRepository = ongRepository;
    }

    @PostMapping(consumes = "multipart/form-data")
    public void crear(
            @RequestParam Long programaId,
            @RequestParam String titulo,
            @RequestParam String descripcion,
            @RequestParam BigDecimal precioTicket,
            @RequestParam Integer stockTickets,
            @RequestParam String fechaFin,
            @RequestParam(required = false) MultipartFile imagen
    ) {

        CrearRifaRequest request = new CrearRifaRequest();

        request.setProgramaId(programaId);
        request.setTitulo(titulo);
        request.setDescripcion(descripcion);
        request.setPrecioTicket(precioTicket);
        request.setStockTickets(stockTickets);
        request.setFechaFin(LocalDateTime.parse(fechaFin));

        rifaService.crearRifa(request, imagen);
    }

    @GetMapping
    public List<RifaResponse> listarPorOng(@AuthenticationPrincipal CustomUserDetails user) {

        Long ongId = ongRepository.findByUsuarioId(user.getId())
                .orElseThrow()
                .getId();

        return rifaService.listarPorOng(ongId);
    }

    @PutMapping("/{id}/enviar-revision")
    public void enviarRevision(
            @PathVariable Long id
    ) {

        rifaService.enviarRevision(id);
    }
}