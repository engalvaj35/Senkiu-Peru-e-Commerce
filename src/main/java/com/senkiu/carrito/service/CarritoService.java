package com.senkiu.carrito.service;

import com.senkiu.carrito.dto.CarritoResponse;

public interface CarritoService {

    Long agregarProducto(Long usuarioId, String sessionId, Long productoId, Integer cantidad);

    CarritoResponse obtenerCarrito(Long usuarioId, String sessionId, Long empresaId);

    void eliminarItem(Long carritoItemId);

    void vaciarCarrito(Long carritoId);
}