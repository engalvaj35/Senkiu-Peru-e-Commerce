package com.senkiu.ong.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.senkiu.ong.model.Ong;
import com.senkiu.usuario.model.Usuario;

public interface OngRepository
        extends JpaRepository<Ong, Long> {

    Optional<Ong> findByUsuarioId(Long usuarioId);

    Optional<Ong> findByUsuario(Usuario usuario);
}