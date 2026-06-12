package com.senkiu.usuario.repository;

import com.senkiu.usuario.model.EstadoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadoUsuarioRepository
        extends JpaRepository<EstadoUsuario, Long> {

    Optional<EstadoUsuario> findByNombre(String nombre);
}