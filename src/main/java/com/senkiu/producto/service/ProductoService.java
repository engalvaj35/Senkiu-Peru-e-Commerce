package com.senkiu.producto.service;

import com.senkiu.producto.model.Producto;
import com.senkiu.solicitud.producto.model.SolicitudProducto;
import com.senkiu.producto.dto.ProductoResponse;

import java.math.BigDecimal;
import java.util.List;

public interface ProductoService {

    Producto crearDesdeSolicitud(SolicitudProducto solicitud);

    List<ProductoResponse> listarPorEmpresa(Long empresaId);
    
    List<ProductoResponse> listarPublicados();

    List<ProductoResponse> listarPublicados(String nombre);

    List<ProductoResponse> listarPublicados(
            String nombre,
            String categoria
    );

    void actualizarPrecioYStock(
            Long productoId,
            Long usuarioId,
            BigDecimal precio,
            Integer stockAgregar
    );
}