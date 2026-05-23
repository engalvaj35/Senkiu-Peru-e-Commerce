package com.senkiu.producto.service;

public interface ProductoAdminService {

    void aprobarProducto(Long productoId);

    void rechazarProducto(Long productoId);
}