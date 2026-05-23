package com.senkiu.carrito.repository;

import com.senkiu.carrito.model.Carrito;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarritoRepository
        extends JpaRepository<Carrito, Long> {

    Optional<Carrito> findByUsuarioIdAndEmpresaId(
            Long usuarioId,
            Long empresaId
    );
}