package com.senkiu.usuario.controller;

import com.senkiu.security.CustomUserDetails;
import com.senkiu.usuario.dto.UsuarioPerfilRequest;
import com.senkiu.usuario.dto.UsuarioPerfilResponse;
import com.senkiu.usuario.service.UsuarioService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PutMapping("/perfil")
    @PreAuthorize("isAuthenticated()")
    public UsuarioPerfilResponse completarPerfil(
            @Valid @RequestBody UsuarioPerfilRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest requestHttp,
            HttpServletResponse responseHttp
    ) {

        String emailActual = userDetails.getEmail();

        UsuarioPerfilResponse response =
                service.completarPerfil(
                        userDetails.getId(),
                        request
                );

        // SI CAMBIÓ EL EMAIL -> LOGOUT
        if (!emailActual.equals(request.getEmail())) {

            new SecurityContextLogoutHandler()
                    .logout(
                            requestHttp,
                            responseHttp,
                            null
                    );
        }

        return response;
    }

    @GetMapping("/perfil")
    @PreAuthorize("isAuthenticated()")
    public UsuarioPerfilResponse obtenerPerfil(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        return service.obtenerPerfil(
                userDetails.getId()
        );
    }
}