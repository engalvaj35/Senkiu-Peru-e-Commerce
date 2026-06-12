package com.senkiu.notificacion.service;

import com.senkiu.notificacion.model.Notificacion;

import java.util.*;

public interface NotificacionService {

    void crear(
            Long usuarioId,
            String tipo,
            String titulo,
            String mensaje
    );

    List<Notificacion> listarPorUsuario(Long usuarioId);

    long contarNoLeidas(Long usuarioId);

    void marcarLeida(Long id, Long usuarioId);
}