package com.senkiu.estado.entrega.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.senkiu.estado.entrega.model.EstadoEntrega;

public interface EstadoEntregaRepository
        extends JpaRepository<EstadoEntrega, Long> {

    Optional<EstadoEntrega> findByNombre(String nombre);
}