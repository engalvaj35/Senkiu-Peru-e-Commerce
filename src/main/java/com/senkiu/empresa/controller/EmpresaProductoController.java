package com.senkiu.empresa.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import com.senkiu.empresa.repository.EmpresaRepository;
import com.senkiu.security.CustomUserDetails;
import com.senkiu.solicitud.producto.dto.SolicitudProductoRequest;
import com.senkiu.solicitud.producto.service.SolicitudProductoService;
import com.senkiu.empresa.model.Empresa;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/empresa/productos")
public class EmpresaProductoController {

    private final SolicitudProductoService solicitudService;
    private final EmpresaRepository empresaRepository;

    public EmpresaProductoController(
            SolicitudProductoService solicitudService,
            EmpresaRepository empresaRepository
    ) {
        this.solicitudService = solicitudService;
        this.empresaRepository = empresaRepository;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void crearSolicitudProducto(

            @Valid
            @ModelAttribute SolicitudProductoRequest request,

            @AuthenticationPrincipal CustomUserDetails user,

            @RequestParam("archivo") MultipartFile archivo
    ) {

        Empresa empresa = empresaRepository
                .findByUsuarioId(user.getId())
                .orElseThrow(() ->
                        new RuntimeException("Empresa no encontrada")
                );

        solicitudService.crearSolicitud(
                empresa.getId(),
                request,
                archivo
        );
    }
}
