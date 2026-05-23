package com.senkiu.usuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.senkiu.usuario.model.Usuario;

import java.util.Optional;

public interface UsuarioRepository
        extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByDni(String dni);
}