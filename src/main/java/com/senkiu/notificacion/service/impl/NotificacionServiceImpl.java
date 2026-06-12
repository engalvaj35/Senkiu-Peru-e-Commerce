package com.senkiu.notificacion.service.impl;

import java.time.LocalDateTime;

import java.util.*;

import org.springframework.stereotype.Service;

import com.senkiu.notificacion.model.Notificacion;
import com.senkiu.notificacion.repository.NotificacionRepository;
import com.senkiu.notificacion.service.NotificacionService;
import com.senkiu.usuario.model.Usuario;
import com.senkiu.usuario.repository.UsuarioRepository;

@Service
public class NotificacionServiceImpl implements NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final UsuarioRepository usuarioRepository;

    public NotificacionServiceImpl(
            NotificacionRepository notificacionRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.notificacionRepository = notificacionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void crear(
            Long usuarioId,
            String tipo,
            String titulo,
            String mensaje
    ) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));

        Notificacion n = new Notificacion();

        n.setUsuario(usuario);
        n.setTipo(tipo);
        n.setTitulo(titulo);
        n.setMensaje(mensaje);

        notificacionRepository.save(n);
    }

    @Override
    public List<Notificacion> listarPorUsuario(Long usuarioId) {
        return notificacionRepository
                .findByUsuarioIdOrderByCreatedAtDesc(usuarioId);
    }

    @Override
    public long contarNoLeidas(Long usuarioId) {
        return notificacionRepository
                .countByUsuarioIdAndLeidoFalse(usuarioId);
    }

    @Override
    public void marcarLeida(Long id, Long usuarioId) {

        Notificacion n =
                notificacionRepository
                        .findByIdAndUsuarioId(id, usuarioId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notificación no encontrada"
                                ));

        n.setLeido(true);
        n.setLeidoAt(LocalDateTime.now());

        notificacionRepository.save(n);
    }
}