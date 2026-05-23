package com.senkiu.empresa.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import com.senkiu.security.CustomUserDetails;
import com.senkiu.solicitud.producto.dto.SolicitudProductoRequest;
import com.senkiu.solicitud.producto.service.SolicitudProductoService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/empresa/productos")
public class EmpresaProductoController {

    private final SolicitudProductoService solicitudService;

    public EmpresaProductoController(
            SolicitudProductoService solicitudService
    ) {
        this.solicitudService = solicitudService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void crearSolicitudProducto(

            @Valid
            @ModelAttribute SolicitudProductoRequest request,

            @AuthenticationPrincipal CustomUserDetails user,

            @RequestParam("archivo") MultipartFile archivo
    ) {

        solicitudService.crearSolicitud(
                user.getId(),
                request,
                archivo
        );
    }
}