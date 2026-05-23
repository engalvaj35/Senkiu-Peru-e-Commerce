package com.senkiu.carrito.repository;

import com.senkiu.carrito.model.CarritoItem;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarritoItemRepository
        extends JpaRepository<CarritoItem, Long> {

    Optional<CarritoItem> findByCarritoIdAndProductoId(
            Long carritoId,
            Long productoId
    );
}