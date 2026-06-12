package com.senkiu.orden.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.senkiu.orden.model.OrdenDetalle;

public interface OrdenDetalleRepository
        extends JpaRepository<OrdenDetalle, Long> {
}