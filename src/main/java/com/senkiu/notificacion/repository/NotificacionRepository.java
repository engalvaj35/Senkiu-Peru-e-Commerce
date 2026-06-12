package com.senkiu.notificacion.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.senkiu.notificacion.model.Notificacion;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    List<Notificacion> findByUsuarioIdOrderByCreatedAtDesc(Long usuarioId);

    long countByUsuarioIdAndLeidoFalse(Long usuarioId);

    Optional<Notificacion> findByIdAndUsuarioId(
            Long id,
            Long usuarioId
    );
}