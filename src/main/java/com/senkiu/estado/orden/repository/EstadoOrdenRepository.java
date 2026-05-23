package com.senkiu.estado.orden.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.senkiu.estado.orden.model.EstadoOrden;

public interface EstadoOrdenRepository
        extends JpaRepository<EstadoOrden, Long> {

    Optional<EstadoOrden> findByNombre(String nombre);
}