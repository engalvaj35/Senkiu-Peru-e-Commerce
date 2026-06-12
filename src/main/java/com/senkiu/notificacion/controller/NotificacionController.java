package com.senkiu.notificacion.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.*;

import com.senkiu.notificacion.dto.NotificacionResponse;
import com.senkiu.notificacion.model.Notificacion;
import com.senkiu.notificacion.service.NotificacionService;
import com.senkiu.security.CustomUserDetails;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(
            NotificacionService notificacionService
    ) {
        this.notificacionService = notificacionService;
    }

    @GetMapping
    public List<NotificacionResponse> listar(
            @AuthenticationPrincipal CustomUserDetails user
    ) {

        return notificacionService
                .listarPorUsuario(user.getId())
                .stream()
                .map(this::convertir)
                .collect(Collectors.toList());
    }

    @GetMapping("/contador")
    public long contador(
            @AuthenticationPrincipal CustomUserDetails user
    ) {

        return notificacionService
                .contarNoLeidas(user.getId());
    }

    @PostMapping("/{id}/leer")
    public void marcarLeida(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails user
    ) {

        notificacionService.marcarLeida(
                id,
                user.getId()
        );
    }

    private NotificacionResponse convertir(
            Notificacion n
    ) {

        NotificacionResponse dto =
                new NotificacionResponse();

        dto.setId(n.getId());
        dto.setTipo(n.getTipo());
        dto.setTitulo(n.getTitulo());
        dto.setMensaje(n.getMensaje());
        dto.setLeido(n.getLeido());
        dto.setCreatedAt(n.getCreatedAt());

        return dto;
    }
}