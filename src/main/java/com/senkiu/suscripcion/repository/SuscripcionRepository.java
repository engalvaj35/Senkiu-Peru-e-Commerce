package com.senkiu.suscripcion.repository;

import com.senkiu.suscripcion.model.Suscripcion;
import com.senkiu.usuario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SuscripcionRepository extends JpaRepository<Suscripcion, Long> {

    Optional<Suscripcion> findByUsuario(Usuario usuario);
}