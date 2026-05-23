package com.senkiu.carrito.service;

import com.senkiu.carrito.dto.CarritoResponse;

public interface CarritoService {

    Long agregarProducto(
            Long usuarioId,
            Long productoId,
            Integer cantidad
    );

    CarritoResponse obtenerCarrito(
            Long usuarioId,
            Long empresaId
    );

    void eliminarItem(
            Long carritoItemId
    );

    void vaciarCarrito(
            Long carritoId
    );
}